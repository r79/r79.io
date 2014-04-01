package annotations;

import generics.Datentyp;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Datenfeld {
  String bezeichnung() default "";
  Datentyp datentyp() default Datentyp.TEXT;
}
