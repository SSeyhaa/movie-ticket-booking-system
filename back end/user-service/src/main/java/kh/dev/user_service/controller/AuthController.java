package kh.dev.user_service.controller;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import kh.dev.user_service.model.dto.request.CredentialRequest;
import kh.dev.user_service.service.KeycloakService;
import lombok.RequiredArgsConstructor;
import org.keycloak.representations.AccessTokenResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/auth")
@RequiredArgsConstructor
public class AuthController {
  private final KeycloakService keycloakService;

  @PostMapping("/access-token")
  public ResponseEntity<AccessTokenResponse> getAccessToken(
      @RequestBody @Valid CredentialRequest credentialRequest, HttpServletResponse response) {

    AccessTokenResponse accessToken = keycloakService.getAccessToken(credentialRequest);
    ResponseCookie refreshTokenCookie =
        createRefreshTokenCookie(accessToken.getRefreshToken(), accessToken.getRefreshExpiresIn());

    response.addHeader(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString());
    return ResponseEntity.ok(accessToken);
  }

  private ResponseCookie createRefreshTokenCookie(String refreshToken, long expiresIn) {
    return ResponseCookie.from("refresh_token", refreshToken)
        .path("/v1/auth/refresh-token") // Restrict cookie to refresh-token path
        .httpOnly(true) // Prevent access to cookie from JavaScript
        .secure(true) // Only sent over HTTPS
        .sameSite("Strict") // Prevent sending cookies in cross-origin requests, CSRF protection
        .maxAge(expiresIn)
        .build();
  }
}
