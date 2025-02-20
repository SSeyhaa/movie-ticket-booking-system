package kh.dev.user_service.exception;

import java.time.LocalDateTime;
import kh.dev.common_util.dto.response.ErrorResponse;
import kh.dev.common_util.exception.AccessDeniedException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.web.csrf.CsrfException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

  @ExceptionHandler(CsrfException.class)
  public ResponseEntity<ErrorResponse> handleCsrfException(CsrfException e, WebRequest request) {
    log.warn("CSRF Validation Failed: {}", e.getMessage());
    ErrorResponse errorResponse =
        buildErrorResponse(
            HttpStatus.FORBIDDEN,
            "CSRF Validation Failed",
            e.getMessage(),
            request.getDescription(false),
            e.getClass().getSimpleName());
    return new ResponseEntity<>(errorResponse, HttpStatus.FORBIDDEN);
  }

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

  @ExceptionHandler({UserCreationException.class, UserAlreadyExistsException.class})
  public ResponseEntity<ErrorResponse> handleUserConflictExceptions(
      RuntimeException e, WebRequest request) {
    log.warn("Conflict: {}", e.getMessage());
    ErrorResponse errorResponse =
        buildErrorResponse(
            HttpStatus.CONFLICT,
            "Conflict",
            e.getMessage(),
            request.getDescription(false),
            e.getClass().getSimpleName());
    return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
  }

  @ExceptionHandler(ResourceNotFoundException.class)
  public ResponseEntity<ErrorResponse> handleResourceNotFoundException(
      ResourceNotFoundException e, WebRequest request) {
    log.warn("Resource Not Found: {}", e.getMessage());
    ErrorResponse errorResponse =
        buildErrorResponse(
            HttpStatus.NOT_FOUND,
            "Not Found",
            e.getMessage(),
            request.getDescription(false),
            e.getClass().getSimpleName());
    return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorResponse> handleGlobalException(Exception e, WebRequest request) {
    log.error("Unexpected error: {}", e.getMessage(), e);
    ErrorResponse errorResponse =
        buildErrorResponse(
            HttpStatus.INTERNAL_SERVER_ERROR,
            "Internal Server Error",
            "An unexpected error occurred. Please try again later.",
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
