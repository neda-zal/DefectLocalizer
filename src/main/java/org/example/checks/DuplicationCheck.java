package org.example.checks;

import static org.example.util.enums.Suggestion.LOCALIZED_PROPERTY_DUPLICATION;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
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
    key = "LocalizedResourceDuplications",
    severity = Severity.MINOR)
@BelongsToProfile(title = Checklist.LOCALIZED_PROPERTY_DUPLICATION, priority = Priority.MINOR, scope = Scope.LOCALIZED_PROPERTIES)
public class DuplicationCheck implements LocalizationCheck {

  private final List<Property> properties;
  private final List<Violation> violations;

  public DuplicationCheck(List<Property> properties, List<Violation> violations) {
    this.properties = properties;
    this.violations = violations;
  }

  @Override
  public void check() {
    List<Property> duplicates = properties.stream()
        .collect(Collectors.groupingBy(Property::getProperty, Collectors.toList()))
        .values()
        .stream()
        .filter(i -> i.size() > 1)
        .distinct()
        .flatMap(Collection::stream)
        .skip(1)
        .toList();

    duplicates.forEach(duplicate -> violations.add(new Violation(
        Severity.MINOR, Checklist.LOCALIZED_PROPERTY_DUPLICATION.getLabel(), Priority.MINOR,
        new Elimination(LOCALIZED_PROPERTY_DUPLICATION.getLabel(), null),
        Scope.LOCALIZED_PROPERTIES, duplicate.getFilename(), duplicate.getLineNumber()
    )));
  }

}
