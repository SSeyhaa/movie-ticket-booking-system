package kh.dev.movie_service.model.dto.response;

import java.time.ZonedDateTime;
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
public class ShowTimeResponse {

  private Long id;
  private String movieTitle;
  private ZonedDateTime date;
}
