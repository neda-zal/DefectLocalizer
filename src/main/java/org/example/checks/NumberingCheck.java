package org.example.checks;

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
import org.example.util.enums.Suggestion;
import org.example.util.interfaces.BelongsToProfile;
import org.example.util.interfaces.Rule;

@Slf4j
@Rule(
    key = "NumberingCheck",
    severity = Severity.MINOR)
@BelongsToProfile(title = Checklist.NUMBERING_USAGE, priority = Priority.CRITICAL, scope = Scope.NUMBERING_USAGE)
public class NumberingCheck implements LocalizationCheck {

  private final List<Property> properties;
  private final List<Violation> violations;

  public NumberingCheck(List<Property> properties, List<Violation> violations) {
    this.properties = properties;
    this.violations = violations;
  }

  String wrongNumbering = ".[0-9]";
  Pattern p = Pattern.compile(wrongNumbering);

  @Override
  public void check() {
    properties.forEach(fileLine -> {
      Matcher m = p.matcher(fileLine.getValue());
      if (m.find()) {
        violations.add(new Violation(
            Severity.CRITICAL, Checklist.NUMBERING_USAGE.getLabel(), Priority.CRITICAL,
            new Elimination(Suggestion.NUMBERING_USAGE.getLabel(), null),
            Scope.NUMBERING_USAGE, fileLine.getFilename(), fileLine.getLineNumber()
        ));
      }
    });
  }


}
