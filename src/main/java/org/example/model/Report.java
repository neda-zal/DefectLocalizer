package org.example.model;

import java.util.List;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class Report {

  List<Violation> violations;
  private int lithuanianLocales;
  private int unrecognizedLocales;

}
