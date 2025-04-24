package kh.dev.movie_service.model.dto;

import jakarta.validation.constraints.NotNull;
import java.time.LocalTime;
import kh.dev.common_util.annotation.ValidTimeSlot;
import kh.dev.common_util.annotation.validator.TimeRange;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ValidTimeSlot
public class TimeSlot implements TimeRange {

  private Long id;

  @NotNull(message = "Start time is required")
  private LocalTime startTime;

  @NotNull(message = "End time is required")
  private LocalTime endTime;
}
