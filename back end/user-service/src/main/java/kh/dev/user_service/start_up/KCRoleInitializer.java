package kh.dev.user_service.start_up;

import kh.dev.common_util.constant.LogMessage;
import kh.dev.common_util.constant.Role;
import kh.dev.user_service.exception.RoleAssignmentException;
import kh.dev.user_service.service.KeycloakService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.RolesResource;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class KCRoleInitializer implements Task, Ordered {
  private final RealmResource realmResource;
  private final KeycloakService keycloakService;

  @Override
  public int getOrder() {
    return 1;
  }

  @Override
  public void run() {
    try {
      RolesResource rolesResource = realmResource.roles();

      for (String role : Role.getRolesStr()) {
        if (!keycloakService.roleExists(rolesResource, role)) {
          keycloakService.createRoleKeycloakRealm(rolesResource, role);
        }
      }
    } catch (Exception e) {
      log.error("{} Failed to initialize roles to Keycloak realm", LogMessage.FIVE_DASH, e);
      throw new RoleAssignmentException("Failed to initialize roles to Keycloak realm", e);
    }
  }
}
