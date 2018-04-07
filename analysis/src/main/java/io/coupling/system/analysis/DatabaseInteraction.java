package io.coupling.system.analysis;

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
    final String text = "MERGE (s:Service {name:$service})\n "
        + "MERGE (q:Query {query:$query})\n "
        + "MERGE (t:Table {name:$table})\n "
        + "CREATE (s)-[:TRACE {traceId:$traceId, spanId:$spanId}]->"
        + "(q)-[:TRACE {traceId:$traceId, spanId:$spanId}]->(t)";
    final Statement statement = new Statement(text).withParameters(parameters);
    session.writeTransaction(transaction -> transaction.run(statement));
  }
}
