package kh.dev.common_util.annotation.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import kh.dev.common_util.annotation.ValidEmail;

public class EmailValidator implements ConstraintValidator<ValidEmail, String> {

  private String regex;
  private boolean required;

  @Override
  public void initialize(ValidEmail constraintAnnotation) {
    this.regex = constraintAnnotation.regex();
    this.required = constraintAnnotation.required();
  }

  @Override
  public boolean isValid(String value, ConstraintValidatorContext context) {
    if (required && (value == null || value.isBlank())) {
      return false;
    }

    if (value == null || value.isBlank()) {
      return true;
    }

    return value.matches(regex);
  }
}
