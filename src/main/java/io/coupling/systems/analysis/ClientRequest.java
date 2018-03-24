package io.coupling.systems.analysis;

import java.util.HashMap;
import java.util.Map;
import org.neo4j.driver.v1.Session;
import org.neo4j.driver.v1.Statement;

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
    final String text = "CREATE (:ApiClient {userAgent:$userAgent})"
        + "-[:TRACE {traceId:$traceId, spanId:$spanId}]->"
        + "(:Endpoint {method:$method, url:$url})"
        + "-[:TRACE {traceId:$traceId, spanId:$spanId}]->"
        + "(:Service {service:$service})";
    final Statement statement = new Statement(text).withParameters(parameters);
    session.writeTransaction(transaction -> transaction.run(statement));
  }
}
