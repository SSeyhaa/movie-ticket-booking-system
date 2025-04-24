package kh.dev.movie_service.service;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import kh.dev.movie_service.exception.ResourceNotFoundException;
import kh.dev.movie_service.model.dto.TimeSlot;
import kh.dev.movie_service.model.dto.request.ShowTimeSlotRequest;
import kh.dev.movie_service.model.dto.response.ShowTimeResponse;
import kh.dev.movie_service.model.dto.response.ShowTimeSlotResponse;
import kh.dev.movie_service.model.entity.Cinema;
import kh.dev.movie_service.model.entity.ShowTime;
import kh.dev.movie_service.model.entity.ShowTimeSlot;
import kh.dev.movie_service.model.entity.Theater;
import kh.dev.movie_service.repository.CinemaRepository;
import kh.dev.movie_service.repository.ShowTimeSlotRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ShowTimeSlotService {

  private final CinemaRepository cinemaRepository;
  private final ShowTimeService showTimeService;
  private final ShowTimeSlotRepository showTimeSlotRepository;

  public ShowTimeSlotResponse addShowTimeSlots(ShowTimeSlotRequest showTimeSlotRequest) {

    // todo: refactor fetching
    Cinema cinema =
        cinemaRepository
            .findByIdAndTheatersId(
                showTimeSlotRequest.getCinemaId(), showTimeSlotRequest.getTheaterId())
            .orElseThrow(() -> new ResourceNotFoundException("Cinema or Theater not found"));

    Theater theater =
        cinema.getTheaters().stream()
            .filter(th -> Objects.equals(th.getId(), showTimeSlotRequest.getTheaterId()))
            .findFirst()
            .orElseThrow(() -> new ResourceNotFoundException("Theater not found"));

    ShowTime showTime = showTimeService.findShowTimeById(showTimeSlotRequest.getShowTimeId());

    List<TimeSlot> timeSlots = showTimeSlotRequest.getTimeSlot();
    List<ShowTimeSlot> showTimeSlot = buildShowTimeSlots(cinema, theater, showTime, timeSlots);
    List<ShowTimeSlot> showTimeSlotsSaved = showTimeSlotRepository.saveAll(showTimeSlot);

    return buildShowTimeSlotResponse(showTimeSlotsSaved);
  }

  public ShowTimeSlotResponse getShowTimeSlots(Long cinemaId, Long theaterId, Long showTimeId) {

    List<ShowTimeSlot> showTimeSlots =
        showTimeSlotRepository.findAllByCinemaIdAndTheaterIdAndShowTimeId(
            cinemaId, theaterId, showTimeId);

    if (showTimeSlots.isEmpty()) {
      throw new ResourceNotFoundException("ShowTimeSlot not found");
    }

    return buildShowTimeSlotResponse(showTimeSlots);
  }

  public void deleteShowTimeSlots(Set<Long> showTimeSlotIds) {
    showTimeSlotRepository.deleteAllById(showTimeSlotIds);
  }

  private ShowTimeSlot findShowTimeSlotById(Long showTimeSlotId) {
    return showTimeSlotRepository
        .findById(showTimeSlotId)
        .orElseThrow(() -> new ResourceNotFoundException("ShowTimeSlot not found"));
  }

  private List<TimeSlot> mapToTimeSlots(List<ShowTimeSlot> showTimeSlots) {
    return showTimeSlots.stream().map(this::mapToTimeSlot).toList();
  }

  private TimeSlot mapToTimeSlot(ShowTimeSlot showTimeSlot) {
    return new TimeSlot(
        showTimeSlot.getId(), showTimeSlot.getStartTime(), showTimeSlot.getEndTime());
  }

  private List<ShowTimeSlot> buildShowTimeSlots(
      Cinema cinema, Theater theater, ShowTime showTime, List<TimeSlot> timeSlots) {

    return timeSlots.stream()
        .map(timeSlot -> buildShowTimeSlot(cinema, theater, showTime, timeSlot))
        .toList();
  }

  private ShowTimeSlot buildShowTimeSlot(
      Cinema cinema, Theater theater, ShowTime showTime, TimeSlot timeSlot) {

    ShowTimeSlot showTimeSlot = new ShowTimeSlot();
    showTimeSlot.setCinema(cinema);
    showTimeSlot.setTheater(theater);
    showTimeSlot.setShowTime(showTime);
    showTimeSlot.setStartTime(timeSlot.getStartTime());
    showTimeSlot.setEndTime(timeSlot.getEndTime());
    return showTimeSlot;
  }

  private ShowTimeSlotResponse buildShowTimeSlotResponse(List<ShowTimeSlot> showTimeSlots) {

    ShowTimeSlot showTimeSlotFirst = showTimeSlots.getFirst();

    ShowTimeResponse showTime =
        buildShowTimeResponse(
            showTimeSlotFirst.getShowTime().getId(),
            showTimeSlotFirst.getShowTime().getMovie().getTitle(),
            showTimeSlotFirst.getShowTime().getDate());

    List<TimeSlot> timeSlots = mapToTimeSlots(showTimeSlots);

    return ShowTimeSlotResponse.builder()
        .cinemaName(showTimeSlotFirst.getCinema().getName())
        .theaterName(showTimeSlotFirst.getTheater().getName())
        .showTime(showTime)
        .timeSlots(timeSlots)
        .build();
  }

  private ShowTimeResponse buildShowTimeResponse(
      Long showTimeId, String movieTitle, ZonedDateTime date) {
    return ShowTimeResponse.builder().id(showTimeId).movieTitle(movieTitle).date(date).build();
  }
}
