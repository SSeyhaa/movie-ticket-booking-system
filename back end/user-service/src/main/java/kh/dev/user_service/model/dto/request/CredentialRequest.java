package kh.dev.user_service.model.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import kh.dev.common_util.annotation.StringProcessor;
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
  @Email(message = "Invalid email format") // todo: validate email
  @StringProcessor(trimSpaces = true)
  private String email;

  @NotBlank(message = "Password is required")
  //  todo: @Size(min = 8, message = "Password must be at least 8 characters")
  @StringProcessor(trimSpaces = true)
  private String password;

  @NotBlank(message = "Confirm Password is required")
  //  todo: @Size(min = 8, message = "Password must be at least 8 characters")
  @StringProcessor(trimSpaces = true)
  private String confirmPassword;
}
