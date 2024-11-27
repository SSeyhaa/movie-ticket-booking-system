package com.legend.movie_service.repository.specification;

import com.legend.movie_service.entity.Movie;
import com.legend.movie_service.entity.ShowTime;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ShowTimeSpecification {

  public static Specification<ShowTime> hasMovieTile(String title) {
    return (Root<ShowTime> root, CriteriaQuery<?> query, CriteriaBuilder cb) ->
        cb.like(
            cb.lower(root.get(ShowTime.Fields.movie).get(Movie.Fields.title)),
            "%" + title.toLowerCase() + "%");
  }

  public static Specification<ShowTime> hasCinema(String cinema) {
    return (Root<ShowTime> root, CriteriaQuery<?> query, CriteriaBuilder cb) ->
        cb.like(cb.lower(root.get(ShowTime.Fields.cinema)), "%" + cinema.toLowerCase() + "%");
  }

  public static Specification<ShowTime> hasDate(LocalDate dateTime) {
    return (Root<ShowTime> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {
      // Start of the day (midnight)
      LocalDateTime startOfDay = dateTime.atStartOfDay();
      // End of the day (23:59:59.999999999)
      LocalDateTime endOfDay = dateTime.atTime(LocalTime.MAX);
      // checks if the dateTime is between the start and end of the day
      return cb.between(root.get(ShowTime.Fields.dateTime), startOfDay, endOfDay);
    };
  }

  public static Specification<ShowTime> hasDateTime(LocalDateTime dateTime) {
    return (Root<ShowTime> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {
      LocalDate endOfDayDate = dateTime.toLocalDate();
      LocalDateTime endOfDay = endOfDayDate.atTime(LocalTime.MAX);
      return cb.between(root.get(ShowTime.Fields.dateTime), dateTime, endOfDay);
    };
  }
}
