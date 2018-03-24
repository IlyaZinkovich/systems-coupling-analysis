package io.coupling.systems.analysis;

import com.google.common.collect.ImmutableMap;
import java.util.List;
import java.util.Map;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.util.TablesNamesFinder;

class DatabaseQuery {

  private final String query;

  DatabaseQuery(final String query) {
    this.query = query;
  }

  public Map<String, Object> toParameters() {
    return ImmutableMap.<String, Object>builder()
        .put("query", query)
        .put("table", tableNames().get(0))
        .build();
  }

  private List<String> tableNames() {
    try {
      final Statement statement = CCJSqlParserUtil.parse(query);
      final TablesNamesFinder tablesNamesFinder = new TablesNamesFinder();
      return tablesNamesFinder.getTableList(statement);
    } catch (JSQLParserException e) {
      throw new CannotExtractTableDataException(e);
    }
  }

  @Override
  public String toString() {
    return "DatabaseQuery{" +
        "query='" + query + '\'' +
        '}';
  }

  private class CannotExtractTableDataException extends RuntimeException {

    CannotExtractTableDataException(Throwable cause) {
      super(cause);
    }
  }
}
