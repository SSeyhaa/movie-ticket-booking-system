package kh.dev.movie_service.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;
import java.time.ZonedDateTime;
import kh.dev.common_util.config.Auditable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@FieldNameConstants
public class ShowTime extends Auditable {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private ZonedDateTime startTime;

  private ZonedDateTime endTime;

  @ManyToOne
  @NotNull(message = "please add the cinema for the show time")
  private Cinema cinema;

  @ManyToOne
  @NotNull(message = "please add the theater for the show time")
  private Theater theater;

  @ManyToOne
  @NotNull(message = "please add the movie for the show time")
  private Movie movie;
}
