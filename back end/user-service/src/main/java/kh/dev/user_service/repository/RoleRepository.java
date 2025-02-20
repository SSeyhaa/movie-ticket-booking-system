package kh.dev.user_service.repository;

import java.util.Set;
import kh.dev.user_service.model.entity.SystemRole;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<SystemRole, Long> {

  Set<SystemRole> findByRoleIn(Set<kh.dev.common_util.constant.SystemRole> roles);
}
