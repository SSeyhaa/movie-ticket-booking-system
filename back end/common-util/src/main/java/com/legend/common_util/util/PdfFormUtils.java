package com.legend.common_util.util;

import com.legend.common_util.dto.request.PdfFormCheckbox;
import com.legend.common_util.dto.request.PdfFormRadioButton;
import com.legend.common_util.dto.request.PdfFormTextField;
import com.legend.common_util.dto.request.RadioOption;
import com.legend.common_util.exception.PdfProcessingException;
import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.fontbox.afm.CharMetric;
import org.apache.fontbox.afm.FontMetrics;
import org.apache.fontbox.util.BoundingBox;
import org.apache.pdfbox.cos.COSDictionary;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.pdmodel.PDAppearanceContentStream;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentCatalog;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDResources;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;
import org.apache.pdfbox.pdmodel.font.encoding.GlyphList;
import org.apache.pdfbox.pdmodel.graphics.color.PDColor;
import org.apache.pdfbox.pdmodel.graphics.color.PDDeviceRGB;
import org.apache.pdfbox.pdmodel.interactive.annotation.PDAnnotationWidget;
import org.apache.pdfbox.pdmodel.interactive.annotation.PDAppearanceCharacteristicsDictionary;
import org.apache.pdfbox.pdmodel.interactive.annotation.PDAppearanceDictionary;
import org.apache.pdfbox.pdmodel.interactive.annotation.PDAppearanceEntry;
import org.apache.pdfbox.pdmodel.interactive.annotation.PDAppearanceStream;
import org.apache.pdfbox.pdmodel.interactive.annotation.PDBorderStyleDictionary;
import org.apache.pdfbox.pdmodel.interactive.form.PDAcroForm;
import org.apache.pdfbox.pdmodel.interactive.form.PDCheckBox;
import org.apache.pdfbox.pdmodel.interactive.form.PDComboBox;
import org.apache.pdfbox.pdmodel.interactive.form.PDField;
import org.apache.pdfbox.pdmodel.interactive.form.PDListBox;
import org.apache.pdfbox.pdmodel.interactive.form.PDRadioButton;
import org.apache.pdfbox.pdmodel.interactive.form.PDTextField;
import org.apache.pdfbox.pdmodel.interactive.form.PDVariableText;

@NoArgsConstructor(access = lombok.AccessLevel.PRIVATE)
@Slf4j
public class PdfFormUtils {
  private static final String DEFAULT_APPEARANCE = "/Helv 12 Tf 0 0 1 rg";

  private static PDAcroForm getOrCreateAcroForm(PDDocument document) {
    return Optional.ofNullable(document.getDocumentCatalog().getAcroForm())
        .orElseGet(
            () -> {
              PDAcroForm acroForm = new PDAcroForm(document);
              document.getDocumentCatalog().setAcroForm(acroForm);
              return acroForm;
            });
  }

  public static void addFormTextBox(
      final PDDocument document, int pageNo, PdfFormTextField pdfFormTextField) {

    PDPage page = document.getPage(pageNo);
    PDAcroForm acroForm = getOrCreateAcroForm(document);

    PDTextField textBox = new PDTextField(acroForm);
    textBox.setPartialName(pdfFormTextField.getFieldName());
    textBox.setQ(PDVariableText.QUADDING_CENTERED);
    textBox.setDefaultAppearance(DEFAULT_APPEARANCE);
    acroForm.getFields().add(textBox);

    PDAnnotationWidget widget = textBox.getWidgets().getFirst();
    PDRectangle rect =
        new PDRectangle(
            pdfFormTextField.getRectangle().getX(),
            pdfFormTextField.getRectangle().getY(),
            pdfFormTextField.getRectangle().getWidth(),
            pdfFormTextField.getRectangle().getHeight());
    setupWidget(page, widget, rect);
  }

  public static void addFormCheckbox(
      final PDDocument document, int pageNo, PdfFormCheckbox pdfFormCheckbox) {
    PDPage page = document.getPage(pageNo);
    PDAcroForm acroForm = getOrCreateAcroForm(document);

    PDCheckBox checkbox = new PDCheckBox(acroForm);
    checkbox.setPartialName(pdfFormCheckbox.getFieldName());
    acroForm.getFields().add(checkbox);

    PDAnnotationWidget widget = checkbox.getWidgets().getFirst();
    PDRectangle rect =
        new PDRectangle(
            pdfFormCheckbox.getRectangle().getX(),
            pdfFormCheckbox.getRectangle().getY(),
            pdfFormCheckbox.getRectangle().getWidth(),
            pdfFormCheckbox.getRectangle().getHeight());
    setupWidget(page, widget, rect);

    PDAppearanceDictionary ap = new PDAppearanceDictionary();
    widget.setAppearance(ap);
    PDAppearanceEntry normalAppearance = ap.getNormalAppearance();

    COSDictionary normalAppearanceDict = normalAppearance.getCOSObject();
    PDFont zapfDingbats = new PDType1Font(Standard14Fonts.FontName.ZAPF_DINGBATS);
    normalAppearanceDict.setItem(
        COSName.Off, createCheckboxAppearanceStream(document, widget, false, zapfDingbats));
    normalAppearanceDict.setItem(
        COSName.YES, createCheckboxAppearanceStream(document, widget, true, zapfDingbats));
  }

  private static void setupWidget(PDPage page, PDAnnotationWidget widget, PDRectangle rect) {
    try {
      widget.setPage(page);
      widget.setRectangle(rect);
      widget.setPrinted(true);

      PDBorderStyleDictionary borderStyleDictionary = new PDBorderStyleDictionary();
      borderStyleDictionary.setWidth(1);
      borderStyleDictionary.setStyle(PDBorderStyleDictionary.STYLE_SOLID);
      widget.setBorderStyle(borderStyleDictionary);

      page.getAnnotations().add(widget);
    } catch (IOException e) {
      throw new PdfProcessingException("Error setting up widget", e);
    }
  }

  private static PDAppearanceCharacteristicsDictionary setFieldAppearance() {
    PDAppearanceCharacteristicsDictionary fieldAppearance =
        new PDAppearanceCharacteristicsDictionary(new COSDictionary());
    fieldAppearance.setBorderColour(
        new PDColor(new float[] {0.6f, 0.6f, 0.6f}, PDDeviceRGB.INSTANCE)); // Soft gray border
    fieldAppearance.setBackground(
        new PDColor(
            new float[] {0.9f, 0.95f, 1.0f}, PDDeviceRGB.INSTANCE)); // Light blue background

    return fieldAppearance;
  }

  private static PDAppearanceStream createCheckboxAppearanceStream(
      final PDDocument document, PDAnnotationWidget widget, boolean on, PDFont font) {
    PDRectangle rect = widget.getRectangle();
    PDAppearanceStream yesAP = new PDAppearanceStream(document);
    yesAP.setBBox(new PDRectangle(rect.getWidth(), rect.getHeight()));
    yesAP.setResources(new PDResources());
    try (PDAppearanceContentStream yesAPCS = new PDAppearanceContentStream(yesAP)) {
      PDColor backgroundColor = new PDColor(new float[] {0.6f, 0.6f, 0.6f}, PDDeviceRGB.INSTANCE);
      PDColor borderColor = new PDColor(new float[] {0.9f, 0.95f, 1.0f}, PDDeviceRGB.INSTANCE);
      float lineWidth = getLineWidth(widget);
      yesAPCS.setBorderLine(lineWidth, widget.getBorderStyle(), widget.getBorder());
      yesAPCS.setNonStrokingColor(backgroundColor);
      yesAPCS.addRect(0, 0, rect.getWidth(), rect.getHeight());
      yesAPCS.fill();
      yesAPCS.setStrokingColor(borderColor);
      yesAPCS.addRect(
          lineWidth / 2, lineWidth / 2, rect.getWidth() - lineWidth, rect.getHeight() - lineWidth);
      yesAPCS.stroke();
      if (!on) {
        return yesAP;
      }

      yesAPCS.addRect(
          lineWidth, lineWidth, rect.getWidth() - lineWidth * 2, rect.getHeight() - lineWidth * 2);
      yesAPCS.clip();

      // 8 = cross; 4 = checkmark; H = star; u = diamond; n = square, l = dot
      String normalCaption = "4";
      if ("8".equals(normalCaption)) {
        // Adobe paints a cross instead of using the Zapf Dingbats cross symbol
        yesAPCS.setStrokingColor(0f);
        yesAPCS.moveTo(lineWidth * 2, rect.getHeight() - lineWidth * 2);
        yesAPCS.lineTo(rect.getWidth() - lineWidth * 2, lineWidth * 2);
        yesAPCS.moveTo(rect.getWidth() - lineWidth * 2, rect.getHeight() - lineWidth * 2);
        yesAPCS.lineTo(lineWidth * 2, lineWidth * 2);
        yesAPCS.stroke();
      } else {
        Rectangle2D bounds = new Rectangle2D.Float();
        String unicode = null;

        FontMetrics metric =
            Standard14Fonts.getAFM(Standard14Fonts.FontName.ZAPF_DINGBATS.getName());
        for (CharMetric cm : metric.getCharMetrics()) {
          if (normalCaption.codePointAt(0) == cm.getCharacterCode()) {
            BoundingBox bb = cm.getBoundingBox();
            bounds =
                new Rectangle2D.Float(
                    bb.getLowerLeftX(), bb.getLowerLeftY(), bb.getWidth(), bb.getHeight());
            unicode = GlyphList.getZapfDingbats().toUnicode(cm.getName());
            break;
          }
        }
        if (bounds.isEmpty()) {
          throw new IOException("Bounds rectangle for chosen glyph is empty");
        }
        float size = (float) Math.min(bounds.getWidth(), bounds.getHeight()) / 1000;
        float fontSize = (rect.getWidth() - lineWidth * 2) / size * 0.6666f;
        float xOffset = (float) (rect.getWidth() - (bounds.getWidth()) / 1000 * fontSize) / 2;
        xOffset -= bounds.getX() / 1000 * fontSize;
        float yOffset = (float) (rect.getHeight() - (bounds.getHeight()) / 1000 * fontSize) / 2;
        yOffset -= bounds.getY() / 1000 * fontSize;
        yesAPCS.setNonStrokingColor(0f);
        yesAPCS.beginText();
        yesAPCS.setFont(font, fontSize);
        yesAPCS.newLineAtOffset(xOffset, yOffset);
        yesAPCS.showText(unicode);
        yesAPCS.endText();
      }
    } catch (IOException e) {
      throw new PdfProcessingException("Error creating checkbox appearance stream", e);
    }
    return yesAP;
  }

  public static void addFormRadio(
      final PDDocument document, int pageNo, PdfFormRadioButton pdfFormRadioButton) {
    PDPage page = document.getPage(pageNo);
    PDAcroForm acroForm = getOrCreateAcroForm(document);

    PDRadioButton radioButton = new PDRadioButton(acroForm);
    radioButton.setPartialName(pdfFormRadioButton.getFieldName());

    List<PDAnnotationWidget> widgets = new ArrayList<>();
    try {
      for (RadioOption option : pdfFormRadioButton.getRadioOption()) {
        PDAnnotationWidget widget = new PDAnnotationWidget();
        widget.setRectangle(
            new PDRectangle(
                option.getRectangle().getX(),
                option.getRectangle().getY(),
                option.getRectangle().getWidth(),
                option.getRectangle().getHeight()));
        widget.setPrinted(true);
        widget.setAppearanceCharacteristics(setFieldAppearance());
        PDBorderStyleDictionary borderStyleDictionary = new PDBorderStyleDictionary();
        borderStyleDictionary.setWidth(2);
        borderStyleDictionary.setStyle(PDBorderStyleDictionary.STYLE_SOLID);
        widget.setBorderStyle(borderStyleDictionary);
        widget.setPage(page);

        COSDictionary apNDict = new COSDictionary();
        apNDict.setItem(COSName.Off, createRadioAppearanceStream(document, widget, false));
        apNDict.setItem(option.getName(), createRadioAppearanceStream(document, widget, true));

        PDAppearanceDictionary appearance = new PDAppearanceDictionary();
        PDAppearanceEntry appearanceNEntry = new PDAppearanceEntry(apNDict);
        appearance.setNormalAppearance(appearanceNEntry);
        widget.setAppearance(appearance);
        widget.setAppearanceState("Off");
        widgets.add(widget);

        page.getAnnotations().add(widget);
      }
    } catch (IOException e) {
      throw new PdfProcessingException("Error adding radio button", e);
    }
    radioButton.setWidgets(widgets);

    acroForm.getFields().add(radioButton);
  }

  private static PDAppearanceStream createRadioAppearanceStream(
      final PDDocument document, PDAnnotationWidget widget, boolean on) {
    try {

      PDRectangle rect = widget.getRectangle();
      PDAppearanceStream onAP = new PDAppearanceStream(document);
      onAP.setBBox(new PDRectangle(rect.getWidth(), rect.getHeight()));
      try (PDAppearanceContentStream onAPCS = new PDAppearanceContentStream(onAP)) {
        PDAppearanceCharacteristicsDictionary appearanceCharacteristics =
            widget.getAppearanceCharacteristics();
        PDColor backgroundColor = appearanceCharacteristics.getBackground();
        PDColor borderColor = appearanceCharacteristics.getBorderColour();
        float lineWidth = getLineWidth(widget);
        onAPCS.setBorderLine(lineWidth, widget.getBorderStyle(), widget.getBorder());
        onAPCS.setNonStrokingColor(backgroundColor);
        float radius = Math.min(rect.getWidth() / 2, rect.getHeight() / 2);
        drawCircle(onAPCS, rect.getWidth() / 2, rect.getHeight() / 2, radius);
        onAPCS.fill();
        onAPCS.setStrokingColor(borderColor);
        drawCircle(onAPCS, rect.getWidth() / 2, rect.getHeight() / 2, radius - lineWidth / 2);
        onAPCS.stroke();
        if (on) {
          onAPCS.setNonStrokingColor(0f);
          drawCircle(onAPCS, rect.getWidth() / 2, rect.getHeight() / 2, (radius - lineWidth) / 2);
          onAPCS.fill();
        }
      }
      return onAP;
    } catch (IOException e) {
      throw new PdfProcessingException("Error creating radio appearance stream", e);
    }
  }

  static void drawCircle(PDAppearanceContentStream cs, float x, float y, float r)
      throws IOException {
    // http://stackoverflow.com/a/2007782/535646
    float magic = r * 0.551784f;
    cs.moveTo(x, y + r);
    cs.curveTo(x + magic, y + r, x + r, y + magic, x + r, y);
    cs.curveTo(x + r, y - magic, x + magic, y - r, x, y - r);
    cs.curveTo(x - magic, y - r, x - r, y - magic, x - r, y);
    cs.curveTo(x - r, y + magic, x - magic, y + r, x, y + r);
    cs.closePath();
  }

  static float getLineWidth(PDAnnotationWidget widget) {
    PDBorderStyleDictionary bs = widget.getBorderStyle();
    if (bs != null) {
      return bs.getWidth();
    }
    return 1;
  }

  public static void fillPdfForm(PDDocument pdfDocument, String name, String value) {
    PDDocumentCatalog docCatalog = pdfDocument.getDocumentCatalog();
    PDAcroForm acroForm = docCatalog.getAcroForm();
    PDField field = acroForm.getField(name);
    try {

      switch (field) {
        case PDCheckBox checkbox -> {
          if (value.isEmpty()) {
            checkbox.unCheck();
          } else {
            checkbox.check();
          }
        }
        case PDComboBox pdComboBox -> field.setValue(value);
        case PDListBox pdListBox -> field.setValue(value);
        case PDRadioButton pdRadioButton -> field.setValue(value);
        case PDTextField pdTextField -> field.setValue(value);
        default -> log.warn("Field type not supported: " + field.getClass().getName());
      }
    } catch (IOException e) {
      throw new PdfProcessingException("Error filling form field: " + name, e);
    }
  }
}
