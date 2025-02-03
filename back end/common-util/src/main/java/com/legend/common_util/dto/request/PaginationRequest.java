package com.legend.common_util.dto.request;

import com.legend.common_util.constant.CommonParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaginationRequest {
  @Schema(defaultValue = "1", description = "Page number, starts from 1")
  private int pageNumber = CommonParam.ONE;

  @Schema(defaultValue = "10", description = "Number of items per page")
  private int pageSize = CommonParam.TEN;

  @Schema(defaultValue = "id", description = "Field to sort by")
  private String sortBy = CommonParam.ID;

  @Schema(defaultValue = "DESC", description = "Sorting direction: ASC or DESC")
  private String sortDirection = CommonParam.SORT_DIRECTION_DESC;
}
