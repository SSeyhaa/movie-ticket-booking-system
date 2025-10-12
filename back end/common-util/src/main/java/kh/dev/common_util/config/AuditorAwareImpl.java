package kh.dev.common_util.config;

import java.util.Optional;
import kh.dev.common_util.util.CurrentAuthenticatedUser;
import org.springframework.data.domain.AuditorAware;
import org.springframework.lang.NonNull;
import org.springframework.security.core.Authentication;
import org.springframework.util.StringUtils;

public class AuditorAwareImpl implements AuditorAware<String> {

  @Override
  @NonNull
  public Optional<String> getCurrentAuditor() {

    return CurrentAuthenticatedUser.getUserOptional()
        .filter(Authentication::isAuthenticated)
        .map(CustomJwtAuthenticationToken::getFullName)
        .filter(StringUtils::hasText)
        .or(() -> Optional.of("system"));
  }
}
