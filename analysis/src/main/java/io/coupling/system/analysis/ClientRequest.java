package io.coupling.system.analysis;

import java.util.HashMap;
import java.util.Map;
import org.neo4j.driver.v1.Session;
import org.neo4j.driver.v1.Statement;
import org.neo4j.driver.v1.StatementResult;

class ClientRequest implements GraphObject {

  private final Endpoint endpoint;
  private final Trace trace;

  ClientRequest(final Endpoint endpoint, final Trace trace) {
    this.endpoint = endpoint;
    this.trace = trace;
  }

  @Override
  public String toString() {
    return "ClientRequest{" +
        "endpoint=" + endpoint +
        ", trace=" + trace +
        '}';
  }

  @Override
  public void persist(final Session session) {
    final Map<String, Object> parameters = new HashMap<>();
    parameters.putAll(endpoint.toParameters());
    parameters.putAll(trace.toParameters());
    parameters.put("userAgent", "Browser");
    final String persistClientRequest = "MERGE (client:ApiClient {userAgent:$userAgent})\n "
        + "MERGE (api:Api {endpoint:$endpoint, service:$service})\n "
        + "MERGE (s:Service {name:$service})\n "
        + "CREATE (client)-[:TRACE {traceId:$traceId, spanId:$spanId}]->"
        + "(api)-[:TRACE {traceId:$traceId, spanId:$spanId}]->(s)";
    final Statement persistClientRequestStatement = new Statement(persistClientRequest, parameters);
    session.writeTransaction(transaction -> transaction.run(persistClientRequestStatement));
    persistCallingServiceRelationIfExist(session, parameters);
  }

  private void persistCallingServiceRelationIfExist(final Session session,
      final Map<String, Object> parameters) {
    final String getCallingService =
        "MATCH ()-[t:TRACE {traceId: $traceId}]->(s:Service) WHERE s.name<>$service\n"
            + "RETURN s.name";
    final StatementResult result = session.readTransaction(transaction -> {
      final Statement statement = new Statement(getCallingService, parameters);
      return transaction.run(statement);
    });
    result.forEachRemaining(record -> {
      final String callingService = record.get("s.name").asString();
      parameters.put("callingService", callingService);
      final String persistRelation =
          "MATCH (s:Service) WHERE s.name=$callingService\n"
              + "MATCH (api:Api) WHERE api.endpoint=$endpoint AND api.service=$service\n"
              + "CREATE (s)-[:TRACE {traceId: $traceId, spanId: $spanId}]->(api)";
      final Statement persistRelationStatement = new Statement(persistRelation, parameters);
      session.writeTransaction(transaction -> transaction.run(persistRelationStatement));
    });
  }
}
