package org.example.checks;

import static org.example.util.enums.Suggestion.LOCALIZED_RESOURCE_COLUMN_LOCALE_EXISTENCE;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.example.model.Elimination;
import org.example.model.Violation;
import org.example.util.checks.LocalizationCheck;
import org.example.util.enums.Checklist;
import org.example.util.enums.Priority;
import org.example.util.enums.Scope;
import org.example.util.enums.Severity;
import org.example.util.interfaces.BelongsToProfile;
import org.example.util.interfaces.Rule;
import org.example.util.string.StringUtil;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Rule(
    key = "LocalizedResourceNaming",
    severity = Severity.MINOR)
@BelongsToProfile(title = Checklist.LOCALIZED_RESOURCE_LOCALE_EXISTENCE_IN_COLUMN, priority = Priority.MINOR, scope = Scope.LOCALIZED_RESOURCES)
public class LocalizedResourceColumnLocaleExistenceCheck implements LocalizationCheck {

  private final MultipartFile file;
  private final List<Violation> violations;

  public LocalizedResourceColumnLocaleExistenceCheck(MultipartFile file,
      List<Violation> violations) {
    this.file = file;
    this.violations = violations;
  }

  @Override
  public void check() {
    try {
      InputStream inputStream = this.file.getInputStream();
      BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
      String line = br.readLine();  // read 1st line
      if (line != null && !line.isEmpty() && !StringUtil.matchLithuanianLocales(line)) {
        violations.add(new Violation(
            Severity.MINOR, Checklist.LOCALIZED_RESOURCE_LOCALE_EXISTENCE_IN_COLUMN.getLabel(),
            Priority.MINOR,
            new Elimination(LOCALIZED_RESOURCE_COLUMN_LOCALE_EXISTENCE.getLabel(), "https://www.localeplanet.com/icu/lt-LT/index.html"),
            Scope.LOCALIZED_RESOURCES, this.file.getOriginalFilename()
        ));
      }
    } catch (IOException e) {
      log.error(e.getMessage());
    }
  }
}
