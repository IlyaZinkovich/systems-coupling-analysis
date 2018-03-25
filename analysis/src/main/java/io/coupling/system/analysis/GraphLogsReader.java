package io.coupling.system.analysis;

import com.google.gson.Gson;
import java.util.function.Consumer;
import org.neo4j.driver.v1.AuthTokens;
import org.neo4j.driver.v1.Driver;
import org.neo4j.driver.v1.GraphDatabase;
import org.neo4j.driver.v1.Session;

public class GraphLogsReader {

  public static void main(final String... args) {
    final String uri = "bolt://localhost:7687";
    final String user = "neo4j";
    final String pass = "root";
    final String logDirectoryName = "build";
    final Gson jsonParser = new Gson();
    final LogRecordParser logParser = new LogRecordParser(jsonParser);
    final LogDirectory logDirectory = new LogDirectory(logDirectoryName, logParser);
    try (final Driver driver = GraphDatabase.driver(uri, AuthTokens.basic(user, pass))) {
      try (final Session session = driver.session()) {
        final Consumer<GraphObject> graphObjectConsumer = new GraphObjectConsumer(session);
        logDirectory.read(graphObjectConsumer);
      }
    }
  }
}
