package io.coupling.systems.plugin;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;

public class RequestLoggingInterceptor implements HandlerInterceptor {

  private static final Logger log = LoggerFactory.getLogger(RequestLoggingInterceptor.class);

  private final Gson gson;

  public RequestLoggingInterceptor(final Gson gson) {
    this.gson = gson;
  }

  @Override
  public boolean preHandle(final HttpServletRequest request, final HttpServletResponse response,
      final Object handler) {
    final JsonObject call = new JsonObject();
    call.addProperty("method", request.getMethod());
    call.addProperty("url", request.getRequestURI());
    final String logMessage = gson.toJson(call);
    log.trace(logMessage);
    return true;
  }
}
