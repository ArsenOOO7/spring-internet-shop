package com.arsen.internet.shop.soap.app.validations;

import com.arsen.internet.shop.soap.app.annotation.OldPassword;
import com.arsen.internet.shop.soap.app.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;


/**
 * Old password validator
 * @author Arsen Sydoryk
 */
public class OldPasswordValidator implements ConstraintValidator<OldPassword, String> {

    @Autowired
    private PasswordEncoder passwordEncoder;


    /**
     * Check if entered and real passwords are equal
     * @param oldPassword String
     * @param constraintValidatorContext context
     * @return boolean
     */
    @Override
    public boolean isValid(String oldPassword, ConstraintValidatorContext constraintValidatorContext) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return passwordEncoder.matches(oldPassword, user.getPassword());
    }
}
