package io.coupling.systems;

import org.neo4j.driver.v1.AuthTokens;
import org.neo4j.driver.v1.Driver;
import org.neo4j.driver.v1.GraphDatabase;

public class GraphLogsReader implements AutoCloseable {

  private final Driver driver;

  private GraphLogsReader(final String uri, final String user, final String password) {
    driver = GraphDatabase.driver(uri, AuthTokens.basic(user, password));
  }

  public static void main(String... args) {
    final String uri = "bolt://localhost:7687";
    final String user = "neo4j";
    final String pass = "root";
    try (final GraphLogsReader reader = new GraphLogsReader(uri, user, pass)) {
      System.out.println(reader);
    }
  }

  @Override
  public void close() {
    driver.close();
  }
}
