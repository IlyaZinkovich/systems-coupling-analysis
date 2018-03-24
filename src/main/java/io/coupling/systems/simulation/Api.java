package io.coupling.systems.simulation;

import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Api {

  private final JdbcTemplate jdbcTemplate;

  @Autowired
  public Api(final JdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
  }

  @GetMapping("/")
  public String get() {
    jdbcTemplate.execute("INSERT INTO `information` (`data`) VALUES ('some data')");
    final List<Map<String, Object>> data =
        jdbcTemplate.queryForList("SELECT `id`, `data` FROM `information`");
    return data.toString();
  }
}
