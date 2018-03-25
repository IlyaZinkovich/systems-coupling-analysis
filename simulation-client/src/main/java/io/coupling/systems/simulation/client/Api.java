package io.coupling.systems.simulation.client;

import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class Api {

  private final JdbcTemplate jdbcTemplate;
  private final RestTemplate restTemplate;

  @Autowired
  public Api(final JdbcTemplate jdbcTemplate, final RestTemplate restTemplate) {
    this.jdbcTemplate = jdbcTemplate;
    this.restTemplate = restTemplate;
  }

  @GetMapping(path = "/")
  public String get() {
    final List<Map<String, Object>> data =
        jdbcTemplate.queryForList("SELECT `id`, `data` FROM `host_data_storage`");
    return data.toString();
  }
}
