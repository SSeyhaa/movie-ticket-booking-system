package com.legend.movie_service.dto.request;

import java.time.LocalDateTime;
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
  private LocalDateTime dateTime;
  private String cinema;
  private String theater;
}
