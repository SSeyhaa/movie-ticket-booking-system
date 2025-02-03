package com.legend.user_service.controller;

import com.legend.user_service.model.dto.request.UserRequest;
import com.legend.user_service.model.dto.response.UserResponse;
import com.legend.user_service.service.UserService;
import lombok.RequiredArgsConstructor;
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
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
  private final UserService userService;

  @PostMapping
  public UserResponse createUser(@RequestBody UserRequest userRequest) {
    return userService.create(userRequest);
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
  public ResponseEntity<Void> deleteUserById(@PathVariable Long id) {
    userService.deleteUserById(id);
    return ResponseEntity.noContent().build();
  }
}
