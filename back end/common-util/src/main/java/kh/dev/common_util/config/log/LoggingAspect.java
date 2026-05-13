package kh.dev.common_util.config.log;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
public class LoggingAspect {

  private static final Set<String> HIDDEN_HEADERS =
      Set.of(HttpHeaders.COOKIE).stream()
          .map(String::toLowerCase)
          .collect(Collectors.toUnmodifiableSet());

  private static final Set<String> MASKING_HEADERS =
      Set.of(HttpHeaders.AUTHORIZATION).stream()
          .map(String::toLowerCase)
          .collect(Collectors.toUnmodifiableSet());

  private final ObjectMapper objectMapper;

  @Around("@annotation(loggable)")
  public Object logMethod(ProceedingJoinPoint joinPoint, Loggable loggable) throws Throwable {

    LocalDateTime startTime = LocalDateTime.now();
    long start = System.currentTimeMillis();

    if (loggable.logRequest()) {
      HttpServletRequest request = RequestUtils.getCurrentRequest();
      logRequest(joinPoint, request);
    }

    Object result = joinPoint.proceed();

    if (loggable.logResponse()) {
      HttpServletResponse response = RequestUtils.getCurrentResponse();
      long executionTime = System.currentTimeMillis() - start;

      logResponse(result, response, startTime, executionTime);
    }

    return result;
  }

  private void logRequest(ProceedingJoinPoint joinPoint, HttpServletRequest request) {

    String requestBody =
        Arrays.stream(joinPoint.getArgs())
            .filter(this::isLoggableObject)
            .map(this::objToJson)
            .collect(Collectors.joining(","));

    String builder =
        "\n"
            + "Incoming request >>\n"
            + "method= "
            + (request != null ? request.getMethod() : "N/A")
            + ", uri= "
            + (request != null ? request.getRequestURI() : "N/A")
            + "\n"
            + "headers= "
            + extractHeaders(request)
            + "\n"
            + "request body= "
            + requestBody
            + "\n";

    log.info(builder);
  }

  private void logResponse(
      Object result, HttpServletResponse response, LocalDateTime startTime, long executionTime) {

    String builder =
        "\n"
            + "Outgoing response <<\n"
            + "status= "
            + (response != null ? response.getStatus() : "N/A")
            + "\n"
            + "response body= "
            + objToJson(result)
            + "\n"
            + "start time= "
            + startTime
            + "\n"
            + "executionTime= "
            + executionTime
            + "ms"
            + "\n";

    log.info(builder);
  }

  private String extractHeaders(HttpServletRequest request) {

    if (request == null) {
      return "N/A";
    }

    StringBuilder headers = new StringBuilder();

    Enumeration<String> headerNames = request.getHeaderNames();

    boolean first = true;
    while (headerNames.hasMoreElements()) {

      String headerName = headerNames.nextElement();
      String normalizedHeaderName = headerName.toLowerCase();

      if (HIDDEN_HEADERS.contains(normalizedHeaderName)) {
        continue;
      }

      String headerValue = request.getHeader(headerName);
      if (MASKING_HEADERS.contains(normalizedHeaderName)) {
        headerValue = "***";
      }

      if (!first) {
        headers.append(", ");
      }

      headers.append(headerName).append("[").append(headerValue).append("]");

      first = false;
    }

    return headers.toString();
  }

  private boolean isLoggableObject(Object obj) {

    return !(obj instanceof ServletRequest)
        && !(obj instanceof ServletResponse)
        && !(obj instanceof MultipartFile);
  }

  private String objToJson(Object obj) {

    try {
      return objectMapper.writeValueAsString(obj);
    } catch (JsonProcessingException e) {
      return String.valueOf(obj);
    }
  }
}
