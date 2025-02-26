package kh.dev.user_service.service;

import java.util.Optional;
import java.util.Set;
import kh.dev.common_util.constant.NotificationTemplate;
import kh.dev.common_util.constant.NotificationType;
import kh.dev.common_util.constant.TopicMessageBinding;
import kh.dev.common_util.constant.UserConstants;
import kh.dev.common_util.dto.request.NotificationRequest;
import kh.dev.common_util.util.ExecutionContext;
import kh.dev.user_service.exception.ResourceNotFoundException;
import kh.dev.user_service.exception.UserAlreadyExistsException;
import kh.dev.user_service.exception.UserCreationException;
import kh.dev.user_service.exception.ValidationException;
import kh.dev.user_service.model.dto.request.CredentialRequest;
import kh.dev.user_service.model.dto.request.UserRequest;
import kh.dev.user_service.model.dto.response.UserResponse;
import kh.dev.user_service.model.entity.SystemRole;
import kh.dev.user_service.model.entity.User;
import kh.dev.user_service.repository.RoleRepository;
import kh.dev.user_service.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.representations.idm.UserRepresentation;
import org.modelmapper.ModelMapper;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
  private final StreamBridge streamBridge;
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

      userCreated = createUserDB(userRepresentation.getId(), userRequest);

      notifyUserCreated(userCreated);

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

  private User createUserDB(String userRepresentationId, UserRequest userRequest) {
    userRequest.setKeycloakId(userRepresentationId);
    User user = modelMapper.map(userRequest, User.class);
    Set<SystemRole> systemRoles = roleRepository.findByRoleIn(userRequest.getRoles());
    user.setSystemRoles(systemRoles);
    return userRepository.save(user);
  }

  private void notifyUserCreated(User userCreated) {
    NotificationRequest notificationRequest = new NotificationRequest();
    notificationRequest.setRecipient(userCreated.getEmail());
    notificationRequest.setType(NotificationType.EMAIL);
    notificationRequest.setTemplate(NotificationTemplate.USER_REGISTRATION);

    ExecutionContext metadata = new ExecutionContext();
    metadata.put(UserConstants.FIRST_NAME, userCreated.getFirstName());
    metadata.put(UserConstants.LAST_NAME, userCreated.getLastName());
    notificationRequest.setMetadata(metadata);

    streamBridge.send(TopicMessageBinding.NOTIFICATION_EVENT_TOPIC.toString(), notificationRequest);
  }

  public void changePassword(CredentialRequest credentialRequest) {
    if (!credentialRequest.getPassword().equals(credentialRequest.getConfirmPassword())) {
      throw new ValidationException("Password and confirm password do not match");
    }

    User user = findUserByIdAndEmail(credentialRequest.getId(), credentialRequest.getEmail());
    keycloakService.changePassword(user.getKeycloakId(), credentialRequest.getPassword());

    notifyUserPasswordUpdated(user);
  }

  private void notifyUserPasswordUpdated(User user) {
    NotificationRequest notificationRequest = new NotificationRequest();
    notificationRequest.setRecipient(user.getEmail());
    notificationRequest.setType(NotificationType.EMAIL);
    notificationRequest.setTemplate(NotificationTemplate.USER_PASSWORD_UPDATE);

    ExecutionContext metadata = new ExecutionContext();
    metadata.put(UserConstants.FIRST_NAME, user.getFirstName());
    metadata.put(UserConstants.LAST_NAME, user.getLastName());
    notificationRequest.setMetadata(metadata);

    streamBridge.send(TopicMessageBinding.NOTIFICATION_EVENT_TOPIC.toString(), notificationRequest);
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

  private User findUserByIdAndEmail(Long id, String email) {
    return userRepository
        .findByIdAndEmail(id, email)
        .orElseThrow(
            () ->
                new ResourceNotFoundException(
                    String.format("User not found with id : %d, email: %s", id, email)));
  }

  @Transactional
  public void deleteUserById(Long id) {
    User user = findUserById(id);
    userRepository.delete(user);
    keycloakService.deleteUserById(user.getKeycloakId());
    log.info("----- User with email {} deleted successfully", user.getEmail());
  }
}
