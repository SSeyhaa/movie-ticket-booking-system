package com.legend.movie_service.exception; // package com.piseth.online_course.exception;

import com.legend.common_util.dto.response.ErrorResponse;
import java.time.LocalDateTime;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(ResourceNotFoundException.class)
  public ResponseEntity<ErrorResponse> handleResourceNotFoundException(
      ResourceNotFoundException e, WebRequest request) {

    ErrorResponse errorResponse =
        buildErrorResponse(HttpStatus.NOT_FOUND, e.getMessage(), request.getDescription(false));

    return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
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
