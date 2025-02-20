package com.legend.common_util.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class PdfFormTextField {

  private String fieldName;
  private String fieldValue;
  private Rectangle rectangle;
}
