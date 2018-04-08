package io.coupling.systems.simulation.subdomain;

import static java.sql.Statement.RETURN_GENERATED_KEYS;
import static java.util.stream.Collectors.toMap;

import com.google.common.collect.ImmutableMap;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Api {

  private final JdbcTemplate jdbcTemplate;

  @Autowired
  public Api(final JdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
  }

  @GetMapping("/data/{id}")
  public Map<String, Object> get(@PathVariable(name = "id") final Long id) {
    final String selectDataSql = "SELECT `id`, `data` FROM `subdomain_storage` WHERE `id`=?";
    return jdbcTemplate.queryForMap(selectDataSql, id)
        .entrySet().stream()
        .filter(entry -> !"DETAILS".equals(entry.getKey()))
        .collect(toMap(entry -> entry.getKey().toLowerCase(), Entry::getValue));
  }

  @PostMapping("/data")
  public Map<String, Number> store(@RequestBody final Map<String, String> body) {
    final String data = body.get("data");
    final String details = body.get("details");
    final KeyHolder keyHolder = new GeneratedKeyHolder();
    jdbcTemplate.update(connection -> prepareUpdate(data, details, connection), keyHolder);
    final Number generatedKey = Optional.ofNullable(keyHolder.getKey())
        .orElseThrow(StorageException::new);
    return ImmutableMap.of("id", generatedKey);
  }

  private PreparedStatement prepareUpdate(final String data, final String details,
      final Connection connection) throws SQLException {
    final String updateSql = "INSERT INTO `subdomain_storage` (`data`, `details`) VALUES (?, ?)";
    final PreparedStatement ps = prepareIdGeneratingStatement(connection, updateSql);
    ps.setString(1, data);
    ps.setString(2, details);
    return ps;
  }

  private PreparedStatement prepareIdGeneratingStatement(final Connection connection,
      final String sql) throws SQLException {
    return connection.prepareStatement(sql, RETURN_GENERATED_KEYS);
  }

  private class StorageException extends RuntimeException {

  }
}
