package kh.dev.user_service.start_up;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import kh.dev.common_util.constant.LogMessage;
import kh.dev.common_util.constant.Role;
import kh.dev.user_service.exception.RoleAssignmentException;
import kh.dev.user_service.model.entity.SystemRole;
import kh.dev.user_service.service.RoleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class DBRoleInitializer implements Task, Ordered {
  private final RoleService roleService;

  @Override
  public int getOrder() {
    return 0;
  }

  @Override
  public void run() {
    try {
      Set<Role> enumRoles = Role.getRolesEnum();

      Set<Role> existingRoles =
          roleService.findRoles(enumRoles).stream()
              .map(SystemRole::getRole)
              .collect(Collectors.toSet());

      Set<Role> missingRoles =
          enumRoles.stream()
              .filter(role -> !existingRoles.contains(role))
              .collect(Collectors.toSet());

      if (missingRoles.isEmpty()) {
        log.info(
            "{} Roles are already initialized in Database: {}",
            LogMessage.FIVE_DASH,
            existingRoles);
        return;
      }

      List<SystemRole> roles =
          missingRoles.stream()
              .map(
                  role -> {
                    SystemRole systemRole = new SystemRole();
                    systemRole.setRole(role);
                    return systemRole;
                  })
              .toList();

      roleService.createRoles(roles);
      log.info("{} Initialized roles to Database: {}", LogMessage.FIVE_DASH, roles);
    } catch (Exception e) {
      log.error("{} Failed to initialize roles to Database", LogMessage.FIVE_DASH, e);
      throw new RoleAssignmentException("Failed to initialize roles to Database", e);
    }
  }
}
