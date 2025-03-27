package kh.dev.movie_service.model.dto.response;

import java.time.LocalTime;
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
public class ShowTimeSlotResponse {

  private Long id;

  private String cinemaName;

  private String theaterName;

  private ShowTimeResponse showTime;

  private LocalTime startTime;

  private LocalTime endTime;
}
