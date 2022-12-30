package org.example.util.counter;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class LocaleCounter {

  private int lithuanian = 0;
  private int unrecognized = 0;

  public void incrementLithuanian() {
    this.lithuanian++;
  }

  public void incrementUnrecognized() {
    this.unrecognized++;
  }

}
