package org.example.util.enums;

import lombok.Getter;

@Getter
public enum Suggestion {

  LOCALIZED_RESOURCE_FILE_NAMING(
      "Lokalizuotų išteklių failo pavadinime turi būti nurodyta lokalė."),
  LOCALIZED_RESOURCE_COLUMN_LOCALE_EXISTENCE(
      "Excel ir CSV formato lokalizuotų išteklių failo stulpelyje turi būti nurodyta lokalė."),
  LOCALIZED_PROPERTY_DUPLICATION(
      "Parametrų kartojimas. Pasitikrinkite ar teisingai nurodėte parametrus."),
  QUOTATION(
      "Išteklių eilutės viduje vartojamos ne lietuviškos kabutės, su kaitos ženklais. Naudokite „“ kabutes."),
  SPACING(
      "Tarpo nebuvimas tarp skaičiaus ir toliau einančio teksto. Skaičius ištekliuose gali būti įrašytas tiesiogiai arba parametro pavidalu. Pavyzdžiui, 87 %, 5 dienos."),
  SPACING_AFTER_DOT(
      "Tarpo nebuvimas po taško, prieš kitą žodį. Pavyzdžiui, rašykite el. paštas vietoj el.paštas."),
  MULTIPLICATION_CHARACTER_USAGE("Naudojamas x vietoje × kaip daugybos ženklas."),
  HYPHEN_CHARACTER_USAGE("Skaičių intervaluose vartojamas ne brūkšnys (0–1), o brūkšnelis (0-1)."),
  NUMBERING_USAGE(
      "Prie skaičiaus vartojamas grotelių simbolis ar kt. netinkamas ženklas numeriui žymėti. taisyti arba į „Nr.“, pvz., „Studento Nr.“, arba praleisti ar sukeisti skaičių ir žodį vietomis, pvz., „5 reikalavimas“."),
  THOUSAND_SEPARATOR(
      "Tūkstančių grupavimo skirtukas yra taškas „.“. Skaitmenys grupuojami po tris."),
  NUMBER_FRACTION("Skaičių trupmeninės dalies skirtukas yra kablelis „,“."),
  HTML_CHARACTER_USAGE(
      "Ženklas pateiktas HTML kodu. Gali būti, kad tą ženklą reikia lokalizuoti, o tai reikškia – pakeisti kitu."),
  UNUSED_LITHUANIAN_WORDS(
      "Nevartotinų/žargono žodžių naudojimas tekste. Pakeiskite žodį į lietuvių kalbai vartotiną."),
  MULTIPLE_UPPERCASE_USAGE(
      "Didžioji raidė segmento (frazės, sakinio) viduje. Ieškoma žodžių iš didžiosios raidės, kurie rašomi ne kabutėse, ne po skyrybos ženklų, nėra išskiriami taikant ženklinimo kalbų priemones. Išimtis – tikriniai daiktavardžiai.");

  private final String label;

  Suggestion(String label) {
    this.label = label;
  }
}
