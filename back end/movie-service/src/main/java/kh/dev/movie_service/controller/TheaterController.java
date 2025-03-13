package kh.dev.movie_service.controller;

import kh.dev.common_util.dto.request.PaginationRequest;
import kh.dev.movie_service.model.dto.TheaterDto;
import kh.dev.common_util.dto.response.PaginationResponse;
import kh.dev.movie_service.service.TheaterService;
import lombok.RequiredArgsConstructor;
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
@RequestMapping("/v1/theaters")
@RequiredArgsConstructor
public class TheaterController {

  private final TheaterService theaterService;

  @PostMapping
  public ResponseEntity<TheaterDto> addTheater(
      @RequestParam("cinemaId") Long cinemaId, @RequestBody TheaterDto theaterDto) {
    return ResponseEntity.ok(theaterService.addTheater(cinemaId, theaterDto));
  }

  @GetMapping
  public ResponseEntity<PaginationResponse<TheaterDto>> getTheaterWithPage(
      @ModelAttribute PaginationRequest paginationRequest) {
    return ResponseEntity.ok(theaterService.getTheaterWithPage(paginationRequest));
  }

  @GetMapping("/{id}")
  public ResponseEntity<TheaterDto> getTheaterById(@PathVariable Long id) {
    return ResponseEntity.ok(theaterService.getTheaterById(id));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteTheaterById(@PathVariable Long id) {
    theaterService.deleteTheaterById(id);
    return ResponseEntity.noContent().build();
  }

  @PutMapping
  public ResponseEntity<TheaterDto> updateCinemaById(@RequestBody TheaterDto theaterDto) {
    return ResponseEntity.ok(theaterService.updateTheaterById(theaterDto));
  }
}
