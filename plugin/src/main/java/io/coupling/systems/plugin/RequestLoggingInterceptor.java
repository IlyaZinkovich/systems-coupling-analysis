package io.coupling.systems.plugin;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.method.HandlerMethod;
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
    final HandlerMethod handlerMethod = (HandlerMethod) handler;
    final String method = request.getMethod();
    final String path = getPath(handlerMethod, method);
    final JsonObject call = new JsonObject();
    call.addProperty("method", method);
    call.addProperty("path", path);
    final String logMessage = gson.toJson(call);
    log.trace(logMessage);
    return true;
  }

  private String getPath(final HandlerMethod handlerMethod, final String method) {
    String path;
    if ("GET".equals(method)) {
      final GetMapping getMapping = handlerMethod.getMethodAnnotation(GetMapping.class);
      path = getMapping.path()[0];
    } else if ("POST".equals(method)) {
      final PostMapping postMapping = handlerMethod.getMethodAnnotation(PostMapping.class);
      path = postMapping.path()[0];
    } else {
      throw new CannotDerivePathException();
    }
    return path;
  }

  private class CannotDerivePathException extends RuntimeException {

  }
}
