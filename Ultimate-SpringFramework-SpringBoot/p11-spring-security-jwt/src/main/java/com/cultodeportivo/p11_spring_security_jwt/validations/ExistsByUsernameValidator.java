package com.cultodeportivo.p11_spring_security_jwt.validations;

import org.springframework.stereotype.Component;

import com.cultodeportivo.p11_spring_security_jwt.config.SpringContext;
import com.cultodeportivo.p11_spring_security_jwt.services.UserService;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

@Component
public class ExistsByUsernameValidator implements ConstraintValidator<ExistsByUsername, String> {

    private UserService userService;

    @Override
    public void initialize(ExistsByUsername constraintAnnotation) {
        this.userService = SpringContext.getBean(UserService.class);
    }

    @Override
    public boolean isValid(String username, ConstraintValidatorContext context) {
        if (userService == null) {
            return true;
        }
        return !userService.existsByUsername(username);
    }
    
}
