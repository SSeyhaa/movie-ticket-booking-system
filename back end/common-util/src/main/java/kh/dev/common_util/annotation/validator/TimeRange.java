package kh.dev.common_util.annotation.validator;

import java.time.LocalTime;

public interface TimeRange {

  LocalTime getStartTime();

  LocalTime getEndTime();
}
