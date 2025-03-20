package kh.dev.movie_service.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import kh.dev.common_util.config.Auditable;
import kh.dev.common_util.constant.SeatType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;

@Entity
@Table(
    uniqueConstraints = @UniqueConstraint(columnNames = {"theater_id", "row_label", "seat_number"}))
@FieldNameConstants
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class Seat extends Auditable {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String rowLabel;

  private int seatNumber;

  @Enumerated(EnumType.STRING)
  private SeatType seatType = SeatType.STANDARD;

  private boolean isActive = true;

  @ManyToOne(fetch = FetchType.LAZY)
  private Theater theater;
}
