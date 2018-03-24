package io.coupling.systems.analysis;

import org.neo4j.driver.v1.Session;

class NilGraphObject implements GraphObject {

  @Override
  public String toString() {
    return "NilGraphObject{}";
  }

  @Override
  public void persist(final Session session) {
    throw new NilGraphObjectPersistException();
  }

  private class NilGraphObjectPersistException extends RuntimeException {

  }
}
