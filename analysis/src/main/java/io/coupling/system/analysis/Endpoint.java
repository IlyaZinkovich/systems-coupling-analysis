package io.coupling.system.analysis;

import static java.lang.String.format;

import com.google.common.collect.ImmutableMap;
import java.util.Map;

public class Endpoint {

  private final String method;
  private final String path;

  public Endpoint(final String method, final String path) {
    this.method = method;
    this.path = path;
  }

  public Map<String, Object> toParameters() {
    return ImmutableMap.<String, Object>builder()
        .put("endpoint", format("%s %s", method, path))
        .build();
  }

  @Override
  public String toString() {
    return "Endpoint{" +
        "method='" + method + '\'' +
        ", path='" + path + '\'' +
        '}';
  }
}
