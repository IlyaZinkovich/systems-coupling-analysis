package io.coupling.systems.analysis;

class ClientRequest implements GraphObject {

  private final Endpoint endpoint;
  private final Trace trace;

  ClientRequest(final Endpoint endpoint, final Trace trace) {
    this.endpoint = endpoint;
    this.trace = trace;
  }

  @Override
  public String toString() {
    return "ClientRequest{" +
        "endpoint=" + endpoint +
        ", trace=" + trace +
        '}';
  }
}
