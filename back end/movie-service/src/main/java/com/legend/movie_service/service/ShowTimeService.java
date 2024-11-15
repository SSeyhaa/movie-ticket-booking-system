package com.legend.movie_service.service;

import com.legend.movie_service.dto.request.ShowTimeRequest;
import com.legend.movie_service.dto.response.ShowTimeResponse;
import com.legend.movie_service.entity.Movie;
import com.legend.movie_service.entity.ShowTime;
import com.legend.movie_service.exception.ResourceNotFoundException;
import com.legend.movie_service.repository.ShowTimeRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ShowTimeService {
  private final ModelMapper modelMapper;
  private final MovieService movieService;
  private final ShowTimeRepository showTimeRepository;

  public ShowTimeResponse createShowTime(Long movieId, ShowTimeRequest showTimeRequest) {
    Movie movie = movieService.findMovieById(movieId);
    ShowTime showTime = modelMapper.map(showTimeRequest, ShowTime.class);
    showTime.setMovie(movie);
    return modelMapper.map(showTimeRepository.save(showTime), ShowTimeResponse.class);
  }

  public ShowTimeResponse getShowTimeById(Long id) {
    ShowTime showTime = findShowTimeById(id);
    return modelMapper.map(showTimeRepository.save(showTime), ShowTimeResponse.class);
  }

  public void deleteShowTimeById(Long id) {
    ShowTime showTime = findShowTimeById(id);
    showTimeRepository.delete(showTime);
  }

  private ShowTime findShowTimeById(Long id) {
    return showTimeRepository
        .findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Show Time not found with id: " + id));
  }

  public ShowTimeResponse updateShowTime(Long id, ShowTimeRequest showTimeRequest) {
    Movie movie = movieService.findMovieById(showTimeRequest.getMovieId());
    ShowTime showTime = findShowTimeById(id);
    modelMapper.map(showTimeRequest, showTime);
    showTime.setMovie(movie);
    ShowTime updatedShowTime = showTimeRepository.save(showTime);
    return modelMapper.map(updatedShowTime, ShowTimeResponse.class);
  }
}
