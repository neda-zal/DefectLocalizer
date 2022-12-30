package org.example.checks;

import java.util.List;
import java.util.Map;
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
import org.example.util.unused.UnusedLithuanianWords;

@Slf4j
@Rule(
    key = "UnusedLithuanianWords",
    severity = Severity.MAJOR)
@BelongsToProfile(title = Checklist.UNUSED_LITHUANIAN_WORDS, priority = Priority.MAJOR, scope = Scope.UNUSED_LITHUANIAN_WORDS)
public class UnusedLithuanianWordsCheck implements LocalizationCheck {

  private final List<Property> properties;
  private final List<Violation> violations;

  public UnusedLithuanianWordsCheck(List<Property> properties, List<Violation> violations) {
    this.properties = properties;
    this.violations = violations;
  }

  @Override
  public void check() {
    properties.forEach(this::matchUnusedWords);
  }

  private void matchUnusedWords(Property property) {
    Map<String, String> unusedLithuanianWords = UnusedLithuanianWords.loadUnusedWords();
    unusedLithuanianWords.forEach((unusedWord, correction) -> {
      if (property.getValue().contains(unusedWord)) {
        violations.add(new Violation(
            Severity.MAJOR, Checklist.UNUSED_LITHUANIAN_WORDS.getLabel(), Priority.MAJOR,
            new Elimination(Suggestion.UNUSED_LITHUANIAN_WORDS.getLabel(), null),
            Scope.UNUSED_LITHUANIAN_WORDS, property.getFilename(), property.getLineNumber()
        ));
      }
    });
  }

}
