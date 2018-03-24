package io.coupling.systems.analysis;

import java.util.function.Consumer;
import org.neo4j.driver.v1.Session;

public class GraphObjectConsumer implements Consumer<GraphObject> {

  private final Session session;

  GraphObjectConsumer(final Session session) {
    this.session = session;
  }

  @Override
  public void accept(final GraphObject graphObject) {
    graphObject.persist(session);
  }
}
