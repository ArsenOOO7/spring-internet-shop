package com.arsen.internet.shop.soap.app.validations;

import com.arsen.internet.shop.soap.app.annotation.Password;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;


/**
 * Password Validator
 * @author Arsen Sydoryk
 */
public class PasswordValidator implements ConstraintValidator<Password, String> {

    private Pattern pattern;

    @Override
    public void initialize(Password constraintAnnotation) {
        pattern = Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[a-zA-Z\\d]{8,}$");
    }


    /**
     * Check password strength
     * @param password String
     * @param constraintValidatorContext context
     * @return boolean
     */
    @Override
    public boolean isValid(String password, ConstraintValidatorContext constraintValidatorContext) {
        return password != null && pattern.matcher(password).matches();
    }
}
