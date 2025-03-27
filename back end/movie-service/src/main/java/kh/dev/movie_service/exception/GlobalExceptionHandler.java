package kh.dev.movie_service.exception;

import java.time.LocalDateTime;
import java.util.stream.Collectors;
import kh.dev.common_util.dto.response.ErrorResponse;
import kh.dev.common_util.exception.ResourceAlreadyExistsException;
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

  @ExceptionHandler(ResourceNotFoundException.class)
  public ResponseEntity<ErrorResponse> handleResourceNotFoundException(
      ResourceNotFoundException e, WebRequest request) {

    ErrorResponse errorResponse =
        buildErrorResponse(
            HttpStatus.NOT_FOUND,
            e.getMessage(),
            request.getDescription(false),
            e.getClass().getSimpleName());

    return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<ErrorResponse> handleIllegalArgumentException(
      IllegalArgumentException e, WebRequest request) {
    log.error("IllegalArgumentException occurred: {}", e.getMessage(), e);

    ErrorResponse errorResponse =
        buildErrorResponse(
            HttpStatus.INTERNAL_SERVER_ERROR,
            e.getMessage(),
            request.getDescription(false),
            e.getClass().getSimpleName());

    return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
  }

  @ExceptionHandler(ResourceAlreadyExistsException.class)
  public ResponseEntity<ErrorResponse> handleResourceAlreadyExistsException(
      ResourceAlreadyExistsException e, WebRequest request) {

    ErrorResponse errorResponse =
        buildErrorResponse(
            HttpStatus.CONFLICT,
            e.getMessage(),
            request.getDescription(false),
            e.getClass().getSimpleName());
    return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
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
        .map(
            error -> {
              if (error instanceof FieldError fieldError) {
                return fieldError.getField() + ": " + fieldError.getDefaultMessage();
              } else {
                return error.getDefaultMessage();
              }
            })
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
