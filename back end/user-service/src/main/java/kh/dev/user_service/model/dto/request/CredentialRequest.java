package kh.dev.user_service.model.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class CredentialRequest {
  @NotNull(message = "Id is required")
  private Long id;

  @NotBlank(message = "Email is required")
  @Email(message = "Invalid email format")
  // todo: @Trim
  private String email;

  @NotBlank(message = "Password is required")
  //  @Size(min = 8, message = "Password must be at least 8 characters")
  // todo: @Trim
  private String password;

  @NotBlank(message = "Confirm Password is required")
  //  @Size(min = 8, message = "Password must be at least 8 characters")
  // todo: @Trim
  private String confirmPassword;
}
