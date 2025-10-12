package com.cultodeportivo.p10_springboot_crud.validations;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.cultodeportivo.p10_springboot_crud.entities.User;

@SuppressWarnings("null")
@Component
public class UserValidation implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return User.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        User user = (User) target;
        
        if (user.getName() == null || user.getName().isBlank()) {
            errors.rejectValue("name", null, "el usuario si o si debe tener un nombre");
        } else if (user.getName().length() < 2 || user.getName().length() > 50) {
            errors.rejectValue("name", null, "el nombre debe tener entre 2 y 50 caracteres");
        }

        if (user.getLastname() == null || user.getLastname().isBlank()) {
            errors.rejectValue("lastname", null, "el usuario si o si debe tener un apellido");
        } else if (user.getLastname().length() < 2 || user.getLastname().length() > 50) {
            errors.rejectValue("lastname", "el apellido debe tener entre 2 y 50 caracteres");
        }

        String regex = "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";
        if (user.getEmail() == null || user.getEmail().isBlank()) {
            errors.rejectValue("email", null, "el usuario si o si debe tener un email");
        } else if (!user.getEmail().matches(regex)) {
            errors.rejectValue("email", null, "el email debe ser válido");
        }

        if (user.getBirthdate() == null) {
            errors.rejectValue("birthdate", null, "el usuario si o si debe tener una fecha de nacimiento");
        }
    }
    
}
