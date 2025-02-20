package kh.dev.user_service.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import kh.dev.user_service.constant.ProfileConstant;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.context.annotation.Profile;

@Profile(ProfileConstant.TEST)
@Entity
@Table(name = "system_roles")
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class RoleTest {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Enumerated(EnumType.STRING)
  private kh.dev.common_util.constant.SystemRole role;
}
