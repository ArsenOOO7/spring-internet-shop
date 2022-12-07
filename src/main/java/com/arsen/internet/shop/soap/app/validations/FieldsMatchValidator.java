package com.arsen.internet.shop.soap.app.validations;

import com.arsen.internet.shop.soap.app.annotation.FieldsMatch;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;


/**
 * Fields match validator
 * @author Arsen Sydoryk
 */
public class FieldsMatchValidator implements ConstraintValidator<FieldsMatch, Object> {

    private String field;
    private String fieldMatch;

    @Override
    public void initialize(FieldsMatch constraintAnnotation) {
        field = constraintAnnotation.field();
        fieldMatch = constraintAnnotation.fieldMatch();
    }

    /**
     * Check if values of two fields are equal
     * @param obj where these fields are
     * @param constraintValidatorContext context
     * @return boolean
     */
    @Override
    public boolean isValid(Object obj, ConstraintValidatorContext constraintValidatorContext) {
        BeanWrapper wrapper = new BeanWrapperImpl(obj);
        String fieldValue = (String) wrapper.getPropertyValue(field);
        String fieldMatchValue = (String) wrapper.getPropertyValue(fieldMatch);

        if(fieldValue == null || fieldMatchValue == null){
            return true;
        }

        return fieldValue.equals(fieldMatchValue);
    }
}
