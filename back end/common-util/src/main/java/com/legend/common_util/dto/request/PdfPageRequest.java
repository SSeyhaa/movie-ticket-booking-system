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
public class PdfPageRequest {
  private int pageNo;
  private List<PdfFormTextField> textFields;
  private List<PdfFormCheckbox> checkboxes;
  private List<PdfFormRadioButton> radioButtons;
}
