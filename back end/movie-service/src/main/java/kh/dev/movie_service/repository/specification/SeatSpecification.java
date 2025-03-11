package kh.dev.movie_service.repository.specification;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import kh.dev.common_util.constant.SeatType;
import kh.dev.movie_service.model.entity.Seat;
import kh.dev.movie_service.model.entity.Theater;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SeatSpecification {

  public static Specification<Seat> hasTheaterId(Long theaterId) {
    return (Root<Seat> root, CriteriaQuery<?> query, CriteriaBuilder cb) ->
        cb.equal(root.get(Seat.Fields.theater).get(Theater.Fields.id), theaterId);
  }

  public static Specification<Seat> hasSeatType(SeatType seatType) {
    return (Root<Seat> root, CriteriaQuery<?> query, CriteriaBuilder cb) ->
        cb.equal(root.get(Seat.Fields.seatType), seatType);
  }

  public static Specification<Seat> hasActive(boolean isActive) {
    return (Root<Seat> root, CriteriaQuery<?> query, CriteriaBuilder cb) ->
        cb.equal(root.get(Seat.Fields.isActive), isActive);
  }
}
