package com.legend.user_service.config;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtConverter implements Converter<Jwt, AbstractAuthenticationToken> {
  private static final String REALM_ACCESS = "realm_access";
  private static final String ROLES = "roles";
  private static final String ROLE_PREFIX = "ROLE_";
  private static final String PREFERRED_USERNAME = "preferred_username";

  @Override
  public AbstractAuthenticationToken convert(@NonNull Jwt jwt) {
    Collection<GrantedAuthority> authorities = extractRealmRoles(jwt);
    return new JwtAuthenticationToken(jwt, authorities, jwt.getClaim(PREFERRED_USERNAME));
  }

  public Collection<GrantedAuthority> extractRealmRoles(@NonNull Jwt jwt) {
    Map<String, List<String>> realmAccess = getRealmAccess(jwt);

    if (realmAccess == null || realmAccess.get(ROLES) == null) {
      log.warn("----- No realm access or roles found in JWT");
      return Collections.emptySet();
    }

    return realmAccess.get(ROLES).stream()
        .map(role -> new SimpleGrantedAuthority(ROLE_PREFIX + role))
        .collect(Collectors.toSet());
  }

  private Map<String, List<String>> getRealmAccess(@NonNull Jwt jwt) {
    try {
      return jwt.getClaim(REALM_ACCESS);
    } catch (ClassCastException e) {
      log.error("----- Unexpected format for realm access claim", e);
      return Map.of();
    }
  }
}
