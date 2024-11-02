package com.legend.user_service.entity;

import com.legend.common_util.audit.Auditable;
import com.legend.user_service.constant.Role;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "users")
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class User extends Auditable {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String keycloakId;

  @Enumerated(EnumType.STRING)
  private Role role;

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
