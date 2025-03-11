package kh.dev.common_util.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import kh.dev.common_util.constant.SeatType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class SeatDto {

  private Long id;

  @NotBlank(message = "Row label is required")
  private String rowLabel;

  @Min(value = 1, message = "Seat number must be at least 1")
  @Max(value = 100, message = "Seat number must be at most 100")
  private int seatNumber;

  private SeatType seatType = SeatType.STANDARD;
  private boolean isActive = true;
}
