package kh.dev.user_service.model.dto.request;

import java.util.Set;
import kh.dev.common_util.constant.SystemRole;
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
public class UserRequest {

  private String keycloakId;
  private String profileImagePath;
  private String username;
  private String email;
  private String password;
  private Set<SystemRole> roles;
  private String firstName;
  private String lastName;
  private String phoneNumber;
  private String address;
  private String city;
  private boolean isActive;
}
