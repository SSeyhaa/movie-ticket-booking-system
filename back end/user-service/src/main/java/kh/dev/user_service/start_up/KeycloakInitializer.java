package kh.dev.user_service.start_up;

import kh.dev.common_util.constant.Role;
import kh.dev.user_service.config.KeycloakProperty;
import kh.dev.user_service.exception.RoleAssignmentException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.RolesResource;
import org.keycloak.representations.idm.RoleRepresentation;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class KeycloakInitializer implements Task, Ordered {
  private final KeycloakProperty keycloakProperty;
  private final Keycloak keycloak;

  @Override
  public int getOrder() {
    return 0;
  }

  @Override
  public void run() {
    try {
      RealmResource realmResource = keycloak.realm(keycloakProperty.getRealm());
      RolesResource rolesResource = realmResource.roles();

      for (String role : Role.getRoles()) {
        if (!roleExists(rolesResource, role)) {
          createRoleKeycloakRealm(rolesResource, role);
        }
      }
    } catch (Exception e) {
      log.error("----- Failed to initialize roles to Keycloak realm", e);
      throw new RoleAssignmentException("Failed to initialize roles to Keycloak realm", e);
    }
  }

  private boolean roleExists(RolesResource rolesResource, String role) {
    try {
      rolesResource.get(role).toRepresentation();
      log.info("----- Role {} already exists in Keycloak server", role);
      return true;
    } catch (Exception e) {
      log.info("----- Role {} does not exist in Keycloak server", role);
      return false;
    }
  }

  private void createRoleKeycloakRealm(RolesResource rolesResource, String role) {
    RoleRepresentation roleRepresentation = new RoleRepresentation();
    roleRepresentation.setName(role);
    rolesResource.create(roleRepresentation);
    log.info("----- Role {} created successfully in Keycloak server", role);
  }
}
