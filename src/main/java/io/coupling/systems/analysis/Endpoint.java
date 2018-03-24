package io.coupling.systems.analysis;

public class Endpoint {

  private final String method;
  private final String url;

  public Endpoint(final String method, final String url) {
    this.method = method;
    this.url = url;
  }

  @Override
  public String toString() {
    return "Endpoint{" +
        "method='" + method + '\'' +
        ", url='" + url + '\'' +
        '}';
  }
}
