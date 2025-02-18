package com.legend.common_util.config;

import com.legend.common_util.util.CurrentUserUtils;
import java.util.Optional;
import org.springframework.data.domain.AuditorAware;
import org.springframework.lang.NonNull;
import org.springframework.security.core.Authentication;
import org.springframework.util.StringUtils;

public class AuditorAwareImpl implements AuditorAware<String> {

  @Override
  @NonNull
  public Optional<String> getCurrentAuditor() {

    return CurrentUserUtils.getCurrentUserOptional()
        .filter(Authentication::isAuthenticated)
        .map(CustomJwtAuthenticationToken::getFullName)
        .filter(StringUtils::hasText)
        .or(() -> Optional.of("system"));
  }
}
