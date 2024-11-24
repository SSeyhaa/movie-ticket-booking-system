package com.legend.movie_service.controller;

import com.legend.common_util.constant.CommonParam;
import com.legend.movie_service.dto.CinemaDto;
import com.legend.movie_service.dto.response.PaginationResponse;
import com.legend.movie_service.service.CinemaService;
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
@RequestMapping("/cinemas")
@RequiredArgsConstructor
public class CinemaController {

  private final CinemaService cinemaService;

  @PostMapping
  public ResponseEntity<CinemaDto> addCinema(@RequestBody CinemaDto cinemaDto) {
    return ResponseEntity.ok(cinemaService.addCinema(cinemaDto));
  }

  @GetMapping
  public ResponseEntity<PaginationResponse<CinemaDto>> getCinemaWithPage(
      @RequestParam(defaultValue = CommonParam.ONE, required = false) int pageNumber,
      @RequestParam(defaultValue = CommonParam.TEN, required = false) int pageSize,
      @RequestParam(defaultValue = CommonParam.SORT_DIRECTION_DESC, required = false)
          String sortDirection,
      @RequestParam(defaultValue = CommonParam.ID, required = false) String sortBy) {
    return ResponseEntity.ok(
        cinemaService.getCinemaWithPage(pageNumber, pageSize, sortDirection, sortBy));
  }

  @GetMapping("/{id}")
  public ResponseEntity<CinemaDto> getCinemaById(@PathVariable Long id) {
    return ResponseEntity.ok(cinemaService.getCinemaById(id));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteCinemaById(@PathVariable Long id) {
    cinemaService.deleteCinemaById(id);
    return ResponseEntity.noContent().build();
  }

  @PutMapping("/{id}")
  public ResponseEntity<CinemaDto> updateCinemaById(
      @PathVariable Long id, @RequestBody CinemaDto cinemaDto) {
    return ResponseEntity.ok(cinemaService.updateCinemaById(id, cinemaDto));
  }
}
