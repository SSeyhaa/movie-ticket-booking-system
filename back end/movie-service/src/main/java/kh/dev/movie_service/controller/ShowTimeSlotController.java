package kh.dev.movie_service.controller;

import jakarta.validation.Valid;
import java.time.ZonedDateTime;
import java.util.Set;
import kh.dev.common_util.dto.response.Response;
import kh.dev.movie_service.model.dto.request.ShowTimeSlotRequest;
import kh.dev.movie_service.model.dto.response.ShowTimeSlotResponse;
import kh.dev.movie_service.service.ShowTimeSlotService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/show-times/slots")
@RequiredArgsConstructor
public class ShowTimeSlotController {

  private final ShowTimeSlotService showTimeSlotService;

  @PostMapping
  public ResponseEntity<ShowTimeSlotResponse> addShowTimeSlot(
      @RequestBody @Valid ShowTimeSlotRequest showTimeSlotRequest) {

    return new ResponseEntity<>(
        showTimeSlotService.addShowTimeSlots(showTimeSlotRequest), HttpStatus.CREATED);
  }

  @GetMapping
  public ResponseEntity<ShowTimeSlotResponse> getShowTimeSlots(
      @RequestParam Long cinemaId, @RequestParam Long theaterId, @RequestParam Long showTimeId) {

    return new ResponseEntity<>(
        showTimeSlotService.getShowTimeSlots(cinemaId, theaterId, showTimeId), HttpStatus.OK);
  }

  @DeleteMapping
  public ResponseEntity<Response> deleteShowTimeSlots(@RequestParam Set<Long> showTimeSlotIds) {
    showTimeSlotService.deleteShowTimeSlots(showTimeSlotIds);
    return new ResponseEntity<>(
        Response.builder()
            .code(HttpStatus.OK.value())
            .status("success")
            .message("show time slots deleted successfully")
            .timestamp(ZonedDateTime.now())
            .build(),
        HttpStatus.OK);
  }
}
