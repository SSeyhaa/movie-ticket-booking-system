package kh.dev.common_util.exception;

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

  @ExceptionHandler(AccessDeniedException.class)
  public ResponseEntity<ErrorResponse> handleAccessDeniedException(
      AccessDeniedException e, WebRequest request) {
    log.warn("Access Denied: {}", e.getMessage());
    ErrorResponse errorResponse =
        buildErrorResponse(
            HttpStatus.FORBIDDEN,
            "Access Denied",
            e.getMessage(),
            request.getDescription(false),
            e.getClass().getSimpleName());
    return new ResponseEntity<>(errorResponse, HttpStatus.FORBIDDEN);
  }

  @ExceptionHandler(SchedulerException.class)
  public ResponseEntity<ErrorResponse> handleSchedulerException(
      SchedulerException e, WebRequest request) {

    ErrorResponse errorResponse =
        buildErrorResponse(
            HttpStatus.INTERNAL_SERVER_ERROR,
            HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),
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
