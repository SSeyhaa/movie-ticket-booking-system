package kh.dev.movie_service.model.dto.request;

import jakarta.validation.constraints.NotNull;
import java.time.LocalTime;
import kh.dev.common_util.annotation.ValidTimeSlot;
import kh.dev.common_util.annotation.validator.TimeRange;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@ValidTimeSlot
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class ShowTimeSlotRequest implements TimeRange {

  @NotNull(message = "Cinema is required")
  private Long cinemaId;

  @NotNull(message = "Theater is required")
  private Long theaterId;

  @NotNull(message = "Show time is required")
  private Long showTimeId;

  @NotNull(message = "Start time is required")
  private LocalTime startTime;

  @NotNull(message = "End time is required")
  private LocalTime endTime;
}
