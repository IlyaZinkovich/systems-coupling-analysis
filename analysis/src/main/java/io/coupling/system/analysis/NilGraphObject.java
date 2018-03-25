package io.coupling.system.analysis;

import org.neo4j.driver.v1.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class NilGraphObject implements GraphObject {

  private static final Logger log = LoggerFactory.getLogger(NilGraphObject.class);

  @Override
  public String toString() {
    return "NilGraphObject{}";
  }

  @Override
  public void persist(final Session session) {
    log.info("Persisted NilGraphObject");
  }
}
