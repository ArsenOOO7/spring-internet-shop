package com.arsen.internet.shop.soap.app.annotation;

import com.arsen.internet.shop.soap.app.validations.PasswordValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Checking password strength
 * @author Arsen Sydoryk
 */
@Constraint(validatedBy = PasswordValidator.class)
@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Password {

    String message() default "user.invalid.input.password.pattern";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

}
