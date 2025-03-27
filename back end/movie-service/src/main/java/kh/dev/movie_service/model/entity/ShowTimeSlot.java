package kh.dev.movie_service.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotNull;
import java.time.LocalTime;
import kh.dev.common_util.config.Auditable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;

@Entity
@Table(
    uniqueConstraints =
        @UniqueConstraint(
            columnNames = {"cinema_id", "theater_id", "showtime_id", "start_time", "end_time"}))
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@FieldNameConstants
public class ShowTimeSlot extends Auditable {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotNull(message = "Start time is required")
  @Column(nullable = false)
  private LocalTime startTime;

  @NotNull(message = "End time is required")
  @Column(nullable = false)
  private LocalTime endTime;

  @ManyToOne(optional = false)
  @NotNull(message = "Cinema is required")
  private Cinema cinema;

  @ManyToOne(optional = false)
  @NotNull(message = "Theater is required")
  private Theater theater;

  @ManyToOne(optional = false)
  @JoinColumn(name = "showtime_id", nullable = false)
  private ShowTime showTime;
}
