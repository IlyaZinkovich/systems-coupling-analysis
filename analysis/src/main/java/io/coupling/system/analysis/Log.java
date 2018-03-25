package io.coupling.system.analysis;

import static java.lang.String.format;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.function.Consumer;
import java.util.stream.Stream;

public class Log {

  private final Path path;
  private final LogRecordParser recordParser;

  Log(final Path path, final LogRecordParser recordParser) {
    this.path = path;
    this.recordParser = recordParser;
  }

  public void read(final Consumer<GraphObject> graphObjectConsumer) {
    try (final Stream<String> stream = Files.lines(path)) {
      stream.map(recordParser::parse).forEach(graphObjectConsumer);
    } catch (final IOException e) {
      throw new ReadLogFileException(format("Unable to read the file %s", path), e);
    }
  }

  private class ReadLogFileException extends RuntimeException {

    ReadLogFileException(final String message, final Throwable cause) {
      super(message, cause);
    }
  }
}
