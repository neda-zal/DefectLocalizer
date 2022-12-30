package org.example.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.util.List;
import java.util.Map;
import javax.xml.parsers.ParserConfigurationException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.example.model.Violation;
import org.example.service.FileParsingService;
import org.example.service.ExcelReportGenerationService;
import org.example.service.GitFileParsingService;
import org.example.service.GitService;
import org.example.service.LocaleDetectionService;
import org.example.util.counter.LocaleCounter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;
import org.xml.sax.SAXException;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/")
public class FileUploadController {

  private final FileParsingService fileParsingService;
  private final ExcelReportGenerationService excelReportGenerationService;
  private final LocaleDetectionService localeDetectionService;
  private final GitService gitService;
  private final GitFileParsingService fileScanService;

  @Operation(
      summary = "Upload localized resources",
      description = "This method accepts form data (Excel, CSV, XML and .properties) file containing localized data, parses it and checks for inadequacies for Lithuanian locale."
  )
  @PostMapping(value = "/file/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
  public ResponseEntity<StreamingResponseBody> uploadFiles (
      @Parameter(name = "files", description = "Files containing localized data")
      @RequestPart(name = "files") MultipartFile[] files)
      throws IOException, ParserConfigurationException, SAXException, ParseException {

    List<Violation> violations = this.fileParsingService.scanFiles(files);
    LocaleCounter counter = this.localeDetectionService.detectLocale(files, violations);

    XSSFWorkbook workbook = excelReportGenerationService.generateExcelFile(violations, counter);

    return ResponseEntity.ok()
        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + "Ataskaita.xlsx")
        .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
        .body(workbook::write);
  }

  @Operation(
      summary = "Upload localized resources",
      description = "This method accepts form data (Excel, CSV, XML and .properties) file containing localized data, parses it and checks for inadequacies for Lithuanian locale."
  )
  @PostMapping(value = "/git/upload", produces = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
  public ResponseEntity<StreamingResponseBody> uploadFiles (
      @Parameter(name = "gitURL", description = "Opensource project URL")
      @RequestParam(name = "gitURL") String gitURL)
      throws IOException, ParserConfigurationException, SAXException, ParseException, GitAPIException {

    Map<String, InputStream> files = gitService.parseGitFiles(gitURL);
    List<Violation> violations = this.fileScanService.scanGitFiles(files);
    LocaleCounter counter = this.localeDetectionService.detectLocaleForGitResources(files, violations);

    XSSFWorkbook workbook = excelReportGenerationService.generateExcelFile(violations, counter);

    return ResponseEntity.ok()
        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + "Ataskaita.xlsx")
        .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
        .body(workbook::write);
  }

}
