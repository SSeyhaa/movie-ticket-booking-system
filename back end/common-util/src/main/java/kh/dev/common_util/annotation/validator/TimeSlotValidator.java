package kh.dev.common_util.annotation.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import kh.dev.common_util.annotation.ValidTimeSlot;

public class TimeSlotValidator implements ConstraintValidator<ValidTimeSlot, TimeRange> {

  @Override
  public boolean isValid(TimeRange timeRange, ConstraintValidatorContext context) {
    if (timeRange == null || timeRange.getStartTime() == null || timeRange.getEndTime() == null) {
      return false;
    }

    return timeRange.getStartTime().isBefore(timeRange.getEndTime());
  }
}
