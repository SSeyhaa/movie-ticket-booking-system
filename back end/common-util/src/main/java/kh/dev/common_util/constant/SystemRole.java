package kh.dev.common_util.constant;

import java.util.stream.Stream;

public enum SystemRole {
  SUPER_ADMIN,
  USER;

  public static String[] getRoles() {
    return Stream.of(SystemRole.values()).map(SystemRole::name).toArray(String[]::new);
  }
}
