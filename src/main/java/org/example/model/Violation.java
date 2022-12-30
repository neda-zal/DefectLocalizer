package org.example.model;

import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.util.enums.Priority;
import org.example.util.enums.Scope;
import org.example.util.enums.Severity;
import org.example.util.interfaces.Rule;

@NoArgsConstructor
@Getter
@Setter
public class Violation {

  private Rule rule;
  private Severity severity;
  private String checklist;
  private Priority priority;
  private Elimination elimination;
  private Scope scope;
  private Integer lineId;
  private String createdAt;
  private String filename;

  public Violation(Severity severity, String checklist, Priority priority, Elimination elimination,
      Scope scope, String filename) {
    this.severity = severity;
    this.checklist = checklist;
    this.priority = priority;
    this.elimination = elimination;
    this.scope = scope;
    this.filename = filename;
    this.createdAt = this.formatDate(new Date());
  }

  public Violation(Severity severity, String checklist, Priority priority, Elimination elimination,
      Scope scope, String filename, int lineId) {
    this.severity = severity;
    this.checklist = checklist;
    this.priority = priority;
    this.elimination = elimination;
    this.scope = scope;
    this.filename = filename;
    this.lineId = lineId;
    this.createdAt = this.formatDate(new Date());
  }

  private String formatDate(Date date) {
    DateFormat df = DateFormat.getDateInstance(DateFormat.FULL, new Locale("lt", "Lithuania"));
    return df.format(date);
  }

}
