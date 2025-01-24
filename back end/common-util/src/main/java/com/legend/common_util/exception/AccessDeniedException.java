package com.legend.common_util.exception;

public class AccessDeniedException extends RuntimeException {

  public AccessDeniedException(String message) {
    super(message);
  }

  public AccessDeniedException(String message, Throwable cause) {
    super(message, cause);
  }
}
