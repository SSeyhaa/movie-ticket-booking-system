package com.legend.user_service.service;

import com.legend.common_util.dto.request.PdfFormRequest;
import com.legend.common_util.dto.request.PdfPageRequest;
import com.legend.common_util.exception.PdfProcessingException;
import com.legend.common_util.util.PdfFormUtils;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

@Service
public class PdfService {

  public Resource createPdfFillForms(PdfFormRequest pdfFormRequest) {
    File file = new File(pdfFormRequest.getPdfPath());

    try (PDDocument document = Loader.loadPDF(file)) {

      for (PdfPageRequest pageRequest : pdfFormRequest.getPages()) {
        processPageForms(document, pageRequest);
      }

      ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
      document.save(outputStream);
      return new ByteArrayResource(outputStream.toByteArray());

    } catch (IOException e) {
      throw new PdfProcessingException("Error processing PDF form creation", e);
    }
  }

  public Resource fillPdfForm(PdfFormRequest pdfFormRequest) {
    File file = new File(pdfFormRequest.getPdfPath());
    try (PDDocument document = Loader.loadPDF(file);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {

      for (PdfPageRequest pageRequest : pdfFormRequest.getPages()) {
        processFillingPageForms(document, pageRequest);
      }

      document.save(outputStream);
      return new ByteArrayResource(outputStream.toByteArray());

    } catch (IOException e) {
      throw new PdfProcessingException("Error processing PDF form filling", e);
    }
  }

  private void processPageForms(PDDocument document, PdfPageRequest pageRequest) {
    int pageNo = pageRequest.getPageNo();

    pageRequest
        .getTextFields()
        .forEach(field -> PdfFormUtils.addFormTextBox(document, pageNo, field));

    pageRequest
        .getCheckboxes()
        .forEach(checkbox -> PdfFormUtils.addFormCheckbox(document, pageNo, checkbox));

    pageRequest
        .getRadioButtons()
        .forEach(radioButton -> PdfFormUtils.addFormRadio(document, pageNo, radioButton));
  }

  private void processFillingPageForms(PDDocument document, PdfPageRequest pageRequest) {
    pageRequest
        .getTextFields()
        .forEach(
            field ->
                PdfFormUtils.fillPdfForm(document, field.getFieldName(), field.getFieldValue()));

    pageRequest
        .getCheckboxes()
        .forEach(
            checkbox ->
                PdfFormUtils.fillPdfForm(
                    document, checkbox.getFieldName(), checkbox.getFieldValue()));

    pageRequest
        .getRadioButtons()
        .forEach(
            radioButton ->
                PdfFormUtils.fillPdfForm(
                    document, radioButton.getFieldName(), radioButton.getFieldValue()));
  }
}
