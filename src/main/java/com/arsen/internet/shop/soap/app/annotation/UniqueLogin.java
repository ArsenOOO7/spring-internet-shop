package com.arsen.internet.shop.soap.app.annotation;

import com.arsen.internet.shop.soap.app.validations.UniqueLoginValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Checking if there is user with this login
 * @author Arsen Sydoryk
 */
@Constraint(validatedBy = UniqueLoginValidator.class)
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD})
public @interface UniqueLogin {
    String message() default "user.invalid.input.login.exists";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
