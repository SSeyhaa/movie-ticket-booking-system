package com.legend.user_service.service;

import com.legend.user_service.config.KeycloakProperty;
import com.legend.user_service.constant.Role;
import com.legend.user_service.dto.request.UserRequest;
import com.legend.user_service.exception.RoleAssignmentException;
import com.legend.user_service.exception.UserAlreadyExistsException;
import com.legend.user_service.exception.UserCreationException;
import jakarta.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.admin.client.CreatedResponseUtil;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.RoleResource;
import org.keycloak.admin.client.resource.RolesResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class KeycloakService {

  private static final int HTTP_STATUS_CREATED = 201;

  private final KeycloakProperty keycloakProperty;
  private final Keycloak keycloak;

  public UserRepresentation createUser(UserRequest userRequest) {
    RealmResource realmResource = keycloak.realm(keycloakProperty.getRealm());
    UsersResource usersResource = realmResource.users();

    List<UserRepresentation> existingUsers =
        usersResource.searchByEmail(userRequest.getEmail(), true);
    if (existingUsers.isEmpty()) {

      UserRepresentation user = buildUserRepresentation(userRequest);
      try (Response response = usersResource.create(user)) {

        if (response.getStatus() != HTTP_STATUS_CREATED) {
          log.error("----- Failed to create user in Keycloak server");
          throw new UserCreationException("Failed to create user in Keycloak server");
        }

        String userId = CreatedResponseUtil.getCreatedId(response);
        UserResource userResource = usersResource.get(userId);
        assignRolesToUser(realmResource, userResource, userRequest.getRoles());

        log.info(
            "----- User with email {} created successfully in Keycloak server",
            userRequest.getEmail());
        return userResource.toRepresentation();
      }
    } else {
      String existingUserEmail = existingUsers.getFirst().getEmail();
      log.error("----- User with email {} already exists in Keycloak server", existingUserEmail);
      throw new UserAlreadyExistsException(
          "User with email " + existingUserEmail + " already exists in Keycloak server");
    }
  }

  private UserRepresentation buildUserRepresentation(UserRequest userRequest) {
    UserRepresentation user = new UserRepresentation();
    user.setUsername(userRequest.getUsername());
    user.setEnabled(true);
    user.setEmail(userRequest.getEmail());
    user.setEmailVerified(true); // implement later
    user.setFirstName(userRequest.getFirstName());
    user.setLastName(userRequest.getLastName());

    CredentialRepresentation credential = new CredentialRepresentation();
    credential.setTemporary(false);
    credential.setType(CredentialRepresentation.PASSWORD);
    credential.setValue(userRequest.getPassword());

    user.setCredentials(Collections.singletonList(credential));
    return user;
  }

  private void assignRolesToUser(
      RealmResource realmResource, UserResource userResource, Set<Role> roles) {
    RolesResource rolesResource = realmResource.roles();
    List<RoleRepresentation> rolesRepresentation = new ArrayList<>();

    try {
      roles.forEach(
          role -> {
            RoleResource roleResource = rolesResource.get(role.name());
            rolesRepresentation.add(roleResource.toRepresentation());
          });
      userResource.roles().realmLevel().add(rolesRepresentation);
    } catch (Exception e) {
      userResource.remove();

      log.error("----- Failed to assign roles to user");
      throw new RoleAssignmentException("Failed to assign roles to user", e);
    }
  }

  public void deleteUserById(String userId) {
    RealmResource realmResource = keycloak.realm(keycloakProperty.getRealm());
    UsersResource usersResource = realmResource.users();
    UserResource userResource = usersResource.get(userId);
    userResource.remove();
    log.info("----- User with id {} deleted successfully in Keycloak server", userId);
  }
}
