package kh.dev.user_service.service;

import java.util.List;
import java.util.Set;
import kh.dev.common_util.constant.Role;
import kh.dev.user_service.model.entity.SystemRole;
import kh.dev.user_service.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class RoleService {

  private final RoleRepository roleRepository;

  public List<SystemRole> createRoles(List<SystemRole> roles) {
    return roleRepository.saveAll(roles);
  }

  public Set<SystemRole> findRoles(Set<Role> roles) {
    return roleRepository.findByRoleIn(roles);
  }

  public Set<Role> mapToRoles(Set<SystemRole> systemRoles) {
    return systemRoles.stream()
        .map(SystemRole::getRole)
        .collect(java.util.stream.Collectors.toSet());
  }
}
