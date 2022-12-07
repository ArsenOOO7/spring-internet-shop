package com.arsen.internet.shop.soap.app.annotation;

import com.arsen.internet.shop.soap.app.validations.OldPasswordValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * When user tries to change password, it checks if old password and value from input are equals
 * @author Arsen Sydoryk
 */
@Constraint(validatedBy = OldPasswordValidator.class)
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD})
public @interface OldPassword {

    String message() default "password.change.invalid.input.old.equals";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

}
