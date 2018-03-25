package io.coupling.system.analysis;

import static java.lang.String.format;

import com.google.common.collect.ImmutableMap;
import java.util.Map;

public class Endpoint {

  private final String method;
  private final String url;

  public Endpoint(final String method, final String url) {
    this.method = method;
    this.url = url;
  }

  public Map<String, Object> toParameters() {
    return ImmutableMap.<String, Object>builder()
        .put("endpoint", format("%s %s", method, url))
        .build();
  }

  @Override
  public String toString() {
    return "Endpoint{" +
        "method='" + method + '\'' +
        ", url='" + url + '\'' +
        '}';
  }
}
