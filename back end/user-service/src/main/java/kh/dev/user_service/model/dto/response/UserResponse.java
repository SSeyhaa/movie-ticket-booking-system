package kh.dev.user_service.model.dto.response;

import java.util.Set;
import kh.dev.common_util.constant.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class UserResponse {

  private Long id;

  private String keycloakId;
  private Set<Role> roles;
  private String profileImagePath;
  private String username;
  private String email;
  private String firstName;
  private String lastName;
  private String phoneNumber;
  private String address;
  private String city;
  private boolean isActive;
}
