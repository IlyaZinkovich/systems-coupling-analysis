package io.coupling.system.analysis;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.function.Consumer;
import java.util.stream.Stream;

public class LogDirectory {

  private final String name;
  private final LogRecordParser logParser;

  LogDirectory(final String name, final LogRecordParser logParser) {
    this.name = name;
    this.logParser = logParser;
  }

  public void read(final Consumer<GraphObject> graphObjectConsumer) {
    try (final Stream<Path> logPaths = Files.walk(Paths.get(name))) {
      logPaths.filter(Files::isRegularFile).forEach(logPath -> {
        final Log log = new Log(logPath, logParser);
        log.read(graphObjectConsumer);
      });
    } catch (final IOException e) {
      throw new LogDirectoryReadException();
    }
  }

  private class LogDirectoryReadException extends RuntimeException {

  }
}
