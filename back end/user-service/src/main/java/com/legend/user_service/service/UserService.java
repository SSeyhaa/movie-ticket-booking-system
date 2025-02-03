package com.legend.user_service.service;

import com.legend.user_service.model.dto.request.UserRequest;
import com.legend.user_service.model.dto.response.UserResponse;
import com.legend.user_service.model.entity.SystemRole;
import com.legend.user_service.model.entity.User;
import com.legend.user_service.exception.ResourceNotFoundException;
import com.legend.user_service.exception.UserAlreadyExistsException;
import com.legend.user_service.exception.UserCreationException;
import com.legend.user_service.repository.RoleRepository;
import com.legend.user_service.repository.UserRepository;
import java.util.Optional;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.representations.idm.UserRepresentation;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
  private final ModelMapper modelMapper;
  private final UserRepository userRepository;
  private final KeycloakService keycloakService;
  private final RoleRepository roleRepository;

  public UserResponse create(UserRequest userRequest) {

    UserRepresentation userRepresentation = null;
    User userCreated = null;
    try {
      userRepresentation = keycloakService.createUser(userRequest);

      if (userRepository.existsByEmail(userRepresentation.getEmail())) {
        log.error("----- User with email {} already exists in Database", userRequest.getEmail());
        throw new UserAlreadyExistsException(
            "User with email " + userRepresentation.getEmail() + " already exists in Database");
      }

      userRequest.setKeycloakId(userRepresentation.getId());
      User user = modelMapper.map(userRequest, User.class);
      Set<SystemRole> systemRoles = roleRepository.findByRoleIn(userRequest.getRoles());
      user.setSystemRoles(systemRoles);
      userCreated = userRepository.save(user);
      log.info(
          "----- User with email {} created successfully in database server",
          userCreated.getEmail());
      return modelMapper.map(userCreated, UserResponse.class);
    } catch (Exception e) {

      Optional.ofNullable(userRepresentation)
          .ifPresent(user -> keycloakService.deleteUserById(user.getId()));

      Optional.ofNullable(userCreated).ifPresent(user -> userRepository.deleteById(user.getId()));

      log.error("----- {}", e.getMessage(), e);
      throw new UserCreationException(e.getMessage(), e);
    }
  }

  public UserResponse getUserById(Long id) {
    User user = findUserById(id);
    return modelMapper.map(user, UserResponse.class);
  }

  public UserResponse updateUserById(Long id, UserRequest userRequest) {
    User user = findUserById(id);
    user.setProfileImagePath(userRequest.getProfileImagePath());
    user.setUsername(userRequest.getUsername());
    user.setEmail(userRequest.getEmail());
    user.setFirstName(userRequest.getFirstName());
    user.setLastName(userRequest.getLastName());
    user.setPhoneNumber(userRequest.getPhoneNumber());
    user.setAddress(userRequest.getAddress());
    user.setCity(userRequest.getCity());
    User userUpdated = userRepository.save(user);
    log.info(
        "----- User with email {} updated successfully in database server", userUpdated.getEmail());
    return modelMapper.map(userUpdated, UserResponse.class);
  }

  private User findUserById(Long id) {
    return userRepository
        .findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
  }

  @Transactional
  public void deleteUserById(Long id) {
    User user = findUserById(id);
    userRepository.delete(user);
    keycloakService.deleteUserById(user.getKeycloakId());
    log.info("----- User with email {} deleted successfully", user.getEmail());
  }
}
