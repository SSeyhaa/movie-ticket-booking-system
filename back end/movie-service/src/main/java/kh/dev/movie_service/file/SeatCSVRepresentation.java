package kh.dev.movie_service.file;

import com.opencsv.bean.CsvBindByName;
import kh.dev.common_util.constant.SeatType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class SeatCSVRepresentation {
  @CsvBindByName(column = "ID")
  private Long id;

  @CsvBindByName(column = "ROW_LABEL")
  private String rowLabel;

  @CsvBindByName(column = "SEAT_NUMBER")
  private int seatNumber;

  @CsvBindByName(column = "SEAT_TYPE")
  private SeatType seatType;

  @CsvBindByName(column = "IS_ACTIVE")
  private boolean isActive;
}
