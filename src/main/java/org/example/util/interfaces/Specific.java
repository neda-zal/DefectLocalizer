package org.example.util.interfaces;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.example.util.enums.Priority;


@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface Specific {

    String title();

    Priority priority();

}
