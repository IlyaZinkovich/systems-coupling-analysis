package io.coupling.systems.analysis;

import com.google.common.collect.ImmutableMap;
import java.util.Map;

class Trace {

  private final String service;
  private final String traceId;
  private final String spanId;

  Trace(final String service, final String traceId, final String spanId) {
    this.service = service;
    this.traceId = traceId;
    this.spanId = spanId;
  }

  public Map<String, Object> toParameters() {
    return ImmutableMap.<String, Object>builder()
        .put("service", service)
        .put("traceId", traceId)
        .put("spanId", spanId)
        .build();
  }

  @Override
  public String toString() {
    return "Trace{" +
        "service='" + service + '\'' +
        ", traceId='" + traceId + '\'' +
        ", spanId='" + spanId + '\'' +
        '}';
  }
}
