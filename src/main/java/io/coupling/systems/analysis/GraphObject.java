package io.coupling.systems.analysis;

import org.neo4j.driver.v1.Session;

interface GraphObject {

  void persist(final Session session);
}
