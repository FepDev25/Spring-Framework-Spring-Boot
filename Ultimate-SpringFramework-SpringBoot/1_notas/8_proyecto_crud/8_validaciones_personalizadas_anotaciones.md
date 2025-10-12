### **8. Validación Personalizada con Anotaciones en Spring Boot**

En este tema, explicaremos cómo implementar validaciones personalizadas en Spring Boot utilizando anotaciones personalizadas. Las anotaciones personalizadas permiten crear reglas de validación específicas para satisfacer los requisitos de una aplicación, más allá de las restricciones estándar que ofrece Java Bean Validation (`@NotNull`, `@NotBlank`, `@Size`, etc.).

#### **8.1. Creación de la Anotación Personalizada `@IsRequired`**

La anotación `@IsRequired` se utiliza para validar si un campo es obligatorio. Es una validación personalizada que puede aplicarse a cualquier campo de tipo `String`. A continuación, se explica cómo crear esta anotación:

```java
package com.cultodeportivo.p10_springboot_crud.validations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

// Definición de la anotación personalizada
@Constraint(validatedBy = RequiredValidation.class)  // Vincula la anotación con el validador
@Retention(RetentionPolicy.RUNTIME)  // Retiene la anotación en tiempo de ejecución
@Target({ElementType.FIELD, ElementType.METHOD})  // Aplica a campos y métodos
public @interface IsRequired {

    String message() default "El campo es requerido, usando anotaciones";  // Mensaje por defecto

    Class<?>[] groups() default {};  // Permite agrupar las validaciones

    Class<? extends Payload>[] payload() default {};  // Permite adjuntar datos adicionales
}
```

#### **Explicación del Código**:

* **`@Constraint(validatedBy = RequiredValidation.class)`**: Se vincula la anotación con el validador `RequiredValidation`, que se encargará de la lógica de validación.
* **`@Retention(RetentionPolicy.RUNTIME)`**: Define que la anotación será disponible en tiempo de ejecución.
* **`@Target({ElementType.FIELD, ElementType.METHOD})`**: Especifica que la anotación puede aplicarse tanto a campos como a métodos.
* **`message`**: Define el mensaje predeterminado que se muestra si la validación falla.
* **`groups`**: Permite agrupar las validaciones en categorías (por ejemplo, para validaciones de distintos niveles o fases).
* **`payload`**: Permite incluir información adicional que puede usarse durante la validación.

#### **8.2. Implementación del Validador `RequiredValidation`**

El validador implementa la interfaz `ConstraintValidator`, que define la lógica de validación de la anotación. Aquí se asegura que el campo no esté vacío ni contenga solo espacios en blanco.

```java
package com.cultodeportivo.p10_springboot_crud.validations;

import org.springframework.util.StringUtils;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class RequiredValidation implements ConstraintValidator<IsRequired, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        // Retorna true si el valor no es null ni vacío
        return StringUtils.hasText(value);
    }
}
```

#### **Explicación del Código**:

* **`ConstraintValidator<IsRequired, String>`**: La interfaz `ConstraintValidator` toma dos parámetros. El primero es la anotación personalizada (`@IsRequired`), y el segundo es el tipo de dato a validar (`String`).
* **`isValid`**: Este método contiene la lógica de validación. En este caso, se usa el método `StringUtils.hasText(value)` para verificar si el campo `value` no es `null` ni vacío, ni contiene solo espacios en blanco.

#### **8.3. Aplicando la Validación Personalizada en la Entidad `User`**

Una vez creada la anotación personalizada y su validador, la podemos usar en nuestra clase `User` para validar campos como `name` y `lastname`.

```java
package com.cultodeportivo.p10_springboot_crud.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.Date;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @IsRequired(message = "La fecha de nacimiento es requerida con anotaciones personalizadas") 
    private String name;

    @IsRequired(message = "{NotBlank.user.lastname}")
    private String lastname;
    
    private String email;
    private Date birthdate;

    public User() {}

    public User(Long id, String name, String lastname, String email, Date birthdate) {
        this.id = id;
        this.name = name;
        this.lastname = lastname;
        this.email = email;
        this.birthdate = birthdate;
    }

    // Getters y Setters
}
```

#### **Explicación del Código**:

* **`@IsRequired(message = "La fecha de nacimiento es requerida con anotaciones personalizadas")`**: Aplica la validación personalizada al campo `name`. Si el campo está vacío o contiene solo espacios, se mostrará el mensaje de error definido en la anotación.
* **`@IsRequired(message = "{NotBlank.user.lastname}")`**: Aplica la validación personalizada al campo `lastname`. En este caso, también puedes utilizar mensajes externos definidos en el archivo `messages.properties`, como se muestra en el mensaje.

#### **8.4. Validación en el Controlador**

El controlador `UserController` es donde se gestionan las solicitudes HTTP. En los métodos `create` y `update`, se utiliza la validación personalizada junto con las validaciones estándar de Spring para asegurar que los datos sean correctos antes de ser procesados.

```java
package com.cultodeportivo.p10_springboot_crud.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import com.cultodeportivo.p10_springboot_crud.entities.User;
import com.cultodeportivo.p10_springboot_crud.services.UserService;
import com.cultodeportivo.p10_springboot_crud.validations.UserValidation;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService service;

    @Autowired
    private UserValidation userValidation;

    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody User user, BindingResult result) {
        userValidation.validate(user, result);  // Validación personalizada
        if (result.hasFieldErrors()) {
            return validation(result);  // Devuelve errores si se encuentran
        }

        User newUser = service.save(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(newUser);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@Valid @RequestBody User user, BindingResult result, @PathVariable Long id) {
        userValidation.validate(user, result);  // Validación personalizada
        if (result.hasFieldErrors()) {
            return validation(result);  // Devuelve errores si se encuentran
        }

        Optional<User> userOp = service.update(id, user);
        if (userOp.isPresent()) {
            return ResponseEntity.status(HttpStatus.CREATED).body(userOp.orElseThrow());
        }
        return ResponseEntity.notFound().build();
    }

    private ResponseEntity<?> validation(BindingResult result) {
        Map<String, String> errors = new HashMap<>();

        result.getFieldErrors().forEach(error -> {
            errors.put(error.getField(), "El campo " + error.getField() + ": " + error.getDefaultMessage());
        });

        return ResponseEntity.badRequest().body(errors);
    }
}
```

### **8.5. Resumen de la Validación Personalizada con Anotaciones**

1. **Definición de la Anotación**: Creamos la anotación `@IsRequired` para validar si un campo es obligatorio. Esta anotación utiliza un validador asociado que contiene la lógica de validación.

2. **Validador Personalizado**: Implementamos el validador `RequiredValidation` que verifica si un campo no es `null` y no está vacío, usando la clase `StringUtils.hasText()` para facilitar la validación.

3. **Aplicación de la Validación**: La anotación se utiliza en los campos de la clase `User`, y podemos personalizar el mensaje de error o utilizar mensajes definidos en archivos `.properties`.

4. **Validación en el Controlador**: En los métodos `create` y `update` del controlador, la validación personalizada se ejecuta antes de guardar o actualizar la entidad, y si se encuentran errores de validación, se devuelven al cliente.
