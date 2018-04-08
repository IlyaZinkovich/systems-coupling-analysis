package io.coupling.systems.simulation.core;

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
    final Map<String, Object> hostServiceData = getHostServiceData(id);
    final Map<String, Object> databaseData = getDatabaseData(id);
    return ImmutableMap.<String, Object>builder()
        .putAll(hostServiceData)
        .putAll(databaseData)
        .build();
  }

  private Map<String, Object> getDatabaseData(final Long id) {
    return jdbcTemplate.queryForMap("SELECT `details` FROM `subdomain_storage` WHERE `id`=?", id);
  }

  private Map<String, Object> getHostServiceData(@PathVariable final Long id) {
    final String dataUrl = format("%s/data/{id}", hostServiceUrl);
    final String hostServiceDataJson = restTemplate.getForObject(dataUrl, String.class, id);
    Type type = new TypeToken<Map<String, Object>>() {
    }.getType();
    return gson.fromJson(hostServiceDataJson, type);
  }
}
