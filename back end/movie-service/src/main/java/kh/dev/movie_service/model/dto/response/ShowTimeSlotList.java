package kh.dev.movie_service.model.dto.response;

import java.util.List;
import kh.dev.movie_service.model.dto.TimeSlot;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class ShowTimeSlotList {

  private String cinemaName;

  private String theaterName;

  private ShowTimeResponse showTime;

  private List<TimeSlot> timeSlot;
}
