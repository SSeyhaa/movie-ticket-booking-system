package com.legend.user_service.constant;

import java.util.stream.Stream;

public enum Role {
  SUPER_ADMIN,
  USER;

  public static String[] getRoles() {
    return Stream.of(Role.values()).map(Role::name).toArray(String[]::new);
  }
}
