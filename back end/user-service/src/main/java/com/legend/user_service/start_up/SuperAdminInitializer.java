package com.legend.user_service.start_up;

import com.legend.common_util.constant.SystemRole;
import com.legend.user_service.config.KeycloakProperty;
import com.legend.user_service.dto.request.UserRequest;
import com.legend.user_service.service.UserService;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class SuperAdminInitializer implements Task, Ordered {
  private final KeycloakProperty keycloakProperty;
  private final UserService userService;

  @Override
  public int getOrder() {
    return 1;
  }

  @Override
  public void run() {
    try {
      userService.create(buildSuperAdminProperty());
    } catch (Exception e) {
      log.info("----- [Super admin]: {}", e.getMessage());
    }
  }

  private UserRequest buildSuperAdminProperty() {
    return UserRequest.builder()
        .username(keycloakProperty.getUsernameSuperAdmin())
        .email(keycloakProperty.getEmailSuperAdmin())
        .firstName(keycloakProperty.getFirstNameSuperAdmin())
        .lastName(keycloakProperty.getLastNameSuperAdmin())
        .password(keycloakProperty.getPasswordSuperAdmin())
        .roles(Set.of(SystemRole.SUPER_ADMIN))
        .isActive(true)
        .build();
  }
}
