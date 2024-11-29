package com.legend.user_service.repository;

import com.legend.user_service.constant.UserRole;
import com.legend.user_service.entity.Role;
import java.util.Set;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {

  Set<Role> findByUserRoleIn(Set<UserRole> roles);
}
