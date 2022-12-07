package com.arsen.internet.shop.soap.app.validations;

import com.arsen.internet.shop.soap.app.annotation.UniqueLogin;
import com.arsen.internet.shop.soap.app.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;


/**
 * Unique Login Validator
 * @author Arsen Sydoryk
 */
@Component("login")
public class UniqueLoginValidator implements ConstraintValidator<UniqueLogin, String> {

    @Autowired
    private UserRepository repository;

    /**
     * Check if there is user with this login
     * @param login String
     * @param constraintValidatorContext context
     * @return boolean
     */
    @Override
    public boolean isValid(String login, ConstraintValidatorContext constraintValidatorContext) {
        return !repository.existsByLogin(login);
    }
}
