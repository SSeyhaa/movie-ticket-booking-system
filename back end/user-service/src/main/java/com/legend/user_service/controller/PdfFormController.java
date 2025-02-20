package com.legend.user_service.controller;

import com.legend.common_util.dto.request.PdfFormRequest;
import com.legend.user_service.service.PdfService;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/pdf-form")
public class PdfFormController {

  private final PdfService pdfService;

  @PostMapping("/form")
  public ResponseEntity<Resource> createPdfForm(@RequestBody PdfFormRequest pdfFormRequest)
      throws IOException {
    Resource resource = pdfService.createPdfFillForms(pdfFormRequest);
    return ResponseEntity.ok()
        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=pdf-form.pdf")
        .contentLength(resource.contentLength())
        .contentType(MediaType.APPLICATION_OCTET_STREAM)
        .body(resource);
  }

  @PostMapping("/fill")
  public ResponseEntity<Resource> fillPdfForm(@RequestBody PdfFormRequest pdfFormRequest)
      throws IOException {
    Resource resource = pdfService.fillPdfForm(pdfFormRequest);
    return ResponseEntity.ok()
        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=pdf-filled.pdf")
        .contentLength(resource.contentLength())
        .contentType(MediaType.APPLICATION_OCTET_STREAM)
        .body(resource);
  }
}
