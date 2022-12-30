package org.example.util.string;

import static org.example.util.Constants.lithuanianLocales;
import static org.example.util.Constants.otherLocalizedResourceFormats;

import java.util.Optional;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.io.FilenameUtils;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class StringUtil {

  public static Optional<String> getExtensionByStringHandling(String filename) {
    return Optional.ofNullable(filename)
        .filter(f -> f.contains("."))
        .map(f -> f.substring(filename.lastIndexOf(".") + 1));
  }

  public static Optional<String> getFileNameWithoutExtensionByStringHandling(String filename) {
    return Optional.ofNullable(filename)
        .filter(f -> f.contains("."))
        .map(FilenameUtils::getBaseName);
  }

  public static boolean matchLithuanianLocales(String line) {
    return lithuanianLocales.stream().anyMatch(line::contains);
  }

  public static boolean matchOtherLocalizedResourceFormats(String line) {
    return otherLocalizedResourceFormats.stream().anyMatch(line::equalsIgnoreCase);
  }

}
