package com.arsen.internet.shop.soap.app.annotation;

import com.arsen.internet.shop.soap.app.validations.UniqueEmailValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Checking if there is user with this email
 * @author Arsen Sydoryk
 */
@Constraint(validatedBy = UniqueEmailValidator.class)
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD})
public @interface UniqueEmail {

    String message() default "user.invalid.input.email.exists";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

}
