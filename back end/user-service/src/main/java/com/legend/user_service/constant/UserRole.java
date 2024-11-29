package com.legend.user_service.constant;

import java.util.stream.Stream;

public enum UserRole {
  SUPER_ADMIN,
  USER;

  public static String[] getRoles() {
    return Stream.of(UserRole.values()).map(UserRole::name).toArray(String[]::new);
  }
}
