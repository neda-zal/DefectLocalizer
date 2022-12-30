package org.example.util.enums;

import lombok.Getter;

@Getter
public enum Checklist {
  LOCALIZED_RESOURCE_FILE_NAMING("Lokalės nurodymas failo pavadinime"),
  LOCALIZED_RESOURCE_LOCALE_EXISTENCE_IN_COLUMN("Lokalės nurodymas failo stulpelyje"),
  LOCALIZED_PROPERTY_DUPLICATION("Pakartotinis parametrų naudojimas"),
  QUOTATION("Nelietuviškų kabučių naudojimas"),
  SPACING("Tarpo po skaičiaus (parametro) naudojimas"),
  SPACING_AFTER_DOT("Tarpo po taško naudojimas"),
  MULTIPLICATION_CHARACTER_USAGE("Daugybos ženklo naudojimas"),
  HYPHEN_CHARACTER_USAGE("Brūkšnelio skaičių intervaluose naudojimas"),
  NUMBERING_USAGE("Numerio ženklo naudojimas"),
  THOUSAND_SEPARATOR("Tūkstančių grupavimas"),
  NUMBER_FRACTION("Trupmeninė dalis"),
  HTML_CHARACTER_USAGE("HTML kodo ženklų naudojimas"),
  UNUSED_LITHUANIAN_WORDS("Nevartotinų lietuviškų žodžių naudojimas"),
  MULTIPLE_UPPERCASE_USAGE("Didžiųjų raidžių naudojimas");

  private final String label;

  Checklist(String label) {
    this.label = label;
  }

}
