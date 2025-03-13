package kh.dev.movie_service.controller;

import kh.dev.common_util.dto.request.PaginationRequest;
import kh.dev.movie_service.model.dto.CinemaDto;
import kh.dev.common_util.dto.response.PaginationResponse;
import kh.dev.movie_service.service.CinemaService;
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
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/cinemas")
@RequiredArgsConstructor
public class CinemaController {

  private final CinemaService cinemaService;

  @PostMapping
  public ResponseEntity<CinemaDto> addCinema(@RequestBody CinemaDto cinemaDto) {
    return ResponseEntity.ok(cinemaService.addCinema(cinemaDto));
  }

  @GetMapping
  public ResponseEntity<PaginationResponse<CinemaDto>> getCinemaWithPage(
      @ModelAttribute PaginationRequest paginationRequest) {
    return ResponseEntity.ok(cinemaService.getCinemaWithPage(paginationRequest));
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
