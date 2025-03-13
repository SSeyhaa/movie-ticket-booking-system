package kh.dev.common_util.dto.response;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class PaginationResponse<T> {
  private int pageNumber;
  private int pageSize;
  private long totalElements;
  private int totalPages;
  private boolean isFirst;
  private boolean isLast;
  private boolean isEmpty;

  private List<T> content;
}
