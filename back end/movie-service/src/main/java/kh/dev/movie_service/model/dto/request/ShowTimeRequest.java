package kh.dev.movie_service.model.dto.request;

import java.time.ZonedDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class ShowTimeRequest {
  private Long movieId;

  // todo: validate time
  private ZonedDateTime date;
}
