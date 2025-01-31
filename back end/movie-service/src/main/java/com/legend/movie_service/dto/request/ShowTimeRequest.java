package com.legend.movie_service.dto.request;

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
  private Long cinemaId;
  private Long theaterId;
  private Long movieId;
  private ZonedDateTime dateTime;
}
