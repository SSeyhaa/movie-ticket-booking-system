package kh.dev.common_util.util;

import java.util.Optional;
import kh.dev.common_util.config.CustomJwtAuthenticationToken;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Slf4j
public class CurrentUserUtils {

  /**
   * Retrieves the current authenticated user as a {@link CustomJwtAuthenticationToken}.
   *
   * @return an Optional containing the current user, or an empty Optional if the user is not
   *     authenticated or the authentication is not of type CustomJwtAuthenticationToken.
   */
  public static Optional<CustomJwtAuthenticationToken> getCurrentUserOptional() {
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

  public static CustomJwtAuthenticationToken getCurrentUser() {
    return getCurrentUserOptional()
        .orElseThrow(
            () -> new IllegalStateException("No authenticated user found in the current context"));
  }
}
