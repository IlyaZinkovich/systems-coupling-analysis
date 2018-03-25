package io.coupling.system.analysis;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.util.Iterator;

public class LogRecordParser {

  private final Gson jsonParser;

  LogRecordParser(final Gson jsonParser) {
    this.jsonParser = jsonParser;
  }

  public GraphObject parse(final String logRecord) {
    try {
      final JsonObject logJsonObject = jsonParser.fromJson(logRecord, JsonObject.class);
      final String classProperty = logJsonObject.get("class").getAsString();
      final String service = logJsonObject.get("service").getAsString();
      final Trace trace = parseTrace(logJsonObject, service);
      GraphObject graphObject;
      if ("i.c.s.plugin.RequestLoggingInterceptor".equals(classProperty)) {
        graphObject = parseClientRequest(logJsonObject, service, trace);
      } else if ("n.t.d.l.l.SLF4JQueryLoggingListener".equals(classProperty)) {
        graphObject = parseDatabaseInteraction(logJsonObject, trace);
      } else {
        graphObject = new NilGraphObject();
      }
      return graphObject;
    } catch (RuntimeException e) {
      return new NilGraphObject();
    }
  }

  private Trace parseTrace(final JsonObject logJsonObject, final String service) {
    final String traceId = logJsonObject.get("trace").getAsString();
    final String spanId = logJsonObject.get("span").getAsString();
    return new Trace(service, traceId, spanId);
  }

  private GraphObject parseClientRequest(final JsonObject logJsonObject, final String service,
      final Trace trace) {
    final String rest = logJsonObject.get("rest").getAsString();
    final JsonObject restObject = jsonParser.fromJson(rest, JsonObject.class);
    final String method = restObject.get("method").getAsString();
    final String path = restObject.get("path").getAsString();
    final Endpoint endpoint = new Endpoint(service, method, path);
    return new ClientRequest(endpoint, trace);
  }

  private GraphObject parseDatabaseInteraction(final JsonObject logJsonObject, final Trace trace) {
    GraphObject graphObject;
    final String rest = logJsonObject.get("rest").getAsString();
    final JsonObject restJsonObject = jsonParser.fromJson(rest, JsonObject.class);
    final JsonArray queries = restJsonObject.getAsJsonArray("query");
    final Iterator<JsonElement> queriesIterator = queries.iterator();
    if (queriesIterator.hasNext()) {
      final String query = queriesIterator.next().getAsString();
      final DatabaseQuery databaseQuery = new DatabaseQuery(query);
      graphObject = new DatabaseInteraction(databaseQuery, trace);
    } else {
      graphObject = new NilGraphObject();
    }
    return graphObject;
  }
}
