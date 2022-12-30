package org.example.util.interfaces;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.example.util.enums.Severity;


@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface Rule {

  String key() default "";

  String name() default "";

  String description() default "";

  Severity severity() default Severity.MINOR;

}
