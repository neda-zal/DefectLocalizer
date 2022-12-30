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
    key = "NumberFractionUsage",
    severity = Severity.MINOR)
@BelongsToProfile(title = Checklist.NUMBER_FRACTION, priority = Priority.MINOR, scope = Scope.NUMBER_FRACTION)
public class NumberFractionCheck implements LocalizationCheck {

  private final List<Property> properties;
  private final List<Violation> violations;

  public NumberFractionCheck(List<Property> properties, List<Violation> violations) {
    this.properties = properties;
    this.violations = violations;
  }

  String wrongFraction = "(\\.\\d+)+";
  Pattern p = Pattern.compile(wrongFraction);

  @Override
  public void check() {
    properties.forEach(fileLine -> {
      Matcher m = p.matcher(fileLine.getValue());
      if (m.find()) {
        violations.add(new Violation(
            Severity.MINOR, Checklist.NUMBER_FRACTION.getLabel(), Priority.MINOR,
            new Elimination(Suggestion.NUMBER_FRACTION.getLabel(), "https://www.localeplanet.com/icu/lt-LT/index.html"),
            Scope.NUMBER_FRACTION, fileLine.getFilename(), fileLine.getLineNumber()
        ));
      }
    });
  }

}
