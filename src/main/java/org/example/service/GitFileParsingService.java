package org.example.service;

import static org.example.util.Constants.CSV;
import static org.example.util.Constants.JAVA_LITHUANIAN_LOCALE_ID;
import static org.example.util.Constants.LITHUANIAN_LOCALE;
import static org.example.util.Constants.LITHUANIAN_LOCALE_ID;
import static org.example.util.Constants.PROPERTIES;
import static org.example.util.Constants.XLS;
import static org.example.util.Constants.XLSX;
import static org.example.util.Constants.XML;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicInteger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.example.model.Property;
import org.example.model.Violation;
import org.example.util.string.StringUtil;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

@Slf4j
@Service
@AllArgsConstructor
public class GitFileParsingService {

  private final InadequacyCheckService inadequacyCheckService;

  public List<Violation> scanGitFiles(Map<String, InputStream> files)
      throws IOException, ParserConfigurationException, SAXException {
    List<Violation> violations = new ArrayList<>();

    for (Map.Entry<String, InputStream> file : files.entrySet()) {
      Optional<String> fileExtension = StringUtil.getExtensionByStringHandling(file.getKey());
      List<Property> properties = new ArrayList<>();

      if (fileExtension.isPresent()) {
        String extension = fileExtension.get().toUpperCase();
        this.parseDifferentFileFormats(extension, properties, file);
      }

      violations = this.inadequacyCheckService.checkViolations(properties);

    }
    return violations;
  }

  private void parseDifferentFileFormats(String extension, List<Property> properties,
      Map.Entry<String, InputStream> file)
      throws IOException, ParserConfigurationException, SAXException {
    switch (extension) {
      case PROPERTIES -> this.parsePropertiesFile(properties, file);
      case XML -> this.parseXMLFile(properties, file);
      case CSV -> this.parseCSVFile(properties, file);
      case XLS -> this.parseXLSFile(properties, file);
      case XLSX -> this.parseXLSXFile(properties, file);
      default -> log.error("Palaikomas failo plėtinys nerastas");
    }
  }

  private void parsePropertiesFile(List<Property> fileLines, Map.Entry<String, InputStream> file) {
    Properties properties = new Properties();
    try {
      properties.load(file.getValue());
      AtomicInteger count = new AtomicInteger(1);
      properties.forEach((key, value) -> {
        fileLines.add(
            new Property(key.toString(), value.toString(),
                file.getKey(), count.get()));
        count.getAndIncrement();
      });
    } catch (Exception e) {
      log.error(e.getMessage());
    }
  }

  private void parseCSVFile(List<Property> properties, Map.Entry<String, InputStream> file) throws IOException {
    List<List<String>> records = new ArrayList<>();
    try (BufferedReader br = new BufferedReader(new InputStreamReader(file.getValue()))) {
      String line;
      while ((line = br.readLine()) != null) {
        String[] values = line.split(",");
        records.add(Arrays.asList(values));
      }
    }

    List<String> headers = records.get(0);
    int lithuanianLocaleIndex = headers.indexOf(LITHUANIAN_LOCALE);
    if (lithuanianLocaleIndex == -1)
      lithuanianLocaleIndex = headers.indexOf(LITHUANIAN_LOCALE_ID);
    if (lithuanianLocaleIndex == -1)
      lithuanianLocaleIndex = headers.indexOf(JAVA_LITHUANIAN_LOCALE_ID);

    int count = 1;
    if (lithuanianLocaleIndex != -1) {
      for (List<String> line : records) {
        properties.add(new Property(
            line.get(0), line.get(lithuanianLocaleIndex), file.getKey(), count
        ));
        count++;
      }
    }
  }

  private void parseXLSFile(List<Property> properties, Map.Entry<String, InputStream> file) throws IOException {
    Workbook wb = new HSSFWorkbook(file.getValue());
    wb.setSelectedTab(0);
    Row header = wb.getSheetAt(0).getRow(0);
    short minColIx = header.getFirstCellNum();
    short maxColIx = header.getLastCellNum();
    AtomicInteger lithuanianLocaleIndex = new AtomicInteger(0);
    for (short colIx = minColIx; colIx < maxColIx; colIx++) {
      if (header.getCell(colIx).getStringCellValue().equals(LITHUANIAN_LOCALE)
          || header.getCell(colIx).getStringCellValue().equals(LITHUANIAN_LOCALE_ID)
          || header.getCell(colIx).getStringCellValue().equals(JAVA_LITHUANIAN_LOCALE_ID)) {
        lithuanianLocaleIndex.set(colIx);
      }
    }
    if (lithuanianLocaleIndex.get() != 0) {
      wb.getSheetAt(0).rowIterator().forEachRemaining(row -> {
        if (row.getRowNum() != 0) {
          properties.add(new Property(
              row.getCell(0).getStringCellValue(),
              row.getCell(lithuanianLocaleIndex.get()).getStringCellValue(),
              file.getKey(),
              row.getRowNum()
          ));
        }
      });
    }
  }

  private void parseXLSXFile(List<Property> properties, Map.Entry<String, InputStream> file) throws IOException {
    XSSFWorkbook wb = new XSSFWorkbook(file.getValue());
    wb.setSelectedTab(0);
    Row header = wb.getSheetAt(0).getRow(0);
    short minColIx = header.getFirstCellNum();
    short maxColIx = header.getLastCellNum();
    AtomicInteger lithuanianLocaleIndex = new AtomicInteger(0);
    for (short colIx = minColIx; colIx < maxColIx; colIx++) {
      if (header.getCell(colIx).getStringCellValue().equals(LITHUANIAN_LOCALE)
          || header.getCell(colIx).getStringCellValue().equals(LITHUANIAN_LOCALE_ID)
          || header.getCell(colIx).getStringCellValue().equals(JAVA_LITHUANIAN_LOCALE_ID)) {
        lithuanianLocaleIndex.set(colIx);
      }
    }
    if (lithuanianLocaleIndex.get() != 0) {
      wb.getSheetAt(0).rowIterator().forEachRemaining(row -> {
        if (row.getRowNum() != 0) {
          properties.add(new Property(
              row.getCell(0).getStringCellValue(),
              row.getCell(lithuanianLocaleIndex.get()).getStringCellValue(),
              file.getKey(),
              row.getRowNum()
          ));
        }
      });
    }
  }

  private void parseXMLFile(List<Property> properties, Map.Entry<String, InputStream> file)
      throws IOException, ParserConfigurationException, SAXException {
    DocumentBuilderFactory dbf = DocumentBuilderFactory.newDefaultInstance();
    DocumentBuilder db = dbf.newDocumentBuilder();
    Document document = db.parse(file.getValue());
    NodeList nodeList = document.getElementsByTagName("string");
    for (int x = 0, size = nodeList.getLength(); x < size; x++) {
      Element element = (Element)nodeList.item(x);
      properties.add(new Property(
          nodeList.item(x).getAttributes().getNamedItem("name").getNodeValue(),
          element.getTextContent(),
          file.getKey(),
          x
      ));
    }
  }

}
