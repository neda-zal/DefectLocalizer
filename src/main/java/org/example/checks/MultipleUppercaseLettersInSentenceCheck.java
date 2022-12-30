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
    key = "MultipleUpperCaseUsageInSentence",
    severity = Severity.INFO)
@BelongsToProfile(title = Checklist.MULTIPLE_UPPERCASE_USAGE, priority = Priority.INFO, scope = Scope.MULTIPLE_UPPERCASE_USAGE)
public class MultipleUppercaseLettersInSentenceCheck implements LocalizationCheck {

  private final List<Property> properties;
  private final List<Violation> violations;

  public MultipleUppercaseLettersInSentenceCheck(List<Property> properties, List<Violation> violations) {
    this.properties = properties;
    this.violations = violations;
  }

  @Override
  public void check() {
    properties.forEach(this::splitSentence);
  }

  private void splitSentence(Property property) {
    List<String> sentences = List.of(property.getValue().split("\\."));
    sentences.forEach(sentence -> {
      String[] words = sentence.split(" ");
      int uppercaseCount = 0;
      for (String word : words) {
        if (word.chars().anyMatch(Character::isUpperCase) &&
            !(word.contains("\\„") || word.contains("\"") || word.contains("'")))
          uppercaseCount++;
      }
      if (uppercaseCount > 1) {
        violations.add(new Violation(
            Severity.INFO, Checklist.MULTIPLE_UPPERCASE_USAGE.getLabel(), Priority.INFO,
            new Elimination(Suggestion.MULTIPLE_UPPERCASE_USAGE.getLabel(), null),
            Scope.MULTIPLE_UPPERCASE_USAGE, property.getFilename(), property.getLineNumber()
        ));
      }
    });
  }

}
