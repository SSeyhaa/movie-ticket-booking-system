package kh.dev.movie_service.service;

import kh.dev.movie_service.model.dto.request.MovieRequest;
import kh.dev.movie_service.model.dto.response.MovieResponse;
import kh.dev.movie_service.model.entity.Movie;
import kh.dev.movie_service.exception.ResourceNotFoundException;
import kh.dev.movie_service.repository.MovieRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class MovieService {
  private final ModelMapper modelMapper;
  private final MovieRepository movieRepository;

  public MovieResponse addMovie(MovieRequest movieRequest) {
    Movie movie = modelMapper.map(movieRequest, Movie.class);
    return modelMapper.map(movieRepository.save(movie), MovieResponse.class);
  }

  public MovieResponse getMovieById(Long id) {
    Movie movie = findMovieById(id);
    return modelMapper.map(movieRepository.save(movie), MovieResponse.class);
  }

  public void deleteMovieById(Long id) {
    Movie movie = findMovieById(id);
    movieRepository.delete(movie);
  }

  public Movie findMovieById(Long id) {
    return movieRepository
        .findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Movie not found with id: " + id));
  }

  public MovieResponse updateMovie(Long id, MovieRequest movieRequest) {
    Movie movie = findMovieById(id);
    modelMapper.map(movieRequest, movie);
    Movie updatedMovie = movieRepository.save(movie);
    return modelMapper.map(updatedMovie, MovieResponse.class);
  }
}
