package kh.dev.common_util.config;

import java.util.Arrays;
import kh.dev.common_util.annotation.RoleRequired;
import kh.dev.common_util.constant.Role;
import kh.dev.common_util.exception.AccessDeniedException;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

/**
 * This class acts as an interceptor to enforce role-based access control using a custom annotation.
 * It leverages Spring AOP to intercept methods annotated with {@link RoleRequired} and validates
 * whether the currently authenticated user has the necessary roles to execute the method.
 *
 * <p>The roles are checked against the user's granted authorities, and access is denied if the
 * required roles are not present.
 *
 * <p>Example usage:
 *
 * <pre>{@code
 * @RoleRequired(required = {SystemRole.ADMIN, SystemRole.MANAGER})
 * public void someProtectedMethod() {
 *     // Method logic here
 * }
 * }</pre>
 *
 * @see RoleRequired
 * @see Role
 */
@Deprecated
@Component
@Aspect
public class AuthorizationInterceptorAOP {

  private static final String ROLE_PREFIX = "ROLE_";

  /**
   * Intercepts methods annotated with {@link RoleRequired} and validates the user's roles.
   *
   * <p>This method retrieves the currently authenticated user and checks whether they possess any
   * of the roles specified in the {@link RoleRequired#required()} annotation. If the user is not
   * authenticated or does not have the necessary roles, an {@link AccessDeniedException} is thrown.
   *
   * @param roleRequired the {@link RoleRequired} annotation applied to the method, which specifies
   *     the roles required to execute the method.
   * @throws AccessDeniedException if the user is not authenticated or does not have the required
   *     roles.
   */
  @Before("@annotation(roleRequired)")
  public void authorize(RoleRequired roleRequired) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

    if (authentication == null || !authentication.isAuthenticated()) {
      throw new AccessDeniedException("User is not authenticated");
    }

    boolean hasRole = false;
    for (Role role : roleRequired.required()) {
      if (authentication
          .getAuthorities()
          .contains(new SimpleGrantedAuthority(ROLE_PREFIX.concat(role.toString())))) {
        hasRole = true;
        break;
      }
    }

    if (!hasRole) {
      throw new AccessDeniedException(
          "Access denied for the required roles: "
              + String.join(", ", Arrays.toString(roleRequired.required())));
    }
  }
}
