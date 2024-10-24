package com.legend.user_service.start_up;

import com.legend.user_service.config.KeycloakProperty;
import com.legend.user_service.constant.Role;
import com.legend.user_service.dto.request.UserRequest;
import com.legend.user_service.service.UserService;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class SuperAdminInitializer {
  private final KeycloakProperty keycloakProperty;
  private final UserService userService;

  @EventListener(ApplicationReadyEvent.class)
  public void initialize() {
    try {
      userService.create(buildSuperAdminProperty());
    } catch (Exception e) {
      //
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
