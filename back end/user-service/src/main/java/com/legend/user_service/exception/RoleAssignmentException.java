package com.legend.user_service.exception;

public class RoleAssignmentException extends RuntimeException {

  public RoleAssignmentException(String message) {
    super(message);
  }

  public RoleAssignmentException(String message, Throwable cause) {
    super(message, cause);
  }
}
