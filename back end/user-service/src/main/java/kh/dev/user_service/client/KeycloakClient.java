package kh.dev.user_service.client;

import java.util.Map;
import org.keycloak.representations.AccessTokenResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "keycloak", url = "${app.keycloak.server-url}/realms/${app.keycloak.realm}")
public interface KeycloakClient {

  @PostMapping(
      value = "/protocol/openid-connect/token",
      consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
  AccessTokenResponse getAccessToken(Map<String, ?> form);

  @PostMapping(
      value = "/protocol/openid-connect/token",
      consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
  AccessTokenResponse getRefreshToken(Map<String, ?> form);

  @PostMapping(
      value = "/protocol/openid-connect/logout",
      consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
  void logout(Map<String, ?> form);
}
