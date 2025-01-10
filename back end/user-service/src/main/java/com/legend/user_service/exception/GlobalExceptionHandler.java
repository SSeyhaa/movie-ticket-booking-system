package com.legend.user_service.exception; // package com.piseth.online_course.exception;

import com.legend.user_service.dto.response.ErrorResponse;
import java.time.LocalDateTime;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(AccessDeniedException.class)
  public ResponseEntity<ErrorResponse> handleAccessDeniedException(
      AccessDeniedException e, WebRequest request) {

    ErrorResponse errorResponse =
        buildErrorResponse(HttpStatus.FORBIDDEN, e.getMessage(), request.getDescription(false));

    return new ResponseEntity<>(errorResponse, HttpStatus.FORBIDDEN);
  }

  @ExceptionHandler(ResourceNotFoundException.class)
  public ResponseEntity<ErrorResponse> handleResourceNotFoundException(
      ResourceNotFoundException e, WebRequest request) {

    ErrorResponse errorResponse =
        buildErrorResponse(HttpStatus.NOT_FOUND, e.getMessage(), request.getDescription(false));

    return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(UserCreationException.class)
  public ResponseEntity<ErrorResponse> handleUserCreationException(
      UserCreationException e, WebRequest request) {

    ErrorResponse errorResponse =
        buildErrorResponse(HttpStatus.CONFLICT, e.getMessage(), request.getDescription(false));

    return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
  }

  @ExceptionHandler(UserAlreadyExistsException.class)
  public ResponseEntity<ErrorResponse> handleUserCreationException(
      UserAlreadyExistsException e, WebRequest request) {

    ErrorResponse errorResponse =
        buildErrorResponse(HttpStatus.CONFLICT, e.getMessage(), request.getDescription(false));

    return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
  }

  @ExceptionHandler(RoleAssignmentException.class)
  public ResponseEntity<ErrorResponse> handleRoleAssignmentException(
      RoleAssignmentException e, WebRequest request) {

    ErrorResponse errorResponse =
        buildErrorResponse(HttpStatus.BAD_REQUEST, e.getMessage(), request.getDescription(false));

    return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
  }

  private ErrorResponse buildErrorResponse(HttpStatus status, String message, String path) {

    return ErrorResponse.builder()
        .status(status.value())
        .error(status.getReasonPhrase())
        .message(message)
        .path(path)
        .timestamp(LocalDateTime.now())
        .build();
  }
}
