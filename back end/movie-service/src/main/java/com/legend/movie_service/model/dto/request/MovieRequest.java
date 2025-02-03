package com.legend.movie_service.model.dto.request;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class MovieRequest {
  private String title;
  private String posterPath;
  private String description;
  private ZonedDateTime releasedDate;
  private int durationMin;
  private String genre;
  private String cast;
  private String language;
  private String country;
  private String trailerUrl;
}
