package com.legend.common_util.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class UserRegistrationJteParam {
  private String firstName;
  private String lastName;
  private String email;
}
