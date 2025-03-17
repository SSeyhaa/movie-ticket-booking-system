package kh.dev.common_util.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import kh.dev.common_util.config.AuthorizationInterceptor;
import kh.dev.common_util.constant.SystemRole;

/**
 * Annotation to specify role-based access control for methods.
 *
 * <p>Methods annotated with {@code @RoleRequired} will be restricted to users having at least one
 * of the specified roles. This annotation is processed by the {@link AuthorizationInterceptor}.
 *
 * <p><strong>Usage Example:</strong>
 *
 * <pre>{@code
 * @RoleRequired(required = {SystemRole.SUPER_ADMIN, SystemRole.MODERATOR})
 * @GetMapping("/admin/dashboard")
 * public ResponseEntity<String> getAdminDashboard() {
 *     return ResponseEntity.ok("Admin Dashboard");
 * }
 * }</pre>
 *
 * <p>The required roles must be defined in the {@link SystemRole} enum. If a user does not have any
 * of the specified roles, access will be denied.
 *
 * @see AuthorizationInterceptor
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RoleRequired {

  SystemRole[] required();
}
