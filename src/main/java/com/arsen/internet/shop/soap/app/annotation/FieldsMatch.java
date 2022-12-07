package com.arsen.internet.shop.soap.app.annotation;

import com.arsen.internet.shop.soap.app.validations.FieldsMatchValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 *
 * Annotation to check if 2 fields are equals
 * @author Arsen Sydoryk
 */
@Constraint(validatedBy = FieldsMatchValidator.class)
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface FieldsMatch {

    String message() default "";
    String field() default "";
    String fieldMatch() default "";

    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};


    @Target({ElementType.TYPE})
    @Retention(RetentionPolicy.RUNTIME)
    @interface List{
        FieldsMatch[] value();
    }

}
