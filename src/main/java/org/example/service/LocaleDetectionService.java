package org.example.service;

import java.io.InputStream;
import java.text.ParseException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.checks.LocalizedGitResourceColumnLocaleExistenceCheck;
import org.example.checks.LocalizedGitResourceNameCheck;
import org.example.checks.LocalizedResourceColumnLocaleExistenceCheck;
import org.example.checks.LocalizedResourceNameCheck;
import org.example.model.Violation;
import org.example.util.counter.LocaleCounter;
import org.example.util.string.StringUtil;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
@AllArgsConstructor
public class LocaleDetectionService {

  public LocaleCounter detectLocale(MultipartFile[] files, List<Violation> violations)
      throws ParseException {
    LocaleCounter counter = new LocaleCounter();

    for (MultipartFile file : files) {
      Optional<String> fileExtension = StringUtil.getExtensionByStringHandling(
          file.getOriginalFilename());
      if (fileExtension.isPresent()) {
        LocalizedResourceNameCheck check = new LocalizedResourceNameCheck(counter, file,
            violations);
        check.check();
      }
      if (fileExtension.isPresent() && StringUtil.matchOtherLocalizedResourceFormats(fileExtension.get())) {
        LocalizedResourceColumnLocaleExistenceCheck check = new LocalizedResourceColumnLocaleExistenceCheck(file, violations);
        check.check();
      }
    }
    return counter;
  }

  public LocaleCounter detectLocaleForGitResources(Map<String, InputStream> files, List<Violation> violations)
      throws ParseException {
    LocaleCounter counter = new LocaleCounter();

    for (Map.Entry<String, InputStream> file : files.entrySet()) {
      Optional<String> fileExtension = StringUtil.getExtensionByStringHandling(
          file.getKey());
      if (fileExtension.isPresent()) {
        LocalizedGitResourceNameCheck check = new LocalizedGitResourceNameCheck(counter,
            file, violations);
        check.check();
      }
      if (fileExtension.isPresent() && StringUtil.matchOtherLocalizedResourceFormats(fileExtension.get())) {
        LocalizedGitResourceColumnLocaleExistenceCheck check = new LocalizedGitResourceColumnLocaleExistenceCheck(
            file, violations);
        check.check();
      }
    }
    return counter;
  }

}
