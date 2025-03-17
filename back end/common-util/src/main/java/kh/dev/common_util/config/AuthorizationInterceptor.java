package kh.dev.common_util.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;
import kh.dev.common_util.annotation.RoleRequired;
import kh.dev.common_util.exception.AccessDeniedException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * Interceptor to enforce role-based authorization for HTTP requests.
 *
 * <p>This interceptor checks if a user is authenticated and has the required roles specified by the
 * {@link RoleRequired} annotation on the handler method. If the user lacks the required roles, an
 * {@link AccessDeniedException} is thrown.
 *
 * <p>This interceptor works by:
 *
 * <ol>
 *   <li>Retrieving the current authentication from the {@link SecurityContextHolder}.
 *   <li>Checking if the user is authenticated and has assigned roles.
 *   <li>Extracting the required roles from the {@link RoleRequired} annotation.
 *   <li>Comparing the user's roles against the required roles.
 *   <li>Allowing access if at least one required role matches the user's roles.
 *   <li>Logging unauthorized access attempts and throwing an {@link AccessDeniedException} if
 *       access is denied.
 * </ol>
 */
@Component
@Slf4j
public class AuthorizationInterceptor implements HandlerInterceptor {

  private static final String ROLE_PREFIX = "ROLE_";

  /**
   * Intercepts incoming HTTP requests to perform authorization checks.
   *
   * @param request The {@link HttpServletRequest} containing client request data.
   * @param response The {@link HttpServletResponse} for sending responses to the client.
   * @param handler The handler (controller method) that is being accessed.
   * @return {@code true} if the request is authorized; {@code false} otherwise.
   * @throws AccessDeniedException if the user is not authenticated or lacks required roles.
   */
  @Override
  public boolean preHandle(
      @NonNull HttpServletRequest request,
      @NonNull HttpServletResponse response,
      @NonNull Object handler) {

    Authentication authentication = getAuthenticatedUser();
    validateUserRoles(authentication);

    if (handler instanceof HandlerMethod handlerMethod) {

      RoleRequired roleRequired = handlerMethod.getMethodAnnotation(RoleRequired.class);
      if (roleRequired == null) {
        return true;
      }

      if (hasRequiredRole(authentication, roleRequired)) {
        return true;
      }

      String requiredRolesString = getRequiredRolesString(roleRequired);
      logUnauthorizedAccess(authentication, requiredRolesString);
      throw new AccessDeniedException(
          "Access denied for the required roles: " + requiredRolesString);
    }

    return false;
  }

  private Authentication getAuthenticatedUser() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication == null || !authentication.isAuthenticated()) {
      throw new AccessDeniedException("User is not authenticated");
    }
    return authentication;
  }

  private void validateUserRoles(Authentication authentication) {
    if (authentication.getAuthorities() == null || authentication.getAuthorities().isEmpty()) {
      throw new AccessDeniedException("User has no assigned roles");
    }
  }

  private boolean hasRequiredRole(Authentication authentication, RoleRequired roleRequired) {
    Set<String> userRoles =
        authentication.getAuthorities().stream()
            .map(GrantedAuthority::getAuthority)
            .collect(Collectors.toSet());

    return Arrays.stream(roleRequired.required())
        .map(role -> ROLE_PREFIX + role.name())
        .anyMatch(userRoles::contains);
  }

  private void logUnauthorizedAccess(Authentication authentication, String requiredRolesString) {
    log.warn(
        "Unauthorized access attempt: user={}, requiredRoles={}",
        authentication.getName(),
        requiredRolesString);
  }

  private String getRequiredRolesString(RoleRequired roleRequired) {
    return Arrays.stream(roleRequired.required()).map(Enum::name).collect(Collectors.joining(", "));
  }
}
