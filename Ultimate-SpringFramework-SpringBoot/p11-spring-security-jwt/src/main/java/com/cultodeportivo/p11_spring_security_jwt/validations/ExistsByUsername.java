package com.cultodeportivo.p11_spring_security_jwt.validations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Constraint(validatedBy = ExistsByUsernameValidator.class)
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ExistsByUsername {

    String message() default "Ya existe el usuario en la base de datos, escoja otro";

	Class<?>[] groups() default { };

	Class<? extends Payload>[] payload() default { };
    
}
