package com.legend.user_service.dto.response;

import com.legend.user_service.constant.Role;
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
public class UserResponse {

  private Long id;

  private String keycloakId;
  private String profileImagePath;
  private String username;
  private String email;
  private Set<Role> roles;
  private String firstName;
  private String lastName;
  private String phoneNumber;
  private String address;
  private String city;
  private boolean isActive;
}
