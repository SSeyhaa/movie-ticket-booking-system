package kh.dev.movie_service.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalTime;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class TimeSlot {

  private Long id;

  private LocalTime startTime;

  private LocalTime endTime;
}
