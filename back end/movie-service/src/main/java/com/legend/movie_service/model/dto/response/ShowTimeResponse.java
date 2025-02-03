package com.legend.movie_service.model.dto.response;

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
  private String cinema;
  private String theater;
  private ZonedDateTime dateTime;
  private String movieTitle;
}
