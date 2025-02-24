package kh.dev.user_service.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import java.util.Optional;
import kh.dev.user_service.exception.ResourceNotFoundException;
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
import org.springframework.web.util.WebUtils;

@RestController
@RequestMapping("/public/v1/auth")
@RequiredArgsConstructor
public class AuthController {

  private static final String REFRESH_TOKEN = "refresh_token";

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

  @PostMapping("/refresh-token")
  public ResponseEntity<AccessTokenResponse> refreshAccessToken(HttpServletRequest request) {

    Cookie refreshTokenCookie = getRefreshTokenCookie(request);
    AccessTokenResponse accessToken =
        keycloakService.refreshAccessToken(refreshTokenCookie.getValue());
    return ResponseEntity.ok(accessToken);
  }

  @PostMapping("/logout")
  public ResponseEntity<Void> logout(HttpServletRequest request, HttpServletResponse response) {

    Cookie refreshTokenCookie = getRefreshTokenCookie(request);
    keycloakService.logout(refreshTokenCookie.getValue());
    ResponseCookie responseCookie = createRefreshTokenCookie("", 0);

    response.addHeader(HttpHeaders.SET_COOKIE, responseCookie.toString());
    return ResponseEntity.noContent().build();
  }

  private Cookie getRefreshTokenCookie(HttpServletRequest request) {
    return Optional.ofNullable(WebUtils.getCookie(request, REFRESH_TOKEN))
        .orElseThrow(() -> new ResourceNotFoundException("Refresh token not found"));
  }

  private ResponseCookie createRefreshTokenCookie(String refreshToken, long expiresIn) {
    return ResponseCookie.from(REFRESH_TOKEN, refreshToken)
        // .domain("localhost") // Restrict cookie to specific domain
        .path("/user-service/public/v1/auth") // Restrict cookie to refresh-token path
        .httpOnly(true) // Prevent access to cookie from JavaScript
        .secure(true) // Only sent over HTTPS
        .sameSite("Strict") // Prevent sending cookies in cross-origin requests, CSRF protection
        .maxAge(expiresIn)
        .build();
  }
}
