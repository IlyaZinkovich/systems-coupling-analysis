package io.coupling.systems.analysis;

class DatabaseQuery {

  private final String query;

  DatabaseQuery(final String query) {
    this.query = query;
  }

  @Override
  public String toString() {
    return "DatabaseQuery{" +
        "query='" + query + '\'' +
        '}';
  }
}
