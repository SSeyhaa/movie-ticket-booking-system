package kh.dev.common_util.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;
import kh.dev.common_util.annotation.RoleRequired;
import kh.dev.common_util.constant.LogMessage;
import kh.dev.common_util.constant.Role;
import kh.dev.common_util.exception.AccessDeniedException;
import kh.dev.common_util.util.CurrentAuthenticatedUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
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
 *   <li>Retrieving the current authentication from the {@link CurrentAuthenticatedUser}.
 *   <li>Checking if the user is authenticated and has assigned roles.
 *   <li>Extracting the required roles from the {@link RoleRequired} annotation.
 *   <li>Comparing the user's roles against the required roles.
 *   <li>Allowing access if at least one required role matches the user's roles.
 *   <li>Throwing an {@link AccessDeniedException} if access is denied.
 * </ol>
 */
@Component
@Slf4j
public class AuthorizationInterceptor implements HandlerInterceptor {

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

    final Set<Role> userRoles = CurrentAuthenticatedUser.getRoles();
    validateUserRoles(userRoles);

    if (handler instanceof HandlerMethod handlerMethod) {

      RoleRequired roleRequired = handlerMethod.getMethodAnnotation(RoleRequired.class);
      if (roleRequired == null) {
        return true;
      }

      if (hasRequiredRole(userRoles, roleRequired)) {
        log.info("{} User has required roles. Access granted.", LogMessage.FIVE_DASH);
        return true;
      }

      String requiredRolesString = getRequiredRolesString(roleRequired);
      throw new AccessDeniedException(
          "Access denied for the required roles: " + requiredRolesString);
    }

    return false;
  }

  private void validateUserRoles(Set<Role> roles) {
    if (CollectionUtils.isEmpty(roles)) {
      throw new AccessDeniedException("User has no assigned roles");
    }
  }

  private boolean hasRequiredRole(Set<Role> userRoles, RoleRequired roleRequired) {
    return Arrays.stream(roleRequired.required()).anyMatch(userRoles::contains);
  }

  private String getRequiredRolesString(RoleRequired roleRequired) {
    return Arrays.stream(roleRequired.required()).map(Enum::name).collect(Collectors.joining(", "));
  }
}
