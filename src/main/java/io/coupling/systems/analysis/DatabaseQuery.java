package io.coupling.systems.analysis;

import com.google.common.collect.ImmutableMap;
import java.util.Map;

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

  public Map<String, Object> toParameters() {
    return ImmutableMap.<String, Object>builder()
        .put("query", query)
        .build();
  }
}
