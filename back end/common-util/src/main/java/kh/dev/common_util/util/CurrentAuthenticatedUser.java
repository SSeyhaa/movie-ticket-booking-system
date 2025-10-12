package kh.dev.common_util.util;

import java.util.Optional;
import java.util.Set;
import kh.dev.common_util.config.CustomJwtAuthenticationToken;
import kh.dev.common_util.constant.LogMessage;
import kh.dev.common_util.constant.Role;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Slf4j
public class CurrentAuthenticatedUser {

  /**
   * Retrieves the current authenticated user as a {@link CustomJwtAuthenticationToken}.
   *
   * @return an Optional containing the current user, or an empty Optional if the user is not
   *     authenticated or the authentication is not of type CustomJwtAuthenticationToken.
   */
  public static Optional<CustomJwtAuthenticationToken> getUserOptional() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication instanceof CustomJwtAuthenticationToken currentUser) {
      return Optional.of(currentUser);
    } else {
      log.warn(
          "Authentication object is not of type CustomJwtAuthenticationToken. Actual type: {}",
          authentication == null ? "null" : authentication.getClass().getName());
    }

    return Optional.empty();
  }

  public static CustomJwtAuthenticationToken getUser() {
    return getUserOptional()
        .orElseThrow(
            () -> new IllegalStateException("No authenticated user found in the current context"));
  }

  public static String getEmail() {
    final String email = getUser().getEmail();
    log.info("{} get current user email: {}", LogMessage.FIVE_DASH, email);
    return email;
  }

  public static Set<Role> getRoles() {
    return getUser().getRoles();
  }
}
