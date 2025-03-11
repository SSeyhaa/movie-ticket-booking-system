package kh.dev.movie_service.file;

import kh.dev.common_util.dto.SeatDto;
import kh.dev.common_util.file.csv.CSVMapper;
import org.springframework.stereotype.Component;

@Component
public class SeatCSVMapper implements CSVMapper<SeatDto, SeatCSVRepresentation> {
  @Override
  public SeatDto mapTo(SeatCSVRepresentation seatCSVRepresentation) {
    return SeatDto.builder()
        .id(seatCSVRepresentation.getId())
        .rowLabel(seatCSVRepresentation.getRowLabel())
        .seatNumber(seatCSVRepresentation.getSeatNumber())
        .seatType(seatCSVRepresentation.getSeatType())
        .isActive(seatCSVRepresentation.isActive())
        .build();
  }

  @Override
  public SeatCSVRepresentation unmapFrom(SeatDto seat) {
    return SeatCSVRepresentation.builder()
        .id(seat.getId())
        .rowLabel(seat.getRowLabel())
        .seatNumber(seat.getSeatNumber())
        .seatType(seat.getSeatType())
        .isActive(seat.isActive())
        .build();
  }
}
