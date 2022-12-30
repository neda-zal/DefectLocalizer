package org.example.util.unused;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.core.io.ClassPathResource;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UnusedLithuanianWords {

  public static Map<String, String> loadUnusedWords() {
    ClassPathResource resource = new ClassPathResource("Neteiktini.xlsx");
    Map<String, String> unusedWordsAndCorrections = new HashMap<>();
    try {
      XSSFWorkbook myWorkBook = new XSSFWorkbook(resource.getInputStream());
      XSSFSheet mySheet = myWorkBook.getSheetAt(0);
      Iterator<Row> rowIterator = mySheet.iterator();
      rowIterator.forEachRemaining(
          row -> unusedWordsAndCorrections.put(row.getCell(1).getStringCellValue().trim(),
              row.getCell(2).getStringCellValue()));
    } catch (IOException e) {
      log.error(e.getMessage());
    }
    return unusedWordsAndCorrections;
  }

}
