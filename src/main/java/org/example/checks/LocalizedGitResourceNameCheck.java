package org.example.checks;

import java.io.InputStream;
import java.text.ParseException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.example.model.Elimination;
import org.example.model.Violation;
import org.example.util.checks.LocalizationCheck;
import org.example.util.counter.LocaleCounter;
import org.example.util.enums.Checklist;
import org.example.util.enums.Priority;
import org.example.util.enums.Scope;
import org.example.util.enums.Severity;
import org.example.util.enums.Suggestion;
import org.example.util.interfaces.BelongsToProfile;
import org.example.util.interfaces.Rule;
import org.example.util.string.StringUtil;

@Rule(
    key = "LocalizedResourceNaming",
    severity = Severity.MINOR)
@BelongsToProfile(title = Checklist.LOCALIZED_RESOURCE_FILE_NAMING, priority = Priority.MINOR, scope = Scope.LOCALIZED_RESOURCES)
public class LocalizedGitResourceNameCheck implements LocalizationCheck {

  private final LocaleCounter counter;
  private final Map.Entry<String, InputStream> file;
  private final List<Violation> violations;

  public LocalizedGitResourceNameCheck(LocaleCounter counter, Map.Entry<String, InputStream> file,
      List<Violation> violations) {
    this.counter = counter;
    this.file = file;
    this.violations = violations;
  }

  @Override
  public void check() throws ParseException {
    Optional<String> fileNameWithoutExtension =
        StringUtil.getFileNameWithoutExtensionByStringHandling(file.getKey());
    if (fileNameWithoutExtension.isEmpty()) {
      counter.incrementUnrecognized();
      violations.add(new Violation(
          Severity.MINOR, Checklist.LOCALIZED_RESOURCE_FILE_NAMING.getLabel(), Priority.MINOR,
          new Elimination(Suggestion.LOCALIZED_RESOURCE_FILE_NAMING.getLabel(),
              "https://www.localeplanet.com/icu/lt-LT/index.html"),
          Scope.LOCALIZED_RESOURCES, this.file.getKey()
      ));
    }

    if (fileNameWithoutExtension.isPresent()) {
      if (StringUtil.matchLithuanianLocales(fileNameWithoutExtension.get())) {
        counter.incrementLithuanian();
      } else {
        counter.incrementUnrecognized();
        violations.add(
            new Violation(Severity.MINOR, Checklist.LOCALIZED_RESOURCE_FILE_NAMING.getLabel(),
                Priority.MINOR, new Elimination(
                Suggestion.LOCALIZED_RESOURCE_FILE_NAMING.getLabel(),
                "https://www.localeplanet.com/icu/lt-LT/index.html"),
                Scope.LOCALIZED_RESOURCES, this.file.getKey()));
      }
    }
  }

}
