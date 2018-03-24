package io.coupling.systems.analysis;

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
}
