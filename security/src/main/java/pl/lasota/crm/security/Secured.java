package pl.lasota.crm.security;

import pl.lasota.crm.enums.PrivilegeName;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Secured {
    PrivilegeName[] value() default {};

}
