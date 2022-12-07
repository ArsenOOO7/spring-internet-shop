package com.arsen.internet.shop.soap.app.validations;

import com.arsen.internet.shop.soap.app.annotation.Image;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;


/**
 * Multipart file validator
 * @author Arsen Sydoryk
 */
public class ImageValidator implements ConstraintValidator<Image, MultipartFile> {


    /**
     * Check if multipart file is not empty
     * @param multipartFile with image
     * @param constraintValidatorContext context
     * @return boolean
     */
    @Override
    public boolean isValid(MultipartFile multipartFile, ConstraintValidatorContext constraintValidatorContext) {

        return multipartFile != null && !multipartFile.isEmpty() && multipartFile.getSize() > 0;

    }
}
