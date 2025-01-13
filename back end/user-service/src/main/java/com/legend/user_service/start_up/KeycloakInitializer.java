package com.legend.user_service.start_up;

import com.legend.common_util.constant.SystemRole;
import com.legend.user_service.config.KeycloakProperty;
import com.legend.user_service.constant.ProfileConstant;
import com.legend.user_service.exception.RoleAssignmentException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.RolesResource;
import org.keycloak.representations.idm.RoleRepresentation;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;

@Profile(ProfileConstant.NOT_TEST)
@Configuration
@RequiredArgsConstructor
@Slf4j
public class KeycloakInitializer {
  private final KeycloakProperty keycloakProperty;
  private final Keycloak keycloak;

  @EventListener(ApplicationReadyEvent.class)
  @Order(1)
  public void initializeRolesToKeycloakRealm() {
    try {
      RealmResource realmResource = keycloak.realm(keycloakProperty.getRealm());
      RolesResource rolesResource = realmResource.roles();

      for (String role : SystemRole.getRoles()) {
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
