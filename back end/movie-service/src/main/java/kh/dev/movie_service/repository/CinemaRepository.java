package kh.dev.movie_service.repository;

import java.util.Optional;
import kh.dev.movie_service.model.entity.Cinema;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CinemaRepository extends JpaRepository<Cinema, Long> {

  Optional<Cinema> findByIdAndTheatersId(Long cinemaId, Long theaterId);
}
