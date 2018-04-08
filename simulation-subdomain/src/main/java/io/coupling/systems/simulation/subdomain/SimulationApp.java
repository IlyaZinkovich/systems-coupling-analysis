package io.coupling.systems.simulation.subdomain;

import static com.wix.mysql.EmbeddedMysql.anEmbeddedMysql;
import static com.wix.mysql.ScriptResolver.classPathScript;
import static com.wix.mysql.config.Charset.UTF8;
import static com.wix.mysql.config.MysqldConfig.aMysqldConfig;
import static com.wix.mysql.distribution.Version.v5_7_latest;
import static java.lang.String.format;

import com.google.gson.Gson;
import com.wix.mysql.EmbeddedMysql;
import com.wix.mysql.config.MysqldConfig;
import io.coupling.systems.plugin.DataSourceProxy;
import io.coupling.systems.plugin.RequestLoggingInterceptor;
import io.coupling.systems.plugin.WebMvcConfig;
import javax.sql.DataSource;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;

@SpringBootApplication
public class SimulationApp {

  public static void main(final String[] args) {
    SpringApplication.run(SimulationApp.class, args);
  }

  @Bean
  DataSource dataSource(final EmbeddedMysql embeddedMysql) {
    final int mysqlPort = embeddedMysql.getConfig().getPort();
    return DataSourceBuilder.create()
        .driverClassName("com.mysql.cj.jdbc.Driver")
        .url(format("jdbc:mysql://localhost:%d/data?useSSL=false", mysqlPort))
        .username("user").password("pass")
        .build();
  }

  @Bean
  EmbeddedMysql embeddedMysql() {
    MysqldConfig config = aMysqldConfig(v5_7_latest).withCharset(UTF8)
        .withPort(3306).withUser("user", "pass")
        .build();
    return anEmbeddedMysql(config)
        .addSchema("data", classPathScript("schema.sql"))
        .start();
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
