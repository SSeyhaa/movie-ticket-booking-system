package kh.dev.movie_service.repository;

import java.util.List;
import java.util.Set;
import kh.dev.movie_service.model.entity.Seat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface SeatRepository extends JpaRepository<Seat, Long>, JpaSpecificationExecutor<Seat> {

  List<Seat> findByTheaterIdAndRowLabelInAndSeatNumberIn(
      Long theaterId, Set<String> rowLabel, Set<Integer> seatNumber);

  List<Seat> findByTheaterIdAndIdIn(Long theaterId, Set<Long> ids);
}
