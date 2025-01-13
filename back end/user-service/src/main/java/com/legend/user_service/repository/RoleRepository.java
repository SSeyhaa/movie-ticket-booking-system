package com.legend.user_service.repository;

import com.legend.user_service.entity.SystemRole;
import java.util.Set;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<SystemRole, Long> {

  Set<SystemRole> findByRoleIn(Set<com.legend.common_util.constant.SystemRole> roles);
}
