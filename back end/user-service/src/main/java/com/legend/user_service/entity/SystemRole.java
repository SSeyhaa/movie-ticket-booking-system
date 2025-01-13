package com.legend.user_service.entity;

import com.legend.user_service.constant.ProfileConstant;
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
import org.hibernate.annotations.JdbcType;
import org.hibernate.dialect.PostgreSQLEnumJdbcType;
import org.springframework.context.annotation.Profile;

@Profile(ProfileConstant.NOT_TEST)
@Entity
@Table(name = "system_roles")
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class SystemRole {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Enumerated(EnumType.STRING)
  @JdbcType(PostgreSQLEnumJdbcType.class)
  private com.legend.common_util.constant.SystemRole role;
}
