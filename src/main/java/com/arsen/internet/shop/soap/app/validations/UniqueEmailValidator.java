package com.arsen.internet.shop.soap.app.validations;

import com.arsen.internet.shop.soap.app.annotation.UniqueEmail;
import com.arsen.internet.shop.soap.app.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * Unique Email Validator
 * @author Arsen Sydoryk
 */
@Component("email")
public class UniqueEmailValidator implements ConstraintValidator<UniqueEmail, String> {

    @Autowired
    private UserRepository repository;


    /**
     * Check if there is user with this email
     * @param email String
     * @param constraintValidatorContext context
     * @return boolean
     */
    @Override
    public boolean isValid(String email, ConstraintValidatorContext constraintValidatorContext) {
        return !repository.existsByEmail(email);
    }
}
