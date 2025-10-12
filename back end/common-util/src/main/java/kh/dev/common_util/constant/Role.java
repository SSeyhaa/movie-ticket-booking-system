package kh.dev.common_util.constant;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import kh.dev.common_util.util.CurrentAuthenticatedUser;

public enum Role {
  SUPER_ADMIN,
  USER;

  public static String[] getRolesStr() {
    return Stream.of(Role.values()).map(Role::name).toArray(String[]::new);
  }

  public static Set<Role> getRolesEnum() {
    return Stream.of(Role.values()).collect(Collectors.toSet());
  }

  public static boolean isSuperAdmin() {
    return CurrentAuthenticatedUser.getRoles().contains(Role.SUPER_ADMIN);
  }
}
