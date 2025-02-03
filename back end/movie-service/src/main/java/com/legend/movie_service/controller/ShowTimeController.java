package com.legend.movie_service.controller;

import com.legend.common_util.dto.request.PaginationRequest;
import com.legend.movie_service.dto.request.ShowTimeRequest;
import com.legend.movie_service.dto.response.PaginationResponse;
import com.legend.movie_service.dto.response.ShowTimeResponse;
import com.legend.movie_service.service.ShowTimeService;
import java.time.ZonedDateTime;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/show-times")
@RequiredArgsConstructor
public class ShowTimeController {

  private final ShowTimeService showTimeService;

  @PostMapping
  public ResponseEntity<ShowTimeResponse> createShowTime(
      @RequestBody ShowTimeRequest showTimeRequest) {
    return ResponseEntity.ok(showTimeService.createShowTime(showTimeRequest));
  }

  @GetMapping("/{id}")
  public ResponseEntity<ShowTimeResponse> getShowTimeById(@PathVariable Long id) {
    return ResponseEntity.ok(showTimeService.getShowTimeById(id));
  }

  @GetMapping
  public ResponseEntity<PaginationResponse<ShowTimeResponse>> getSHowTimes(
      @ModelAttribute PaginationRequest paginationRequest,
      @RequestParam Optional<String> cinema,
      @RequestParam Optional<String> theater,
      @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) ZonedDateTime dateTime,
      @RequestParam Optional<String> movieTitle) {
    return ResponseEntity.ok(
        showTimeService.getShowTimesWithFilters(
            paginationRequest, cinema, theater, dateTime, movieTitle));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteShowTimeById(@PathVariable Long id) {
    showTimeService.deleteShowTimeById(id);
    return ResponseEntity.noContent().build();
  }

  @PutMapping("/{id}")
  public ResponseEntity<ShowTimeResponse> updateShowTimeById(
      @PathVariable Long id, @RequestBody ShowTimeRequest showTimeRequest) {
    return ResponseEntity.ok(showTimeService.updateShowTime(id, showTimeRequest));
  }
}
