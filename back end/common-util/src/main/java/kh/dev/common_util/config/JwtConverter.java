package kh.dev.common_util.config;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import kh.dev.common_util.constant.Role;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtConverter implements Converter<Jwt, AbstractAuthenticationToken> {
  private static final String REALM_ACCESS = "realm_access";
  private static final String ROLES = "roles";
  private static final String ROLE_PREFIX = "ROLE_";
  private static final String SUB_CLAIM = "sub";
  private static final String EMAIL_CLAIM = "email";
  private static final String NAME_CLAIM = "name";
  private static final String GIVEN_NAME_CLAIM = "given_name";
  private static final String FAMILY_NAME_CLAIM = "family_name";

  @Override
  public AbstractAuthenticationToken convert(@NonNull Jwt jwt) {
    final Set<Role> roles = extractRealmRoles(jwt);
    Collection<GrantedAuthority> authorities = getAuthorities(roles);

    return new CustomJwtAuthenticationToken(
        jwt,
        authorities,
        jwt.getClaim(SUB_CLAIM),
        jwt.getClaim(EMAIL_CLAIM),
        jwt.getClaim(NAME_CLAIM),
        jwt.getClaim(GIVEN_NAME_CLAIM),
        jwt.getClaim(FAMILY_NAME_CLAIM),
        roles);
  }

  public Set<Role> extractRealmRoles(@NonNull Jwt jwt) {
    Map<String, List<String>> realmAccess = getRealmAccess(jwt);

    if (realmAccess == null || realmAccess.get(ROLES) == null) {
      log.warn("----- No realm access or roles found in JWT");
      return Collections.emptySet();
    }

    return Role.getRolesEnum().stream()
        .filter(role -> realmAccess.get(ROLES).contains(role.toString()))
        .collect(Collectors.toSet());
  }

  public Collection<GrantedAuthority> getAuthorities(Set<Role> roles) {
    return roles.stream()
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
