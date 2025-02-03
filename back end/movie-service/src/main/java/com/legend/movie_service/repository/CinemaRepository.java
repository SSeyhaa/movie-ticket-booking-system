package com.legend.movie_service.repository;

import com.legend.movie_service.model.entity.Cinema;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CinemaRepository extends JpaRepository<Cinema, Long> {}
