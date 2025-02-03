package com.legend.movie_service.exception;

import com.legend.common_util.dto.response.ErrorResponse;
import java.time.LocalDateTime;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
