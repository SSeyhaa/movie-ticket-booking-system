package com.legend.user_service.dto.response;

import com.legend.user_service.constant.UserRole;
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
  private Set<UserRole> roles;
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
