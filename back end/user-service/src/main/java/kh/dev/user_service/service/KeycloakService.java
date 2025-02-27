package kh.dev.user_service.service;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.ws.rs.core.Form;
import jakarta.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import kh.dev.common_util.constant.SystemRole;
import kh.dev.user_service.client.KeycloakClient;
import kh.dev.user_service.config.KeycloakProperty;
import kh.dev.user_service.exception.ResourceNotFoundException;
import kh.dev.user_service.exception.RoleAssignmentException;
import kh.dev.user_service.exception.UnauthorizedException;
import kh.dev.user_service.exception.UserAlreadyExistsException;
import kh.dev.user_service.exception.UserCreationException;
import kh.dev.user_service.exception.ValidationException;
import kh.dev.user_service.model.dto.request.CredentialRequest;
import kh.dev.user_service.model.dto.request.PasswordChangeRequest;
import kh.dev.user_service.model.dto.request.UserRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.CreatedResponseUtil;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.RoleResource;
import org.keycloak.admin.client.resource.RolesResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.AccessTokenResponse;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;
import org.springframework.web.util.WebUtils;

@Service
@RequiredArgsConstructor
@Slf4j
public class KeycloakService {

  public static final String REFRESH_TOKEN = "refresh_token";
  private static final String USER_SERVICE = "/user-service";
  private static final int HTTP_STATUS_CREATED = 201;

  private final KeycloakProperty keycloakProperty;
  private final KeycloakClient keycloakClient;
  private final RealmResource realmResource;

  public static ResponseCookie createRefreshTokenCookie(String refreshToken, long expiresIn) {
    return ResponseCookie.from(REFRESH_TOKEN, refreshToken)
        // .domain("localhost") // Restrict cookie to specific domain
        .path(USER_SERVICE) // Restrict cookie to refresh-token path
        .httpOnly(true) // Prevent access to cookie from JavaScript
        .secure(true) // Only sent over HTTPS
        .sameSite("Strict") // Prevent sending cookies in cross-origin requests, CSRF protection
        .maxAge(expiresIn)
        .build();
  }

  public static Cookie getRefreshTokenCookie(HttpServletRequest request) {
    return Optional.ofNullable(WebUtils.getCookie(request, KeycloakService.REFRESH_TOKEN))
        .orElseThrow(() -> new ResourceNotFoundException("Refresh token not found"));
  }

  public AccessTokenResponse getAccessToken(CredentialRequest credentialRequest) {

    try {
      Form form =
          buildTokenRequestForm(credentialRequest.getEmail(), credentialRequest.getPassword());

      return keycloakClient.getAccessToken(form.asMap());
    } catch (Exception e) {
      throw new UnauthorizedException("Invalid credentials", e);
    }
  }

  public AccessTokenResponse refreshAccessToken(String refreshToken) {
    try {
      Form form = buildRefreshTokenRequestForm(refreshToken);
      return keycloakClient.getRefreshToken(form.asMap());
    } catch (Exception e) {
      throw new UnauthorizedException("Invalid refresh token", e);
    }
  }

  public void logout(HttpServletRequest request, HttpServletResponse response) {

    Cookie refreshTokenCookie = KeycloakService.getRefreshTokenCookie(request);
    try {
      Form form = buildLogoutRequestForm(refreshTokenCookie.getValue());
      keycloakClient.logout(form.asMap());

      ResponseCookie responseCookie = createRefreshTokenCookie("", 0);
      response.addHeader(HttpHeaders.SET_COOKIE, responseCookie.toString());
    } catch (Exception e) {
      throw new UnauthorizedException("Invalid refresh token", e);
    }
  }

  private Form buildTokenRequestForm(String email, String password) {
    return new Form()
        .param(OAuth2Constants.CLIENT_ID, keycloakProperty.getClientId())
        .param(OAuth2Constants.CLIENT_SECRET, keycloakProperty.getClientSecret())
        .param(OAuth2Constants.GRANT_TYPE, OAuth2Constants.PASSWORD)
        .param(OAuth2Constants.USERNAME, email)
        .param(OAuth2Constants.PASSWORD, password);
  }

  private Form buildRefreshTokenRequestForm(String refreshToken) {
    return new Form()
        .param(OAuth2Constants.CLIENT_ID, keycloakProperty.getClientId())
        .param(OAuth2Constants.CLIENT_SECRET, keycloakProperty.getClientSecret())
        .param(OAuth2Constants.GRANT_TYPE, OAuth2Constants.REFRESH_TOKEN)
        .param(OAuth2Constants.REFRESH_TOKEN, refreshToken);
  }

  private Form buildLogoutRequestForm(String refreshToken) {
    return new Form()
        .param(OAuth2Constants.CLIENT_ID, keycloakProperty.getClientId())
        .param(OAuth2Constants.CLIENT_SECRET, keycloakProperty.getClientSecret())
        .param(OAuth2Constants.GRANT_TYPE, OAuth2Constants.LOGOUT_TOKEN)
        .param(OAuth2Constants.REFRESH_TOKEN, refreshToken);
  }

  public UserRepresentation createUser(UserRequest userRequest) {
    UsersResource usersResource = realmResource.users();

    List<UserRepresentation> existingUsers =
        usersResource.searchByEmail(userRequest.getEmail(), true);
    if (existingUsers.isEmpty()) {

      UserRepresentation user = buildUserRepresentation(userRequest);
      try (Response response = usersResource.create(user)) {

        if (response.getStatus() != HTTP_STATUS_CREATED) {
          log.error("----- Failed to create user in Keycloak server: {}", response.getStatusInfo());
          throw new UserCreationException(
              "Failed to create user in Keycloak server : " + response.getStatusInfo());
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

  public void changePassword(PasswordChangeRequest passwordChangeRequest) {

    boolean isPasswordValid =
        isPasswordValid(
            passwordChangeRequest.getEmail(), passwordChangeRequest.getCurrentPassword());
    if (!isPasswordValid) {
      throw new ValidationException("Invalid current password");
    }

    UsersResource usersResource = realmResource.users();
    UserResource userResource = usersResource.get(passwordChangeRequest.getKeycloakId());

    UserRepresentation existingUser = userResource.toRepresentation();
    existingUser.setCredentials(
        Collections.singletonList(buildUserCredential(passwordChangeRequest.getPassword())));
    userResource.update(existingUser);
  }

  private boolean isPasswordValid(String email, String password) {
    try {
      CredentialRequest credentialRequest = new CredentialRequest();
      credentialRequest.setEmail(email);
      credentialRequest.setPassword(password);

      this.getAccessToken(credentialRequest);
    } catch (UnauthorizedException e) {
      return false;
    }
    return true;
  }

  private UserRepresentation buildUserRepresentation(UserRequest userRequest) {
    UserRepresentation user = new UserRepresentation();
    user.setUsername(userRequest.getUsername());
    user.setEnabled(true);
    user.setEmail(userRequest.getEmail());
    user.setEmailVerified(true); // implement later
    user.setFirstName(userRequest.getFirstName());
    user.setLastName(userRequest.getLastName());

    CredentialRepresentation credential = buildUserCredential(userRequest.getPassword());

    user.setCredentials(Collections.singletonList(credential));
    return user;
  }

  private CredentialRepresentation buildUserCredential(String password) {
    CredentialRepresentation credential = new CredentialRepresentation();
    credential.setTemporary(false);
    credential.setType(CredentialRepresentation.PASSWORD);
    credential.setValue(password);
    return credential;
  }

  private void assignRolesToUser(
      RealmResource realmResource, UserResource userResource, Set<SystemRole> userRoles) {
    RolesResource rolesResource = realmResource.roles();
    List<RoleRepresentation> rolesRepresentation = new ArrayList<>();

    try {
      userRoles.forEach(
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
    UsersResource usersResource = realmResource.users();
    UserResource userResource = usersResource.get(userId);
    userResource.remove();
    log.info("----- User with id {} deleted successfully in Keycloak server", userId);
  }
}
