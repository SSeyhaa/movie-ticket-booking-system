package kh.dev.user_service.start_up;

import java.util.Set;
import kh.dev.common_util.constant.Role;
import kh.dev.user_service.config.KeycloakProperty;
import kh.dev.user_service.model.dto.request.UserRequest;
import kh.dev.user_service.service.UserService;
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
        .roles(Set.of(Role.SUPER_ADMIN))
        .isActive(true)
        .build();
  }
}
