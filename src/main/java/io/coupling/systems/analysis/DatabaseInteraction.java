package io.coupling.systems.analysis;

import java.util.HashMap;
import java.util.Map;
import org.neo4j.driver.v1.Session;
import org.neo4j.driver.v1.Statement;

class DatabaseInteraction implements GraphObject {

  private final DatabaseQuery databaseQuery;
  private final Trace trace;

  DatabaseInteraction(final DatabaseQuery databaseQuery, final Trace trace) {
    this.trace = trace;
    this.databaseQuery = databaseQuery;
  }

  @Override
  public String toString() {
    return "DatabaseInteraction{" +
        "databaseQuery=" + databaseQuery +
        ", trace=" + trace +
        '}';
  }

  @Override
  public void persist(final Session session) {
    final Map<String, Object> parameters = new HashMap<>();
    parameters.putAll(databaseQuery.toParameters());
    parameters.putAll(trace.toParameters());
    final String text = "MATCH (s:Service {service:$service})\n"
        + "CREATE (s)"
        + "-[:TRACE {traceId:$traceId, spanId:$spanId}]->"
        + "(:Database {query:$query})";
    final Statement statement = new Statement(text).withParameters(parameters);
    session.writeTransaction(transaction -> transaction.run(statement));
  }
}
