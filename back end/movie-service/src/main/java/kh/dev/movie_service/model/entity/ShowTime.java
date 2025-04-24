package kh.dev.movie_service.model.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import kh.dev.common_util.config.Auditable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"date", "movie_id"}))
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@FieldNameConstants
public class ShowTime extends Auditable {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private ZonedDateTime date;

  @ManyToOne(optional = false)
  private Movie movie;

  @OneToMany(mappedBy = "showTime", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<ShowTimeSlot> showTimeSlots = new ArrayList<>();
}
