package com.legend.user_service.service;

import com.legend.user_service.dto.request.UserRequest;
import com.legend.user_service.dto.response.UserResponse;
import com.legend.user_service.entity.User;
import com.legend.user_service.exception.UserAlreadyExistsException;
import com.legend.user_service.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.representations.idm.UserRepresentation;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
  private final ModelMapper modelMapper;
  private final UserRepository userRepository;
  private final KeycloakService keycloakService;

  public UserResponse create(UserRequest userRequest) {
    UserRepresentation userRepresentation = keycloakService.createUser(userRequest);

    if (userRepository.existsByEmail(userRepresentation.getEmail())) {
      log.error("----- User with email {} already exists in Database", userRequest.getEmail());
      throw new UserAlreadyExistsException(
          "User with email " + userRepresentation.getEmail() + " already exists in Database");
    }

    userRequest.setKeycloakId(userRepresentation.getId());
    User user = modelMapper.map(userRequest, User.class);
    User userCreated = userRepository.save(user);

    return modelMapper.map(userCreated, UserResponse.class);
  }
}
