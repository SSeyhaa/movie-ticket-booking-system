package kh.dev.movie_service.repository;

import java.util.List;
import kh.dev.movie_service.model.entity.ShowTimeSlot;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShowTimeSlotRepository extends JpaRepository<ShowTimeSlot, Long> {

  List<ShowTimeSlot> findAllByCinemaIdAndTheaterIdAndShowTimeId(
      Long cinemaId, Long theaterId, Long showTimeId);
}
