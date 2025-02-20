package kh.dev.movie_service.repository;

import kh.dev.movie_service.model.entity.Movie;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MovieRepository extends JpaRepository<Movie, Long> {}
