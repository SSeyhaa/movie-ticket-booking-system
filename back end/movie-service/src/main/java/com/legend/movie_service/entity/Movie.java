package com.legend.movie_service.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;

@Entity
@Table(name = "movies")
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@FieldNameConstants
public class Movie {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String title;
  private String posterPath;
  private String description;
  private ZonedDateTime releasedDate;
  private int durationMin;
  private String genre;

  @Column(name = "\"cast\"")
  private String cast;

  private String language;
  private String country;
  private String trailerUrl;

  @OneToMany(mappedBy = "movie", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<ShowTime> showTimes;

  public List<ShowTime> getShowTimes() {
    if (showTimes == null) {
      showTimes = new ArrayList<>();
    }
    return showTimes;
  }
}
