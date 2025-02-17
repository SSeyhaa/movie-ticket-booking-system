package com.legend.common_util.config;

import com.legend.common_util.util.CurrentUserUtils;
import java.util.Optional;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;

public class AuditorAwareImpl implements AuditorAware<String> {

  @Override
  public Optional<String> getCurrentAuditor() {

    return CurrentUserUtils.getCurrentUserOptional()
        .filter(Authentication::isAuthenticated)
        .map(CustomJwtAuthenticationToken::getFullName)
        .filter(name -> !name.isEmpty())
        .or(() -> Optional.of("system"));
  }
}
