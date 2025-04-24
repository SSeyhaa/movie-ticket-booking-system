package kh.dev.movie_service.model.dto.request;

import jakarta.validation.constraints.NotNull;
import java.util.List;
import kh.dev.movie_service.model.dto.TimeSlot;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class ShowTimeSlotRequest {

  @NotNull(message = "Cinema is required")
  private Long cinemaId;

  @NotNull(message = "Theater is required")
  private Long theaterId;

  @NotNull(message = "Show time is required")
  private Long showTimeId;

  private List<TimeSlot> timeSlot;
}
