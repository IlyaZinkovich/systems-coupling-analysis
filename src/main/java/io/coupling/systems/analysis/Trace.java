package io.coupling.systems.analysis;

class Trace {

  private final String traceId;
  private final String spanId;

  Trace(final String traceId, final String spanId) {
    this.traceId = traceId;
    this.spanId = spanId;
  }

  @Override
  public String toString() {
    return "Trace{" +
        "traceId='" + traceId + '\'' +
        ", spanId='" + spanId + '\'' +
        '}';
  }
}
