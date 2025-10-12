package kh.dev.common_util.config;

import java.util.Collection;
import java.util.Objects;
import java.util.Set;
import kh.dev.common_util.constant.Role;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

/**
 * Custom implementation of {@link JwtAuthenticationToken} that extends its functionality to include
 * additional user-specific fields for Keycloak integration.
 *
 * <p>This class holds additional details extracted from the JWT, such as Keycloak ID, email, full
 * name, first name, and last name. It also overrides {@code equals} and {@code hashCode} to include
 * these fields in the equality and hash computations.
 */
@Getter
public class CustomJwtAuthenticationToken extends JwtAuthenticationToken {

  private final String keycloakId;
  private final String email;
  private final String fullName;
  private final String firstName;
  private final String lastName;
  private final Set<Role> roles;

  /**
   * Constructs a {@code CustomJwtAuthenticationToken} with the given JWT and user details.
   *
   * @param jwt the {@link Jwt} containing authentication information.
   * @param authorities the granted authorities for the authenticated user.
   * @param keycloakId the unique Keycloak ID associated with the user.
   * @param email the email address of the user.
   * @param fullName the full name of the user.
   * @param firstName the first name of the user.
   * @param lastName the last name of the user.
   * @param roles the roles of the user.
   */
  public CustomJwtAuthenticationToken(
      Jwt jwt,
      Collection<? extends GrantedAuthority> authorities,
      String keycloakId,
      String email,
      String fullName,
      String firstName,
      String lastName,
      Set<Role> roles) {
    super(jwt, authorities);
    this.keycloakId = keycloakId;
    this.email = email;
    this.fullName = fullName;
    this.firstName = firstName;
    this.lastName = lastName;
    this.roles = roles;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof CustomJwtAuthenticationToken that)) return false;
    if (!super.equals(o)) return false;

    return Objects.equals(keycloakId, that.keycloakId)
        && Objects.equals(email, that.email)
        && Objects.equals(fullName, that.fullName)
        && Objects.equals(firstName, that.firstName)
        && Objects.equals(lastName, that.lastName);
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), keycloakId, email, fullName, firstName, lastName, roles);
  }
}
