package com.legend.movie_service.service;

import com.legend.common_util.dto.request.PaginationRequest;
import com.legend.common_util.util.DateTimeUtil;
import com.legend.movie_service.dto.request.ShowTimeRequest;
import com.legend.movie_service.dto.response.PaginationResponse;
import com.legend.movie_service.dto.response.ShowTimeResponse;
import com.legend.movie_service.entity.Cinema;
import com.legend.movie_service.entity.Movie;
import com.legend.movie_service.entity.ShowTime;
import com.legend.movie_service.entity.Theater;
import com.legend.movie_service.exception.ResourceNotFoundException;
import com.legend.movie_service.repository.ShowTimeRepository;
import com.legend.movie_service.repository.specification.ShowTimeSpecification;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ShowTimeService {
  private final CinemaService cinemaService;
  private final MovieService movieService;
  private final ShowTimeRepository showTimeRepository;

  public ShowTimeResponse createShowTime(ShowTimeRequest showTimeRequest) {
    Cinema cinema = cinemaService.findCinemaById(showTimeRequest.getCinemaId());

    Theater theater =
        cinema.getTheaters().stream()
            .filter(th -> Objects.equals(th.getId(), showTimeRequest.getTheaterId()))
            .findFirst()
            .orElseThrow(
                () ->
                    new ResourceNotFoundException(
                        "Theater not found with id: " + showTimeRequest.getTheaterId()));

    Movie movie = movieService.findMovieById(showTimeRequest.getMovieId());

    ShowTime showTime = buildShowTime(cinema, theater, movie, showTimeRequest.getDateTime());

    ShowTime showTimeSaved = showTimeRepository.save(showTime);

    return buildShowTimeResponse(showTimeSaved);
  }

  private ShowTimeResponse buildShowTimeResponse(ShowTime showTime) {
    return ShowTimeResponse.builder()
        .id(showTime.getId())
        .cinema(showTime.getCinema().getName())
        .theater(showTime.getTheater().getName())
        .dateTime(showTime.getDateTime())
        .movieTitle(showTime.getMovie().getTitle())
        .build();
  }

  private List<ShowTimeResponse> buildShowTimesResponse(List<ShowTime> showTimes) {
    return showTimes.stream()
        .map(
            showTime ->
                ShowTimeResponse.builder()
                    .id(showTime.getId())
                    .cinema(showTime.getCinema().getName())
                    .theater(showTime.getTheater().getName())
                    .dateTime(showTime.getDateTime())
                    .movieTitle(showTime.getMovie().getTitle())
                    .build())
        .toList();
  }

  private ShowTime buildShowTime(
      Cinema cinema, Theater theater, Movie movie, ZonedDateTime dateTime) {
    ShowTime showTime = new ShowTime();
    showTime.setCinema(cinema);
    showTime.setTheater(theater);
    showTime.setMovie(movie);
    showTime.setDateTime(dateTime);
    return showTime;
  }

  public ShowTimeResponse getShowTimeById(Long id) {
    ShowTime showTime = findShowTimeById(id);
    return buildShowTimeResponse(showTime);
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

    Cinema cinema = cinemaService.findCinemaById(showTimeRequest.getCinemaId());

    Theater theater =
        cinema.getTheaters().stream()
            .filter(th -> Objects.equals(th.getId(), showTimeRequest.getTheaterId()))
            .findFirst()
            .orElseThrow(
                () ->
                    new ResourceNotFoundException(
                        "Theater not found with id: " + showTimeRequest.getTheaterId()));

    showTime.setCinema(cinema);
    showTime.setTheater(theater);
    showTime.setMovie(movie);
    showTime.setDateTime(showTimeRequest.getDateTime());
    ShowTime showTimeUpdated = showTimeRepository.save(showTime);
    return buildShowTimeResponse(showTimeUpdated);
  }

  public PaginationResponse<ShowTimeResponse> getShowTimesWithFilters(
      PaginationRequest paginationRequest,
      Optional<String> cinema,
      Optional<String> theater,
      ZonedDateTime dateTime,
      Optional<String> movieTitle) {

    Pageable pageable =
        PageRequest.of(
            paginationRequest.getPageNumber() - 1,
            paginationRequest.getPageSize(),
            Sort.by(
                Sort.Direction.fromString(paginationRequest.getSortDirection()),
                paginationRequest.getSortBy()));

    Specification<ShowTime> specification = Specification.allOf();
    if (cinema.isPresent()) {
      specification = specification.and(ShowTimeSpecification.hasCinema(cinema.get()));
    }

    if (theater.isPresent()) {
      specification = specification.and(ShowTimeSpecification.hasTheater(theater.get()));
    }

    if (Objects.nonNull(dateTime)) {
      LocalDateTime dateTimeLocal = dateTime.toLocalDateTime();

      if (DateTimeUtil.isDateExcludeTime(dateTimeLocal)) {
        specification =
            specification.and(ShowTimeSpecification.hasDate(dateTimeLocal.toLocalDate()));
      } else if (DateTimeUtil.isDateIncludeTime(dateTimeLocal)) {
        specification = specification.and(ShowTimeSpecification.hasDateTime(dateTimeLocal));
      }
    }

    if (movieTitle.isPresent()) {
      specification = specification.and(ShowTimeSpecification.hasMovieTile(movieTitle.get()));
    }

    Page<ShowTime> pageShowTime = showTimeRepository.findAll(specification, pageable);
    return PaginationResponse.<ShowTimeResponse>builder()
        .pageNumber(paginationRequest.getPageNumber())
        .pageSize(paginationRequest.getPageSize())
        .totalElements(pageShowTime.getTotalElements())
        .totalPages(pageShowTime.getTotalPages())
        .isFirst(pageShowTime.isFirst())
        .isLast(pageShowTime.isLast())
        .isEmpty(pageShowTime.isEmpty())
        .content(buildShowTimesResponse(pageShowTime.getContent()))
        .build();
  }
}
