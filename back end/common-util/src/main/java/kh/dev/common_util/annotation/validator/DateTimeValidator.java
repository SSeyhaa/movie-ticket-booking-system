package kh.dev.common_util.annotation.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Calendar;
import java.util.Date;
import kh.dev.common_util.annotation.ValidDateTime;

public class DateTimeValidator implements ConstraintValidator<ValidDateTime, Object> {

  private boolean isFuture;
  private boolean isPast;

  @Override
  public void initialize(ValidDateTime constraintAnnotation) {
    this.isFuture = constraintAnnotation.isFuture();
    this.isPast = constraintAnnotation.isPast();
  }

  @Override
  public boolean isValid(Object value, ConstraintValidatorContext context) {
    if (value == null) {
      return false;
    }

    Instant now = Instant.now();
    return switch (value) {
      case ZonedDateTime zdt -> validateInstant(zdt.toInstant(), now);
      case OffsetDateTime odt -> validateInstant(odt.toInstant(), now);
      case Instant instant -> validateInstant(instant, now);
      case LocalDateTime ldt -> validateInstant(ldt.atZone(ZoneId.systemDefault()).toInstant(), now);
      case LocalDate ld -> validateInstant(ld.atStartOfDay(ZoneId.systemDefault()).toInstant(), now);
      case LocalTime lt -> validateInstant(lt.atDate(LocalDate.now()).atZone(ZoneId.systemDefault()).toInstant(), now);
      case Date date -> validateInstant(date.toInstant(), now);
      case Calendar cal -> validateInstant(cal.toInstant(), now);
      default -> false; // Unsupported type
    };
  }

  private boolean validateInstant(Instant time, Instant now) {
    if (isFuture) return time.isAfter(now);
    if (isPast) return time.isBefore(now);
    return true;
  }
}
