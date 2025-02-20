package kh.dev.movie_service.repository;

import kh.dev.movie_service.model.entity.ShowTime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ShowTimeRepository
    extends JpaRepository<ShowTime, Long>, JpaSpecificationExecutor<ShowTime> {}
