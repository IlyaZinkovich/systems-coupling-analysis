package io.coupling.systems.analysis;

import static java.lang.String.format;

import com.google.gson.Gson;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;
import org.neo4j.driver.v1.AuthTokens;
import org.neo4j.driver.v1.Driver;
import org.neo4j.driver.v1.GraphDatabase;
import org.neo4j.driver.v1.Session;

public class GraphLogsReader {

  public static void main(final String... args) {
    final String uri = "bolt://localhost:7687";
    final String user = "neo4j";
    final String pass = "root";
    final Gson jsonParser = new Gson();
    final LogParser logParser = new LogParser(jsonParser);
    try (Driver driver = GraphDatabase.driver(uri, AuthTokens.basic(user, pass))) {
      final String filePath = "build/simulation.json";
      try (Stream<String> stream = Files.lines(Paths.get(filePath))) {
        try (final Session session = driver.session()) {
          stream.map(logParser::parse)
              .forEach(graphObject -> persistGraphObject(session, graphObject));
        }
      } catch (IOException e) {
        throw new RuntimeException(format("Unable to read the file %s", filePath));
      }
    }
  }

  private static void persistGraphObject(final Session session, final GraphObject graphObject) {
    graphObject.persist(session);
  }
}
