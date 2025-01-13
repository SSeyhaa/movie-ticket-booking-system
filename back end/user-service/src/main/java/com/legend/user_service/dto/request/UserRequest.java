package com.legend.user_service.dto.request;

import com.legend.common_util.constant.SystemRole;
import java.util.Set;
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
