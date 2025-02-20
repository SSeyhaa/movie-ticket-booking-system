package kh.dev.user_service.config;

import lombok.RequiredArgsConstructor;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class KeycloakConfig {
  private final KeycloakProperty keycloakProperty;

  /**
   * Configures a Keycloak client for administrative purposes.
   *
   * <p>This method creates and returns a Keycloak client instance configured with the necessary
   * credentials and connection settings to interact with the Keycloak server. The Client ID needs
   * at least roles "manage-users, view-clients, view-realm, view-users" for
   * "realm-management".
   *
   * @return a configured Keycloak client instance
   */
  @Bean
  public Keycloak configKeycloak() {
    return KeycloakBuilder.builder()
        .serverUrl(keycloakProperty.getServerUrl())
        .realm(keycloakProperty.getRealm())
        .grantType(OAuth2Constants.CLIENT_CREDENTIALS)
        .clientId(keycloakProperty.getClientId())
        .clientSecret(keycloakProperty.getClientSecret())
        .build();
  }
}
