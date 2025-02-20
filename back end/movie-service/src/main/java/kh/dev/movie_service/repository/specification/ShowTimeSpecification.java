package kh.dev.movie_service.repository.specification;

import kh.dev.movie_service.model.entity.Cinema;
import kh.dev.movie_service.model.entity.Movie;
import kh.dev.movie_service.model.entity.ShowTime;
import kh.dev.movie_service.model.entity.Theater;
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
        cb.like(
            cb.lower(root.get(ShowTime.Fields.cinema).get(Cinema.Fields.name)),
            "%" + cinema.toLowerCase() + "%");
  }

  public static Specification<ShowTime> hasTheater(String theater) {
    return (Root<ShowTime> root, CriteriaQuery<?> query, CriteriaBuilder cb) ->
        cb.like(
            cb.lower(root.get(ShowTime.Fields.theater).get(Theater.Fields.name)),
            "%" + theater.toLowerCase() + "%");
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
