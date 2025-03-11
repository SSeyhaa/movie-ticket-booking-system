package kh.dev.movie_service.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import kh.dev.common_util.constant.SeatType;
import kh.dev.common_util.dto.SeatDto;
import kh.dev.common_util.dto.request.PaginationRequest;
import kh.dev.common_util.exception.ResourceAlreadyExistsException;
import kh.dev.common_util.file.csv.CSVService;
import kh.dev.common_util.util.PaginationUtils;
import kh.dev.movie_service.file.SeatCSVRepresentation;
import kh.dev.movie_service.model.dto.TheaterSeatsDto;
import kh.dev.movie_service.model.dto.response.PaginationResponse;
import kh.dev.movie_service.model.entity.Seat;
import kh.dev.movie_service.model.entity.Theater;
import kh.dev.movie_service.repository.SeatRepository;
import kh.dev.movie_service.repository.specification.SeatSpecification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class SeatService {

  private final ModelMapper modelMapper;
  private final SeatRepository seatRepository;
  private final TheaterService theaterService;
  private final CSVService<SeatDto, SeatCSVRepresentation> csvService;

  public TheaterSeatsDto createSeatByTheater(TheaterSeatsDto theaterSeatsDto) {

    List<Seat> savedSeats = saveSeats(theaterSeatsDto.getTheaterId(), theaterSeatsDto.getSeats());
    theaterSeatsDto.setSeats(new HashSet<>(convertToSeatDto(savedSeats)));
    return theaterSeatsDto;
  }

  private List<Seat> saveSeats(Long theaterId, Set<SeatDto> seatsDto) {
    Theater theater = theaterService.findTheaterById(theaterId);

    validateSeatExists(theaterId, seatsDto);

    Set<Seat> seats = convertToSeat(seatsDto);
    seats.forEach(seat -> seat.setTheater(theater));

    return seatRepository.saveAll(seats);
  }

  public void importSeatCSV(Long theaterId, InputStream inputStream) throws IOException {
    Set<SeatDto> seatsImported = csvService.parseCSV(inputStream, SeatCSVRepresentation.class);
    saveSeats(theaterId, seatsImported);
  }

  private void validateSeatExists(Long theaterId, Set<SeatDto> seatsDto) {
    List<Seat> seatsExists = findByTheaterIdRowSeatNo(theaterId, seatsDto);
    if (!seatsExists.isEmpty()) {
      List<String> seats =
          seatsExists.stream().map(seat -> seat.getRowLabel() + seat.getSeatNumber()).toList();
      throw new ResourceAlreadyExistsException(String.format("Seat %s already exists", seats));
    }
  }

  private List<Seat> findByTheaterIdRowSeatNo(Long theaterId, Set<SeatDto> seats) {
    Set<String> rowLabelSet = seats.stream().map(SeatDto::getRowLabel).collect(Collectors.toSet());
    Set<Integer> seatNumberSet =
        seats.stream().map(SeatDto::getSeatNumber).collect(Collectors.toSet());
    return seatRepository.findByTheaterIdAndRowLabelInAndSeatNumberIn(
        theaterId, rowLabelSet, seatNumberSet);
  }

  private Set<Seat> convertToSeat(Set<SeatDto> seatDto) {
    return seatDto.stream()
        .map(seat -> modelMapper.map(seat, Seat.class))
        .collect(Collectors.toSet());
  }

  public PaginationResponse<SeatDto> getSeats(
      PaginationRequest paginationRequest,
      Optional<Long> theaterId,
      Optional<SeatType> seatType,
      Optional<Boolean> isActive) {

    Pageable pageable = PaginationUtils.buildPageable(paginationRequest);

    Specification<Seat> specification = Specification.allOf();

    if (theaterId.isPresent()) {
      specification = specification.and(SeatSpecification.hasTheaterId(theaterId.get()));
    }

    if (seatType.isPresent()) {
      specification = specification.and(SeatSpecification.hasSeatType(seatType.get()));
    }

    if (isActive.isPresent()) {
      specification = specification.and(SeatSpecification.hasActive(isActive.get()));
    }

    Page<Seat> seatPages = seatRepository.findAll(specification, pageable);

    return PaginationResponse.<SeatDto>builder()
        .pageNumber(paginationRequest.getPageNumber())
        .pageSize(paginationRequest.getPageSize())
        .totalElements(seatPages.getTotalElements())
        .totalPages(seatPages.getTotalPages())
        .isFirst(seatPages.isFirst())
        .isLast(seatPages.isLast())
        .isEmpty(seatPages.isEmpty())
        .content(convertToSeatDto(seatPages.getContent()))
        .build();
  }

  private List<SeatDto> convertToSeatDto(List<Seat> seats) {
    return seats.stream().map(seat -> modelMapper.map(seat, SeatDto.class)).toList();
  }

  public void updateSeat(TheaterSeatsDto theaterSeatsDto) {

    List<Seat> previousSeats = findByTheaterIdSeatIds(theaterSeatsDto);

    previousSeats.forEach(
        seat ->
            getSeatDtoById(theaterSeatsDto, seat.getId())
                .ifPresent(seatPresent -> modelMapper.map(seatPresent, seat)));

    seatRepository.saveAll(previousSeats);
  }

  private Optional<SeatDto> getSeatDtoById(TheaterSeatsDto theaterSeatsDto, Long seatId) {
    return theaterSeatsDto.getSeats().stream()
        .filter(seatFilter -> seatId.equals(seatFilter.getId()))
        .findFirst();
  }

  public void deleteSeatsByTheaterId(TheaterSeatsDto theaterSeatsDto) {

    List<Seat> seatsExists = findByTheaterIdSeatIds(theaterSeatsDto);
    if (!seatsExists.isEmpty()) {
      seatsExists.forEach(seat -> seat.setTheater(null));
      seatRepository.deleteAll(seatsExists);
      log.info("Seat {} are deleted", seatsExists);
    }
  }

  private List<Seat> findByTheaterIdSeatIds(TheaterSeatsDto theaterSeatsDto) {
    Set<Long> ids =
        theaterSeatsDto.getSeats().stream().map(SeatDto::getId).collect(Collectors.toSet());
    return seatRepository.findByTheaterIdAndIdIn(theaterSeatsDto.getTheaterId(), ids);
  }
}
