package org.example.scanner;

import java.io.File;
import lombok.NoArgsConstructor;
import org.example.model.Report;
import org.example.util.checks.LocalizationCheck;

@NoArgsConstructor
public class LocalizedResourceScanner {

  public static Report scanSingleFile(File file, LocalizationCheck... visitors) {
    if (!file.isFile())
      throw new IllegalArgumentException("File '" + file + "' not found.");





    //AstScanner<EcmaScriptGrammar> scanner = create(new EcmaScriptConfiguration(Charsets.UTF_8), visitors);
    //scanner.scanFile(file);
    /*Collection<SourceCode> sources = scanner.getIndex().search(new QueryByType(SourceFile.class));
    if (sources.size() != 1) {
      throw new IllegalStateException("Only one SourceFile was expected whereas " + sources.size() + " has been returned.");
    }*/
    return null; //(SourceFile) sources.iterator().next();
  }

}
