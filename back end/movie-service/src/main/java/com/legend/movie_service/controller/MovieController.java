package com.legend.movie_service.controller;

import com.legend.movie_service.dto.request.MovieRequest;
import com.legend.movie_service.dto.response.MovieResponse;
import com.legend.movie_service.service.MovieService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/movies")
@RequiredArgsConstructor
public class MovieController {

  private final MovieService movieService;

  @PostMapping
  public ResponseEntity<MovieResponse> addMovie(@RequestBody MovieRequest movieRequest) {
    return ResponseEntity.ok(movieService.addMovie(movieRequest));
  }

  @GetMapping("/{id}")
  public ResponseEntity<MovieResponse> getMovieById(@PathVariable Long id) {
    return ResponseEntity.ok(movieService.getMovieById(id));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteMovieById(@PathVariable Long id) {
    movieService.deleteMovieById(id);
    return ResponseEntity.noContent().build();
  }

  @PutMapping("/{id}")
  public ResponseEntity<MovieResponse> updateMovieById(
      @PathVariable Long id, @RequestBody MovieRequest movieRequest) {
    return ResponseEntity.ok(movieService.updateMovie(id, movieRequest));
  }
}
