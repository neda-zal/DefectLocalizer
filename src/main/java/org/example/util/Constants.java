package org.example.util;

import java.util.List;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Constants {

  public static final String PROPERTIES = "PROPERTIES";
  public static final String XML = "XML";
  public static final String LITHUANIAN_LOCALE = "lt";
  public static final String LITHUANIAN_LOCALE_ID = "lt_LT";
  public static final String JAVA_LITHUANIAN_LOCALE_ID = "lt-LT";
  public static final List<String> lithuanianLocales = List.of(LITHUANIAN_LOCALE, LITHUANIAN_LOCALE_ID, JAVA_LITHUANIAN_LOCALE_ID);
  public static final String CSV = "CSV";
  public static final String XLSX = "XLSX";
  public static final String XLS = "XLS";
  public static final List<String> otherLocalizedResourceFormats = List.of(CSV, XLSX, XLS);
  public static final List<String> maintainedResourceFormats = List.of(CSV, XLS, XLSX, PROPERTIES, XML);

}
