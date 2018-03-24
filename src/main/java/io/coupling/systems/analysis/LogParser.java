package io.coupling.systems.analysis;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class LogParser {

  private final Gson jsonParser;

  LogParser(final Gson jsonParser) {
    this.jsonParser = jsonParser;
  }

  public GraphObject parse(final String log) {
    try {
      final JsonObject logJsonObject = jsonParser.fromJson(log, JsonObject.class);
      final String classProperty = logJsonObject.get("class").getAsString();
      final String service = logJsonObject.get("service").getAsString();
      final String traceId = logJsonObject.get("trace").getAsString();
      final String spanId = logJsonObject.get("span").getAsString();
      final Trace trace = new Trace(service, traceId, spanId);
      GraphObject graphObject;
      if ("i.c.s.s.RequestLoggingInterceptor".equals(classProperty)) {
        final String rest = logJsonObject.get("rest").getAsString();
        final Endpoint endpoint = jsonParser.fromJson(rest, Endpoint.class);
        graphObject = new ClientRequest(endpoint, trace);
      } else if ("n.t.d.l.l.SLF4JQueryLoggingListener".equals(classProperty)) {
        final String rest = logJsonObject.get("rest").getAsString();
        final JsonObject restJsonObject = jsonParser.fromJson(rest, JsonObject.class);
        final String query = restJsonObject.getAsJsonArray("query").get(0).getAsString();
        final DatabaseQuery databaseQuery = new DatabaseQuery(query);
        graphObject = new DatabaseInteraction(databaseQuery, trace);
      } else {
        graphObject = new NilGraphObject();
      }
      return graphObject;
    } catch (RuntimeException e) {
      return new NilGraphObject();
    }
  }
}
