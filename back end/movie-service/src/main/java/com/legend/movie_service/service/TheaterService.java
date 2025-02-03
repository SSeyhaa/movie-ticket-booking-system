package com.legend.movie_service.service;

import com.legend.common_util.dto.request.PaginationRequest;
import com.legend.movie_service.model.dto.TheaterDto;
import com.legend.movie_service.model.dto.response.PaginationResponse;
import com.legend.movie_service.model.entity.Cinema;
import com.legend.movie_service.model.entity.Theater;
import com.legend.movie_service.exception.ResourceNotFoundException;
import com.legend.movie_service.repository.TheaterRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TheaterService {
  private final ModelMapper modelMapper;
  private final TheaterRepository theaterRepository;
  private final CinemaService cinemaService;

  public TheaterDto addTheater(Long cinemaId, TheaterDto theaterDto) {
    Cinema cinema = cinemaService.findCinemaById(cinemaId);
    Theater theater = modelMapper.map(theaterDto, Theater.class);
    cinema.getTheaters().add(theater);
    theater.setCinema(cinema);
    return modelMapper.map(theaterRepository.save(theater), TheaterDto.class);
  }

  private Theater findTheaterById(Long id) {
    return theaterRepository
        .findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Theater not found with id: " + id));
  }

  public TheaterDto getTheaterById(Long id) {
    Theater theater = findTheaterById(id);
    return modelMapper.map(theater, TheaterDto.class);
  }

  public void deleteTheaterById(Long id) {
    Theater theater = findTheaterById(id);
    theaterRepository.delete(theater);
  }

  public TheaterDto updateTheaterById(Long id, TheaterDto theaterDto) {
    Theater theater = findTheaterById(id);
    theater.setName(theaterDto.getName());
    theaterRepository.save(theater);
    return modelMapper.map(theater, TheaterDto.class);
  }

  public PaginationResponse<TheaterDto> getTheaterWithPage(PaginationRequest paginationRequest) {
    Pageable pageable =
        PageRequest.of(
            paginationRequest.getPageNumber() - 1,
            paginationRequest.getPageSize(),
            Sort.by(
                Sort.Direction.fromString(paginationRequest.getSortDirection()),
                paginationRequest.getSortBy()));

    Page<Theater> theaterPages = theaterRepository.findAll(pageable);
    return PaginationResponse.<TheaterDto>builder()
        .pageNumber(paginationRequest.getPageNumber())
        .pageSize(paginationRequest.getPageSize())
        .totalElements(theaterPages.getTotalElements())
        .totalPages(theaterPages.getTotalPages())
        .isFirst(theaterPages.isFirst())
        .isLast(theaterPages.isLast())
        .isEmpty(theaterPages.isEmpty())
        .content(mapToTheatersDto(theaterPages.getContent()))
        .build();
  }

  private List<TheaterDto> mapToTheatersDto(List<Theater> theaters) {
    return theaters.stream().map(theater -> modelMapper.map(theater, TheaterDto.class)).toList();
  }
}
