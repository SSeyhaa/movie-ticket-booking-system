package kh.dev.movie_service.model.dto;

import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class TheaterSeatsDto {

  private Long theaterId;
  private Set<SeatDto> seats;
}
