package kh.dev.movie_service.model.dto;

import java.util.Set;

import kh.dev.common_util.dto.SeatDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class TheaterDto {

  private Long id;
  private String name;
  private Set<SeatDto> seats;
}
