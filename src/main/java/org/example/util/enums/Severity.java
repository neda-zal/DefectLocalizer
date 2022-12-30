package org.example.util.enums;

import lombok.Getter;

//@NoArgsConstructor
@Getter
public enum Severity {
  INFO("Informacija"),
  MINOR("Žemas"),
  MAJOR("Aukštas"),
  CRITICAL("Kritinis"),
  BLOCKER("Kliūtis");

  public final String label;

  private Severity(String label) {
    this.label = label;
  }
}
