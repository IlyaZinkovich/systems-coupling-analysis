package io.coupling.systems.plugin;

import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

public class WebMvcConfig implements WebMvcConfigurer {

  private final RequestLoggingInterceptor interceptor;

  public WebMvcConfig(final RequestLoggingInterceptor interceptor) {
    this.interceptor = interceptor;
  }

  @Override
  public void addInterceptors(final InterceptorRegistry registry) {
    registry.addInterceptor(interceptor);
  }
}
