package kh.dev.common_util.util;

import kh.dev.common_util.dto.request.PaginationRequest;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@NoArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public class PaginationUtils {

  public static Pageable buildPageable(PaginationRequest paginationRequest) {
    return PageRequest.of(
        paginationRequest.getPageNumber() - 1,
        paginationRequest.getPageSize(),
        Sort.by(
            Sort.Direction.fromString(paginationRequest.getSortDirection()),
            paginationRequest.getSortBy()));
  }
}
