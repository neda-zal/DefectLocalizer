package org.example.checks;

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
    key = "QuotationUsage",
    severity = Severity.MINOR)
@BelongsToProfile(title = Checklist.QUOTATION, priority = Priority.MAJOR, scope = Scope.QUOTATION)
public class QuotationCheck implements LocalizationCheck {

  private final List<Property> properties;
  private final List<Violation> violations;

  public QuotationCheck(List<Property> properties, List<Violation> violations) {
    this.properties = properties;
    this.violations = violations;
  }

  @Override
  public void check() {
    properties.forEach(fileLine -> {
      if (fileLine.getValue().contains("'") || fileLine.getValue().contains("\"")) {
        violations.add(new Violation(
            Severity.MAJOR, Checklist.QUOTATION.getLabel(), Priority.MAJOR,
            new Elimination(Suggestion.QUOTATION.getLabel(), null),
            Scope.LOCALIZED_PROPERTIES, fileLine.getFilename(), fileLine.getLineNumber()
        ));
      }
    });
  }

}
