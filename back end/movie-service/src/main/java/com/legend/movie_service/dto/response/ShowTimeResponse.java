package com.legend.movie_service.dto.response;

import java.time.ZonedDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class ShowTimeResponse {

  private Long id;
  private ZonedDateTime dateTime;
  private String cinema;
  private String theater;
}
