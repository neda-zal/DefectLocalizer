package org.example.util.interfaces;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.example.util.enums.Checklist;
import org.example.util.enums.Priority;
import org.example.util.enums.Scope;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface BelongsToProfile {
  Checklist title();

  Priority priority();

  Scope scope();

}
