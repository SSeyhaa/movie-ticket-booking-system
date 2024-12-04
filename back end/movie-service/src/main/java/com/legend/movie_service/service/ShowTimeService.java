package com.legend.movie_service.service;

import com.legend.common_util.util.DateTimeUtil;
import com.legend.movie_service.dto.request.ShowTimeRequest;
import com.legend.movie_service.dto.response.PaginationResponse;
import com.legend.movie_service.dto.response.ShowTimeResponse;
import com.legend.movie_service.entity.Movie;
import com.legend.movie_service.entity.ShowTime;
import com.legend.movie_service.exception.ResourceNotFoundException;
import com.legend.movie_service.repository.ShowTimeRepository;
import com.legend.movie_service.repository.specification.ShowTimeSpecification;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Objects;
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

  public PaginationResponse<ShowTimeResponse> getShowTimesWithFilters(
      int pageNumber,
      int pageSize,
      String sortBy,
      String sortDirection,
      String cinema,
      ZonedDateTime dateTime,
      String title) {

    Pageable pageable =
        PageRequest.of(
            pageNumber - 1, pageSize, Sort.by(Sort.Direction.fromString(sortDirection), sortBy));

    Specification<ShowTime> specification = Specification.allOf();
    if (Objects.nonNull(cinema)) {
      specification = specification.and(ShowTimeSpecification.hasCinema(cinema));
    }

    LocalDateTime dateTimeLocal = dateTime.toLocalDateTime();

    if (Objects.nonNull(dateTimeLocal)) {
      if (DateTimeUtil.isDateExcludeTime(dateTimeLocal)) {
        specification = specification.and(ShowTimeSpecification.hasDate(dateTimeLocal.toLocalDate()));
      } else if (DateTimeUtil.isDateIncludeTime(dateTimeLocal)) {
        specification = specification.and(ShowTimeSpecification.hasDateTime(dateTimeLocal));
      }
    }

    if (Objects.nonNull(title)) {
      specification = specification.and(ShowTimeSpecification.hasMovieTile(title));
    }

    Page<ShowTime> pageShowTime = showTimeRepository.findAll(specification, pageable);
    return PaginationResponse.<ShowTimeResponse>builder()
        .pageNumber(pageNumber)
        .pageSize(pageSize)
        .totalElements(pageShowTime.getTotalElements())
        .totalPages(pageShowTime.getTotalPages())
        .isFirst(pageShowTime.isFirst())
        .isLast(pageShowTime.isLast())
        .isEmpty(pageShowTime.isEmpty())
        .content(mapToShowTimeResponses(pageShowTime.getContent()))
        .build();
  }

  private List<ShowTimeResponse> mapToShowTimeResponses(List<ShowTime> showTimes) {
    return showTimes.stream()
        .map(showTime -> modelMapper.map(showTime, ShowTimeResponse.class))
        .toList();
  }
}
