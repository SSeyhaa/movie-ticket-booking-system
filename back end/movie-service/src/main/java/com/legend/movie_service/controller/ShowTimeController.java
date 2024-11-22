package com.legend.movie_service.controller;

import com.legend.common_util.constant.CommonParam;
import com.legend.movie_service.dto.request.ShowTimeRequest;
import com.legend.movie_service.dto.response.PaginationResponse;
import com.legend.movie_service.dto.response.ShowTimeResponse;
import com.legend.movie_service.service.ShowTimeService;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
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

  @PostMapping("/{movieId}")
  public ResponseEntity<ShowTimeResponse> createShowTime(
      @PathVariable Long movieId, @RequestBody ShowTimeRequest showTimeRequest) {
    return ResponseEntity.ok(showTimeService.createShowTime(movieId, showTimeRequest));
  }

  @GetMapping("/{id}")
  public ResponseEntity<ShowTimeResponse> getShowTimeById(@PathVariable Long id) {
    return ResponseEntity.ok(showTimeService.getShowTimeById(id));
  }

  @GetMapping
  public ResponseEntity<PaginationResponse<ShowTimeResponse>> getSHowTimes(
      @RequestParam(defaultValue = CommonParam.PAGE_NUMBER) int pageNumber,
      @RequestParam(defaultValue = CommonParam.PAGE_SIZE) int pageSize,
      @RequestParam(defaultValue = CommonParam.ID, required = false) String sortBy,
      @RequestParam(defaultValue = CommonParam.SORT_DIRECTION_DESC, required = false)
          String sortDirection,
      @RequestParam(required = false) String cinema,
      @RequestParam(required = false) LocalDateTime dateTime,
      @RequestParam(required = false) String movieTitle) {
    return ResponseEntity.ok(
        showTimeService.getShowTimesWithFilters(
            pageNumber, pageSize, sortBy, sortDirection, cinema, dateTime, movieTitle));
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
