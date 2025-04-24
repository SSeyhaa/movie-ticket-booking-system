package kh.dev.movie_service.model.dto.request;

import java.time.ZonedDateTime;
import kh.dev.common_util.annotation.ValidDateTime;
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

  @ValidDateTime(isFuture = true, message = "date time must be in the future")
  private ZonedDateTime date;
}
