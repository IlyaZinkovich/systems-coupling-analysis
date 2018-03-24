package io.coupling.systems.analysis;

import org.neo4j.driver.v1.Session;

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
  public void persist(Session session) {

  }
}
