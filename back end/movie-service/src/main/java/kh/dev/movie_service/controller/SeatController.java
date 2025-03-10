package kh.dev.movie_service.controller;

import java.time.ZonedDateTime;
import java.util.Optional;
import kh.dev.common_util.annotation.RoleRequired;
import kh.dev.common_util.constant.SystemRole;
import kh.dev.common_util.dto.request.PaginationRequest;
import kh.dev.common_util.dto.response.Response;
import kh.dev.movie_service.constant.SeatType;
import kh.dev.movie_service.model.dto.SeatDto;
import kh.dev.movie_service.model.dto.TheaterSeatsDto;
import kh.dev.movie_service.model.dto.response.PaginationResponse;
import kh.dev.movie_service.service.SeatService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/seats")
@RequiredArgsConstructor
public class SeatController {

  private final SeatService seatService;

  @PostMapping
  @RoleRequired(required = SystemRole.SUPER_ADMIN)
  public ResponseEntity<TheaterSeatsDto> createSeat(@RequestBody TheaterSeatsDto theaterSeatsDto) {
    return new ResponseEntity<>(
        seatService.createSeatByTheater(theaterSeatsDto), HttpStatus.CREATED);
  }

  @GetMapping
  @RoleRequired(required = SystemRole.SUPER_ADMIN)
  public ResponseEntity<PaginationResponse<SeatDto>> getSeats(
      @ModelAttribute PaginationRequest paginationRequest,
      @RequestParam Optional<Long> theaterId,
      @RequestParam Optional<SeatType> seatType,
      @RequestParam Optional<Boolean> isActive) {

    return ResponseEntity.ok(
        seatService.getSeats(paginationRequest, theaterId, seatType, isActive));
  }

  @PutMapping
  @RoleRequired(required = SystemRole.SUPER_ADMIN)
  public ResponseEntity<Response> updateSeatsByTheaterId(
      @RequestBody TheaterSeatsDto theaterSeatsDto) {
    seatService.updateSeat(theaterSeatsDto);
    return new ResponseEntity<>(
        Response.builder()
            .code(HttpStatus.NO_CONTENT.value())
            .status("success")
            .message("Seats updated successfully")
            .timestamp(ZonedDateTime.now())
            .build(),
        HttpStatus.NO_CONTENT);
  }

  @DeleteMapping
  @RoleRequired(required = SystemRole.SUPER_ADMIN)
  public ResponseEntity<Response> deleteSeatsByTheaterId(
      @RequestBody TheaterSeatsDto theaterSeatsDto) {
    seatService.deleteSeatsByTheaterId(theaterSeatsDto);
    return new ResponseEntity<>(
        Response.builder()
            .code(HttpStatus.NO_CONTENT.value())
            .status("success")
            .message("Seats deleted successfully")
            .timestamp(ZonedDateTime.now())
            .build(),
        HttpStatus.NO_CONTENT);
  }
}
