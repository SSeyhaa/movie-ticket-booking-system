package kh.dev.movie_service.model.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class CinemaDto {
  private Long id;
  private String name;
  private List<TheaterDto> theaters;
}
