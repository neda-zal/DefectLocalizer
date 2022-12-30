package org.example.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xddf.usermodel.chart.AxisCrossBetween;
import org.apache.poi.xddf.usermodel.chart.AxisCrosses;
import org.apache.poi.xddf.usermodel.chart.AxisPosition;
import org.apache.poi.xddf.usermodel.chart.AxisTickLabelPosition;
import org.apache.poi.xddf.usermodel.chart.BarDirection;
import org.apache.poi.xddf.usermodel.chart.ChartTypes;
import org.apache.poi.xddf.usermodel.chart.LegendPosition;
import org.apache.poi.xddf.usermodel.chart.XDDFBarChartData;
import org.apache.poi.xddf.usermodel.chart.XDDFCategoryAxis;
import org.apache.poi.xddf.usermodel.chart.XDDFChartData;
import org.apache.poi.xddf.usermodel.chart.XDDFChartLegend;
import org.apache.poi.xddf.usermodel.chart.XDDFDataSource;
import org.apache.poi.xddf.usermodel.chart.XDDFDataSourcesFactory;
import org.apache.poi.xddf.usermodel.chart.XDDFNumericalDataSource;
import org.apache.poi.xddf.usermodel.chart.XDDFValueAxis;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFChart;
import org.apache.poi.xssf.usermodel.XSSFClientAnchor;
import org.apache.poi.xssf.usermodel.XSSFDrawing;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.example.model.Violation;
import org.example.util.counter.LocaleCounter;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@AllArgsConstructor
public class ExcelReportGenerationService {

  public XSSFWorkbook generateExcelFile(List<Violation> violations, LocaleCounter counter) {
    ClassPathResource resource = new ClassPathResource("Template.xlsx");
    try {
      XSSFWorkbook workbook = new XSSFWorkbook(resource.getInputStream());
      this.writeLocaleAnalysisReport(counter, workbook);
      this.writeMainViolations(violations, workbook);
      this.writeAnalysisReport(workbook, violations);
      return workbook;
    } catch (IOException exception) {
      log.error(exception.getMessage());
    }
    return null;
  }

  private void writeLocaleAnalysisReport(LocaleCounter counter, XSSFWorkbook workbook) {
    XSSFCellStyle style = this.getBoldedTextStyle(workbook);

    XSSFSheet spreadsheet = workbook.getSheetAt(0);
    spreadsheet.createRow(3).createCell(4).setCellValue("Lietuviškų lokalių:");
    spreadsheet.getRow(3).createCell(5).setCellValue(
        counter.getLithuanian() + "/" + (counter.getLithuanian() + counter.getUnrecognized()));
    spreadsheet.createRow(4).createCell(4).setCellValue("Neatpažintų lokalių:");
    spreadsheet.getRow(4).createCell(5).setCellValue(
        counter.getUnrecognized() + "/" + (counter.getLithuanian() + counter.getUnrecognized()));

    spreadsheet.getRow(3).getCell(4).setCellStyle(style);
    spreadsheet.getRow(4).getCell(4).setCellStyle(style);
  }

  private void writeMainViolations(List<Violation> violations, XSSFWorkbook workbook) {
    XSSFSheet spreadsheet = workbook.getSheetAt(0);
    int rowNum = 10;
    for (Violation violation : violations) {
      Row row = spreadsheet.createRow(rowNum);
      row.createCell(1).setCellValue(violation.getFilename());
      row.createCell(2)
          .setCellValue(violation.getLineId() == null ? "" : violation.getLineId().toString());
      row.createCell(3).setCellValue(violation.getSeverity().getLabel());
      row.createCell(4).setCellValue(violation.getChecklist());
      row.createCell(5).setCellValue(violation.getCreatedAt());
      row.createCell(6).setCellValue(violation.getElimination().getMessage());
      row.createCell(7).setCellValue(violation.getElimination().getResourceUrl() == null ? ""
          : violation.getElimination().getResourceUrl());
      rowNum++;
    }
    for (int i = 10; i < rowNum; i++) {
      XSSFCellStyle style = this.getWrappedTextStyle(workbook);
      spreadsheet.getRow(i).getCell(6).setCellStyle(style);
    }
  }

  private void writeAnalysisReport(XSSFWorkbook workbook, List<Violation> violations) {
    XSSFSheet spreadsheet = workbook.getSheetAt(1);

    XSSFDrawing drawing = spreadsheet.createDrawingPatriarch();
    XSSFClientAnchor anchor = drawing.createAnchor(0, 0, 0, 0, 1, 1, 20, 24);

    XSSFChart chart = drawing.createChart(anchor);
    chart.setTitleText("Klaidų pasitaikymo statistika"); // "Dažniausiai pasikartojančių klaidų analizė"
    chart.setTitleOverlay(false);

    XDDFChartLegend legend = chart.getOrAddLegend();
    legend.setPosition(LegendPosition.TOP_RIGHT);

    XDDFCategoryAxis bottomAxis = chart.createCategoryAxis(AxisPosition.BOTTOM);
    bottomAxis.setTitle("Klaida");
    bottomAxis.setTickLabelPosition(AxisTickLabelPosition.NEXT_TO);
    bottomAxis.setCrosses(AxisCrosses.AUTO_ZERO);

    XDDFValueAxis leftAxis = chart.createValueAxis(AxisPosition.LEFT);
    leftAxis.setTitle("Pasikartojimo dažnis");
    leftAxis.setMinorUnit(1.0);
    leftAxis.setCrossBetween(AxisCrossBetween.BETWEEN);
    leftAxis.setCrosses(AxisCrosses.AUTO_ZERO);

    Map<String, Long> violationsKeyValueList = this.countViolationOccurrences(violations);

    XDDFDataSource<String> inadequacies = XDDFDataSourcesFactory.fromArray(
        violationsKeyValueList.keySet().toArray(String[]::new));

    XDDFNumericalDataSource<Long> values = XDDFDataSourcesFactory.fromArray(
        violationsKeyValueList.values().toArray(Long[]::new));

    XDDFChartData data = chart.createData(ChartTypes.BAR, bottomAxis, leftAxis);
    XDDFChartData.Series series = data.addSeries(inadequacies, values);
    series.setTitle("Klaidos");
    data.setVaryColors(true);
    chart.plot(data);

    XDDFBarChartData bar = (XDDFBarChartData) data;
    bar.setBarDirection(BarDirection.COL);
  }

  private XSSFCellStyle getBoldedTextStyle(XSSFWorkbook workbook) {
    XSSFCellStyle style = workbook.createCellStyle();
    XSSFFont font = workbook.createFont();
    font.setBold(true);
    style.setFont(font);
    return style;
  }

  private XSSFCellStyle getWrappedTextStyle(XSSFWorkbook workbook) {
    XSSFCellStyle style = workbook.createCellStyle();
    style.setWrapText(true);
    return style;
  }

  private Map<String, Long> countViolationOccurrences(List<Violation> violations) {
    return violations.stream()
        .collect(Collectors.groupingBy(Violation::getChecklist,
            Collectors.counting()));
  }

}
