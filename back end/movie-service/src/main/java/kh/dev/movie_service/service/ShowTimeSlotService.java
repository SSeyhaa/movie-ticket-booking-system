package kh.dev.movie_service.service;

import java.util.Objects;
import kh.dev.movie_service.exception.ResourceNotFoundException;
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

  public ShowTimeSlotResponse addShowTimeSlot(ShowTimeSlotRequest showTimeSlotRequest) {

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

    ShowTimeSlot showTimeSlot = buildShowTimeSlot(cinema, theater, showTime, showTimeSlotRequest);
    ShowTimeSlot showTimeSlotSaved = showTimeSlotRepository.save(showTimeSlot);

    return buildShowTimeSlotResponse(showTimeSlotSaved);
  }

  private ShowTimeSlot buildShowTimeSlot(
      Cinema cinema, Theater theater, ShowTime showTime, ShowTimeSlotRequest showTimeSlotRequest) {
    ShowTimeSlot showTimeSlot = new ShowTimeSlot();
    showTimeSlot.setCinema(cinema);
    showTimeSlot.setTheater(theater);
    showTimeSlot.setShowTime(showTime);
    showTimeSlot.setStartTime(showTimeSlotRequest.getStartTime());
    showTimeSlot.setEndTime(showTimeSlotRequest.getEndTime());
    return showTimeSlot;
  }

  private ShowTimeSlotResponse buildShowTimeSlotResponse(ShowTimeSlot showTimeSlot) {

    ShowTimeResponse showTime =
        ShowTimeResponse.builder()
            .id(showTimeSlot.getShowTime().getId())
            .movieTitle(showTimeSlot.getShowTime().getMovie().getTitle())
            .date(showTimeSlot.getShowTime().getDate())
            .build();

    return ShowTimeSlotResponse.builder()
        .id(showTimeSlot.getId())
        .cinemaName(showTimeSlot.getCinema().getName())
        .theaterName(showTimeSlot.getTheater().getName())
        .showTime(showTime)
        .startTime(showTimeSlot.getStartTime())
        .endTime(showTimeSlot.getEndTime())
        .build();
  }
}
