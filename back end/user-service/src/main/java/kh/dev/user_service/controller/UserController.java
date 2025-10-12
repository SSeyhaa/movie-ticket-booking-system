package kh.dev.user_service.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import java.io.IOException;
import java.time.ZonedDateTime;
import kh.dev.common_util.annotation.RoleRequired;
import kh.dev.common_util.constant.Role;
import kh.dev.common_util.dto.response.ApiResponse;
import kh.dev.common_util.dto.response.Response;
import kh.dev.user_service.model.dto.request.PasswordChangeRequest;
import kh.dev.user_service.model.dto.request.UserRequest;
import kh.dev.user_service.model.dto.response.UserResponse;
import kh.dev.user_service.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {
  private final UserService userService;

  @PostMapping
  public UserResponse createUser(@RequestBody UserRequest userRequest) {
    return userService.create(userRequest);
  }

  @PutMapping("/password")
  public ResponseEntity<Response> changePassword(
      HttpServletRequest request,
      HttpServletResponse response,
      @RequestBody @Valid PasswordChangeRequest passwordChangeRequest) {

    userService.changePassword(request, response, passwordChangeRequest);
    return ResponseEntity.ok(
        Response.builder()
            .code(HttpStatus.OK.value())
            .status("success")
            .message("Password updated successfully")
            .timestamp(ZonedDateTime.now())
            .build());
  }

  @GetMapping("/{id}")
  @RoleRequired(required = {Role.SUPER_ADMIN, Role.USER})
  public ResponseEntity<ApiResponse<UserResponse>> getUserById(@PathVariable Long id) {
    UserResponse userResponse = userService.getUserById(id);
    return ResponseEntity.ok(ApiResponse.success("user has been found", userResponse));
  }

  @PutMapping("/{id}")
  public ResponseEntity<UserResponse> updateUserById(
      @PathVariable Long id, @RequestBody UserRequest userRequest) {
    UserResponse userResponse = userService.updateUserById(id, userRequest);
    return ResponseEntity.ok(userResponse);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Response> deleteUserById(@PathVariable Long id) {
    userService.deleteUserById(id);
    return ResponseEntity.ok(
        Response.builder()
            .code(HttpStatus.OK.value())
            .status("success")
            .message("User deleted successfully")
            .timestamp(ZonedDateTime.now())
            .build());
  }

  @PostMapping(value = "/profile", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<Response> uploadImage(@RequestParam MultipartFile file) throws IOException {
    return ResponseEntity.ok(
        Response.builder()
            .code(HttpStatus.OK.value())
            .status("success")
            .message("image uploaded successfully")
            .body(userService.uploadProfilePicture(file))
            .timestamp(ZonedDateTime.now())
            .build());
  }
}
