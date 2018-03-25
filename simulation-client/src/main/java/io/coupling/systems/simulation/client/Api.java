package io.coupling.systems.simulation.client;

import static java.lang.String.format;

import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class Api {

  private final JdbcTemplate jdbcTemplate;
  private final RestTemplate restTemplate;
  private final String hostServiceUrl;
  private final Gson gson;

  @Autowired
  public Api(final JdbcTemplate jdbcTemplate, final RestTemplate restTemplate,
      @Value("${services.host.url}") final String hostServiceUrl, final Gson gson) {
    this.jdbcTemplate = jdbcTemplate;
    this.restTemplate = restTemplate;
    this.hostServiceUrl = hostServiceUrl;
    this.gson = gson;
  }

  @GetMapping(path = "/data/{id}")
  public Map<String, Object> get(@PathVariable final Long id) {
    final String dataUrl = format("%s/data/{id}", hostServiceUrl);
    final String hostServiceDataJson = restTemplate.getForObject(dataUrl, String.class, id);
    Type type = new TypeToken<Map<String, Object>>() {
    }.getType();
    final Map<String, Object> hostServiceData = gson.fromJson(hostServiceDataJson, type);
    final Map<String, Object> databaseData =
        jdbcTemplate.queryForMap("SELECT `details` FROM `host_data_storage` WHERE `id`=?", id);
    return ImmutableMap.<String, Object>builder()
        .putAll(hostServiceData)
        .putAll(databaseData)
        .build();
  }
}
