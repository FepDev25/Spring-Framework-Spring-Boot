package com.cultodeportivo.p11_spring_security_jwt.validations;

import org.springframework.stereotype.Component;

import com.cultodeportivo.p11_spring_security_jwt.config.SpringContext;
import com.cultodeportivo.p11_spring_security_jwt.services.ProductService;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

@Component
public class IsExistsDbValidation implements ConstraintValidator<IsExistsDb, String> {

    private ProductService productService;

    @Override
    public void initialize(IsExistsDb constraintAnnotation) {
        this.productService = SpringContext.getBean(ProductService.class);
    }


    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return !productService.existsBySku(value);
    }

    
    
}       
