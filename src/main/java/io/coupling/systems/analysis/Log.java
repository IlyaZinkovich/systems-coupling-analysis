package io.coupling.systems.analysis;

import static java.lang.String.format;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.function.Consumer;
import java.util.stream.Stream;

public class Log {

  private final String path;
  private final LogRecordParser recordParser;

  Log(final String path, final LogRecordParser recordParser) {
    this.path = path;
    this.recordParser = recordParser;
  }

  public void read(Consumer<GraphObject> graphObjectConsumer) {
    try (Stream<String> stream = Files.lines(Paths.get(path))) {
      stream.map(recordParser::parse).forEach(graphObjectConsumer);
    } catch (IOException e) {
      throw new ReadLogFileException(format("Unable to read the file %s", path), e);
    }
  }

  private class ReadLogFileException extends RuntimeException {

    ReadLogFileException(final String message, final Throwable cause) {
      super(message, cause);
    }
  }
}
