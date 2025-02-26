package kh.dev.user_service.exception;

import java.time.LocalDateTime;
import java.util.stream.Collectors;
import kh.dev.common_util.dto.response.ErrorResponse;
import kh.dev.common_util.exception.AccessDeniedException;
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
    log.warn("Access Denied: {}", e.getMessage(), e);

    ErrorResponse errorResponse =
        buildErrorResponse(
            HttpStatus.FORBIDDEN.value(),
            "Access Denied",
            e.getMessage(),
            request.getDescription(false),
            e.getClass().getSimpleName());
    return new ResponseEntity<>(errorResponse, HttpStatus.FORBIDDEN);
  }

  @ExceptionHandler({UserCreationException.class, UserAlreadyExistsException.class})
  public ResponseEntity<ErrorResponse> handleUserConflictExceptions(
      RuntimeException e, WebRequest request) {

    ErrorResponse errorResponse =
        buildErrorResponse(
            HttpStatus.CONFLICT.value(),
            HttpStatus.CONFLICT.getReasonPhrase(),
            e.getMessage(),
            request.getDescription(false),
            e.getClass().getSimpleName());
    return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
  }

  @ExceptionHandler(RoleAssignmentException.class)
  public ResponseEntity<ErrorResponse> handleRoleAssignmentException(
      RoleAssignmentException e, WebRequest request) {

    ErrorResponse errorResponse =
        buildErrorResponse(
            HttpStatus.INTERNAL_SERVER_ERROR.value(),
            HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),
            e.getMessage(),
            request.getDescription(false),
            e.getClass().getSimpleName());
    return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
  }

  @ExceptionHandler(ResourceNotFoundException.class)
  public ResponseEntity<ErrorResponse> handleResourceNotFoundException(
      ResourceNotFoundException e, WebRequest request) {

    ErrorResponse errorResponse =
        buildErrorResponse(
            HttpStatus.NOT_FOUND.value(),
            HttpStatus.NOT_FOUND.getReasonPhrase(),
            e.getMessage(),
            request.getDescription(false),
            e.getClass().getSimpleName());
    return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(ValidationException.class)
  public ResponseEntity<ErrorResponse> handleValidationException(
      ValidationException e, WebRequest request) {

    ErrorResponse errorResponse =
        buildErrorResponse(
            HttpStatus.BAD_REQUEST.value(),
            HttpStatus.BAD_REQUEST.getReasonPhrase(),
            e.getMessage(),
            request.getDescription(false),
            e.getClass().getSimpleName());
    return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(
      MethodArgumentNotValidException e, WebRequest request) {
    String errorMessages = extractValidationErrors(e);
    log.error("Validation error: {}", errorMessages, e);

    ErrorResponse errorResponse =
        buildErrorResponse(
            HttpStatus.BAD_REQUEST.value(),
            HttpStatus.BAD_REQUEST.getReasonPhrase(),
            errorMessages,
            request.getDescription(false),
            e.getClass().getSimpleName());
    return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(UnauthorizedException.class)
  public ResponseEntity<ErrorResponse> handleUnauthorizedException(
      UnauthorizedException e, WebRequest request) {

    ErrorResponse errorResponse =
        buildErrorResponse(
            HttpStatus.UNAUTHORIZED.value(),
            HttpStatus.UNAUTHORIZED.getReasonPhrase(),
            e.getMessage(),
            request.getDescription(false),
            e.getClass().getSimpleName());
    return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
  }

  private ErrorResponse buildErrorResponse(
      int status, String error, String message, String path, String exceptionType) {
    return ErrorResponse.builder()
        .status(status)
        .error(error)
        .message(message)
        .path(path)
        .exceptionType(exceptionType)
        .timestamp(LocalDateTime.now())
        .build();
  }

  private String extractValidationErrors(MethodArgumentNotValidException e) {
    return e.getBindingResult().getAllErrors().stream()
        .map(error -> ((FieldError) error).getField() + ": " + error.getDefaultMessage())
        .collect(Collectors.joining(", "));
  }
}
