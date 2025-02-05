package com.legend.notification_service.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@ConfigurationProperties(prefix = "app.keycloak")
@Configuration
@Getter
@Setter
public class KeycloakProperty {
  private String serverUrl;
  private String realm;
  private String clientId;
  private String clientSecret;
  private String tokenUrl;
  private String usernameSuperAdmin;
  private String passwordSuperAdmin;
  private String emailSuperAdmin;
  private String firstNameSuperAdmin;
  private String lastNameSuperAdmin;
}
