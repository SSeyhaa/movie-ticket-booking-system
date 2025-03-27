package kh.dev.common_util.exception;

import jakarta.validation.ValidationException;
import java.time.LocalDateTime;
import java.util.stream.Collectors;

import kh.dev.common_util.dto.response.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
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
            e.getMessage(),
            request.getDescription(false),
            e.getClass().getSimpleName());
    return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(
      MethodArgumentNotValidException e, WebRequest request) {
    String errorMessages = extractValidationErrors(e);
    log.error("Validation error: {}", errorMessages, e);

    ErrorResponse errorResponse =
        buildErrorResponse(
            HttpStatus.BAD_REQUEST,
            errorMessages,
            request.getDescription(false),
            e.getClass().getSimpleName());
    return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
  }

  private String extractValidationErrors(MethodArgumentNotValidException e) {
    return e.getBindingResult().getAllErrors().stream()
        .map(error -> ((FieldError) error).getField() + ": " + error.getDefaultMessage())
        .collect(Collectors.joining(", "));
  }

  private ErrorResponse buildErrorResponse(
      HttpStatus status, String message, String path, String exceptionType) {
    return ErrorResponse.builder()
        .status(status.value())
        .error(status.getReasonPhrase())
        .message(message)
        .path(path)
        .exceptionType(exceptionType)
        .timestamp(LocalDateTime.now())
        .build();
  }
}
