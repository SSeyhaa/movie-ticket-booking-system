package kh.dev.user_service.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import java.time.ZonedDateTime;
import kh.dev.common_util.dto.response.Response;
import kh.dev.user_service.model.dto.request.CredentialRequest;
import kh.dev.user_service.service.KeycloakService;
import lombok.RequiredArgsConstructor;
import org.keycloak.representations.AccessTokenResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/public/v1/auth")
@RequiredArgsConstructor
public class AuthController {

  private final KeycloakService keycloakService;

  @PostMapping("/access-token")
  public ResponseEntity<AccessTokenResponse> getAccessToken(
      @RequestBody @Valid CredentialRequest credentialRequest, HttpServletResponse response) {

    AccessTokenResponse accessToken = keycloakService.getAccessToken(credentialRequest);
    ResponseCookie refreshTokenCookie =
        KeycloakService.createRefreshTokenCookie(
            accessToken.getRefreshToken(), accessToken.getRefreshExpiresIn());

    response.addHeader(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString());
    return ResponseEntity.ok(accessToken);
  }

  @PostMapping("/refresh-token")
  public ResponseEntity<AccessTokenResponse> refreshAccessToken(HttpServletRequest request) {

    Cookie refreshTokenCookie = KeycloakService.getRefreshTokenCookie(request);
    AccessTokenResponse accessToken =
        keycloakService.refreshAccessToken(refreshTokenCookie.getValue());
    return ResponseEntity.ok(accessToken);
  }

  @PostMapping("/logout")
  public ResponseEntity<Response> logout(HttpServletRequest request, HttpServletResponse response) {

    keycloakService.logout(request, response);
    return ResponseEntity.ok(
        Response.builder()
            .code(HttpStatus.OK.value())
            .status("success")
            .message("logout successfully")
            .timestamp(ZonedDateTime.now())
            .build());
  }
}
