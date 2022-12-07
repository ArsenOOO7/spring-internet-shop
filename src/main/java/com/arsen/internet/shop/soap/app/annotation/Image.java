package com.arsen.internet.shop.soap.app.annotation;

import com.arsen.internet.shop.soap.app.validations.ImageValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * Annotation to check multipart file
 * @author Arsen Sydoryk
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ImageValidator.class)
public @interface Image {

    String message() default "product.invalid.input.image";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

}
