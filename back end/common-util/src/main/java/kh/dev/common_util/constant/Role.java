package kh.dev.common_util.constant;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum Role {
  SUPER_ADMIN,
  USER;

  public static String[] getRolesStr() {
    return Stream.of(Role.values()).map(Role::name).toArray(String[]::new);
  }

  public static Set<Role> getRolesEnum() {
    return Stream.of(Role.values()).collect(Collectors.toSet());
  }
}
