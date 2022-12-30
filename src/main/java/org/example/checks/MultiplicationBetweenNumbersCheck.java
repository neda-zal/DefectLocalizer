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
    key = "MultiplicationCharacterUsage",
    severity = Severity.MINOR)
@BelongsToProfile(title = Checklist.MULTIPLICATION_CHARACTER_USAGE, priority = Priority.MAJOR, scope = Scope.MULTIPLICATION_CHARACTER_USAGE)
public class MultiplicationBetweenNumbersCheck implements LocalizationCheck {

  private final List<Property> properties;
  private final List<Violation> violations;

  public MultiplicationBetweenNumbersCheck(List<Property> properties, List<Violation> violations) {
    this.properties = properties;
    this.violations = violations;
  }

  String wrongMultiplication = "\\d+[xX]\\d+";
  Pattern p = Pattern.compile(wrongMultiplication);

  @Override
  public void check() {
    properties.forEach(fileLine -> {
      Matcher m = p.matcher(fileLine.getValue());
      if (m.find()) {
        violations.add(new Violation(
            Severity.MAJOR, Checklist.MULTIPLICATION_CHARACTER_USAGE.getLabel(), Priority.MAJOR,
            new Elimination(Suggestion.MULTIPLICATION_CHARACTER_USAGE.getLabel(), null),
            Scope.MULTIPLICATION_CHARACTER_USAGE, fileLine.getFilename(),
            fileLine.getLineNumber()));
      }
    });
  }

  public boolean findMultiplicationBetweenNumbers(Property property) {
    Matcher m = p.matcher(property.getValue());
    return m.find();
  }

}
