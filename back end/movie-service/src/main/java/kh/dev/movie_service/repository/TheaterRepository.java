package kh.dev.movie_service.repository;

import kh.dev.movie_service.model.entity.Theater;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TheaterRepository extends JpaRepository<Theater, Long> {}
