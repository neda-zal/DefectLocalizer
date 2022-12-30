package org.example.checks;

import static org.example.util.enums.Suggestion.SPACING_AFTER_DOT;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.extern.slf4j.Slf4j;
import org.example.model.Elimination;
import org.example.model.Property;
import org.example.model.Violation;
import org.example.util.checks.LocalizationCheck;
import org.example.util.enums.Checklist;
import org.example.util.enums.Priority;
import org.example.util.enums.Scope;
import org.example.util.enums.Severity;
import org.example.util.interfaces.BelongsToProfile;
import org.example.util.interfaces.Rule;

@Slf4j
@Rule(
    key = "Spacing",
    severity = Severity.MINOR)
@BelongsToProfile(title = Checklist.SPACING_AFTER_DOT, priority = Priority.MINOR, scope = Scope.SPACING)
public class SpaceAfterDotCheck implements LocalizationCheck {

  private final List<Property> properties;
  private final List<Violation> violations;

  public SpaceAfterDotCheck(List<Property> properties, List<Violation> violations) {
    this.properties = properties;
    this.violations = violations;
  }

  String noWhitespace = "[.][a-zA-Z]"; // (?<=[.])[a-zA-Z]
  Pattern p = Pattern.compile(noWhitespace);

  @Override
  public void check() {
    properties.forEach(fileLine -> {
      Matcher m = p.matcher(fileLine.getValue());
      if (m.find()) {
        violations.add(new Violation(
            Severity.MINOR, Checklist.SPACING_AFTER_DOT.getLabel(), Priority.MINOR,
            new Elimination(SPACING_AFTER_DOT.getLabel(), null),
            Scope.SPACING, fileLine.getFilename(), fileLine.getLineNumber()
        ));
      }
    });
  }
}
