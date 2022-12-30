package org.example.checks;

import static org.example.util.html.HTMLCodes.HTMLCodesList;

import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.example.model.Elimination;
import org.example.model.Property;
import org.example.model.Violation;
import org.example.util.checks.LocalizationCheck;
import org.example.util.enums.Checklist;
import org.example.util.enums.Priority;
import org.example.util.enums.Scope;
import org.example.util.enums.Severity;
import org.example.util.enums.Suggestion;
import org.example.util.interfaces.BelongsToProfile;
import org.example.util.interfaces.Rule;

@Slf4j
@Rule(
    key = "HTMLCharacterUsage",
    severity = Severity.MAJOR)
@BelongsToProfile(title = Checklist.HTML_CHARACTER_USAGE, priority = Priority.MAJOR, scope = Scope.HTML_CHARACTER_USAGE)
public class HTMLCharacterUsageCheck implements LocalizationCheck {

  private final List<Property> properties;
  private final List<Violation> violations;

  public HTMLCharacterUsageCheck(List<Property> properties, List<Violation> violations) {
    this.properties = properties;
    this.violations = violations;
  }

  @Override
  public void check() {
    properties.forEach(fileLine -> {
      if (matchHTMLCodes(fileLine.getValue())) {
        violations.add(new Violation(
            Severity.MAJOR, Checklist.HTML_CHARACTER_USAGE.getLabel(), Priority.MAJOR,
            new Elimination(Suggestion.HTML_CHARACTER_USAGE.getLabel(), null),
            Scope.HTML_CHARACTER_USAGE, fileLine.getFilename(), fileLine.getLineNumber()
        ));
      }
    });
  }

  private static boolean matchHTMLCodes(String line) {
    return HTMLCodesList.stream().anyMatch(line::contains);
  }

}
