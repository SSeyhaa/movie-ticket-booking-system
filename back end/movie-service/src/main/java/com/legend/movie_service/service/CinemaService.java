package com.legend.movie_service.service;

import com.legend.common_util.dto.request.PaginationRequest;
import com.legend.movie_service.dto.CinemaDto;
import com.legend.movie_service.dto.response.PaginationResponse;
import com.legend.movie_service.entity.Cinema;
import com.legend.movie_service.exception.ResourceNotFoundException;
import com.legend.movie_service.repository.CinemaRepository;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CinemaService {
  private final ModelMapper modelMapper;
  private final CinemaRepository cinemaRepository;

  public CinemaDto addCinema(CinemaDto cinemaDto) {
    Cinema cinema = modelMapper.map(cinemaDto, Cinema.class);

    if (Objects.nonNull(cinema.getTheaters())) {
      cinema.getTheaters().forEach(theater -> theater.setCinema(cinema));
    }

    Cinema cinemaSaved = cinemaRepository.save(cinema);
    return modelMapper.map(cinemaSaved, CinemaDto.class);
  }

  public Cinema findCinemaById(Long id) {
    return cinemaRepository
        .findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Cinema not found with id: " + id));
  }

  public CinemaDto getCinemaById(Long id) {
    Cinema cinema = findCinemaById(id);
    return modelMapper.map(cinema, CinemaDto.class);
  }

  public void deleteCinemaById(Long id) {
    Cinema cinema = findCinemaById(id);
    cinemaRepository.delete(cinema);
  }

  public CinemaDto updateCinemaById(Long id, CinemaDto cinemaDto) {
    Cinema cinema = findCinemaById(id);
    cinema.setName(cinemaDto.getName());

    Cinema updatedCinema = cinemaRepository.save(cinema);
    return modelMapper.map(updatedCinema, CinemaDto.class);
  }

  public PaginationResponse<CinemaDto> getCinemaWithPage(PaginationRequest paginationRequest) {
    Pageable pageable =
        PageRequest.of(
            paginationRequest.getPageNumber() - 1,
            paginationRequest.getPageSize(),
            Sort.by(
                Sort.Direction.fromString(paginationRequest.getSortDirection()),
                paginationRequest.getSortBy()));

    Page<Cinema> cinemaPages = cinemaRepository.findAll(pageable);
    return PaginationResponse.<CinemaDto>builder()
        .pageNumber(paginationRequest.getPageNumber())
        .pageSize(paginationRequest.getPageSize())
        .totalElements(cinemaPages.getTotalElements())
        .totalPages(cinemaPages.getTotalPages())
        .isFirst(cinemaPages.isFirst())
        .isLast(cinemaPages.isLast())
        .isEmpty(cinemaPages.isEmpty())
        .content(mapToCinemasDto(cinemaPages.getContent()))
        .build();
  }

  private List<CinemaDto> mapToCinemasDto(List<Cinema> cinemas) {
    return cinemas.stream().map(cinema -> modelMapper.map(cinema, CinemaDto.class)).toList();
  }
}
