package kh.dev.notification_service.exception;

import java.time.LocalDateTime;
import kh.dev.common_util.dto.response.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

  @ExceptionHandler(NotificationException.class)
  public ResponseEntity<ErrorResponse> handleNotificationException(
      NotificationException e, WebRequest request) {

    log.error("Notification Error: {}", e.getMessage(), e);

    ErrorResponse errorResponse =
        buildErrorResponse(
            HttpStatus.INTERNAL_SERVER_ERROR,
            "Notification Error",
            e.getMessage(),
            request.getDescription(false),
            e.getClass().getSimpleName());

    return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
  }

  private ErrorResponse buildErrorResponse(
      HttpStatus status, String error, String message, String path, String exceptionType) {
    return ErrorResponse.builder()
        .status(status.value())
        .error(error)
        .message(message)
        .path(path)
        .exceptionType(exceptionType)
        .timestamp(LocalDateTime.now())
        .build();
  }
}
