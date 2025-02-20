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
public class PdfFormRequest {

  private String pdfPath;
  private List<PdfPageRequest> pages;
}
