package kh.dev.movie_service.controller;

import jakarta.validation.Valid;
import kh.dev.movie_service.model.dto.request.ShowTimeSlotRequest;
import kh.dev.movie_service.model.dto.response.ShowTimeSlotResponse;
import kh.dev.movie_service.service.ShowTimeSlotService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
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
        showTimeSlotService.addShowTimeSlot(showTimeSlotRequest), HttpStatus.CREATED);
  }
}
