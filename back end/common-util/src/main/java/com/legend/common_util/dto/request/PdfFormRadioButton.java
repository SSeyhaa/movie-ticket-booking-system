package com.legend.common_util.dto.request;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class PdfFormRadioButton {
  private String fieldName;
  private String fieldValue;
  private List<RadioOption> radioOption;
}
