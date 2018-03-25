package io.coupling.systems.simulation.host;

import static org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType.H2;

import com.google.gson.Gson;
import io.coupling.systems.plugin.DataSourceProxy;
import io.coupling.systems.plugin.RequestLoggingInterceptor;
import io.coupling.systems.plugin.WebMvcConfig;
import javax.sql.DataSource;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.web.servlet.HandlerMapping;

@SpringBootApplication
public class SimulationApp {

  public static void main(final String[] args) {
    SpringApplication.run(SimulationApp.class, args);
  }

  @Bean
  DataSource dataSource() {
    return new EmbeddedDatabaseBuilder().setType(H2).build();
  }

  @Bean
  DataSourceProxy dataSourceProxy() {
    return new DataSourceProxy();
  }

  @Bean
  JdbcTemplate jdbcTemplate(final DataSource dataSource) {
    return new JdbcTemplate(dataSource);
  }

  @Bean
  WebMvcConfig webMvcConfig(final RequestLoggingInterceptor interceptor) {
    return new WebMvcConfig(interceptor);
  }

  @Bean
  Gson gson() {
    return new Gson();
  }

  @Bean
  RequestLoggingInterceptor requestLoggingInterceptor(final Gson gson) {
    return new RequestLoggingInterceptor(gson);
  }
}
