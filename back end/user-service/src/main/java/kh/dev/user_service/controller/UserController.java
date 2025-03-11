package kh.dev.user_service.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import java.time.ZonedDateTime;
import kh.dev.common_util.dto.response.Response;
import kh.dev.user_service.model.dto.request.PasswordChangeRequest;
import kh.dev.user_service.model.dto.request.UserRequest;
import kh.dev.user_service.model.dto.response.UserResponse;
import kh.dev.user_service.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/users")
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
  public ResponseEntity<UserResponse> getUserById(@PathVariable Long id) {
    UserResponse userResponse = userService.getUserById(id);
    return ResponseEntity.ok(userResponse);
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
}
