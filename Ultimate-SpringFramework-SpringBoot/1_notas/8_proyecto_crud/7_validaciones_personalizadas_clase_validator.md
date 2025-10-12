### **7. Validación Personalizada con una Clase `Validator` en Spring Boot**

En este tema, exploramos cómo implementar validaciones personalizadas en Spring Boot utilizando la interfaz `Validator`. Esta es una forma avanzada de realizar validaciones que no se pueden cubrir solo con las anotaciones estándar de validación como `@NotBlank`, `@NotNull`, entre otras.

#### **7.1. Explicación de la Clase de Entidad `User`**

La clase `User` representa la entidad de usuario, con atributos como `name`, `lastname`, `email`, y `birthdate`. En este caso, se busca realizar una validación más compleja que no puede ser cubierta únicamente con las anotaciones estándar.

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

    @NotBlank(message = "El nombre es obligatorio")
    private String name;

    @NotBlank(message = "El apellido es obligatorio")
    private String lastname;

    @NotBlank(message = "El email es obligatorio")
    private String email;

    @NotNull(message = "La fecha de nacimiento es obligatoria")
    private Date birthdate;

    // Getters y setters
}
```

#### **7.2. Implementación de la Clase `Validator` Personalizado (`UserValidation`)**

Para las validaciones personalizadas, implementamos la interfaz `Validator` de Spring, la cual nos permite validar los objetos de la clase `User` de una manera flexible.

```java
package com.cultodeportivo.p10_springboot_crud.validations;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import com.cultodeportivo.p10_springboot_crud.entities.User;

@Component
public class UserValidation implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return User.class.isAssignableFrom(clazz);  // Verifica que el objeto sea de tipo User
    }

    @Override
    public void validate(Object target, Errors errors) {
        User user = (User) target;

        // Validación para el nombre
        if (user.getName() == null || user.getName().isBlank()) {
            errors.rejectValue("name", null, "El usuario debe tener un nombre");
        } else if (user.getName().length() < 2 || user.getName().length() > 50) {
            errors.rejectValue("name", null, "El nombre debe tener entre 2 y 50 caracteres");
        }

        // Validación para el apellido
        if (user.getLastname() == null || user.getLastname().isBlank()) {
            errors.rejectValue("lastname", null, "El usuario debe tener un apellido");
        } else if (user.getLastname().length() < 2 || user.getLastname().length() > 50) {
            errors.rejectValue("lastname", "El apellido debe tener entre 2 y 50 caracteres");
        }

        // Validación para el email (usando una expresión regular)
        String regex = "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";
        if (user.getEmail() == null || user.getEmail().isBlank()) {
            errors.rejectValue("email", null, "El usuario debe tener un email");
        } else if (!user.getEmail().matches(regex)) {
            errors.rejectValue("email", null, "El email debe ser válido");
        }

        // Validación para la fecha de nacimiento
        if (user.getBirthdate() == null) {
            errors.rejectValue("birthdate", null, "El usuario debe tener una fecha de nacimiento");
        }
    }
}
```

### **7.3. Explicación de la Clase `UserValidation`**

1. **`supports(Class<?> clazz)`**:

   * Este método verifica si el validador puede validar el tipo de clase proporcionado. En este caso, solo se permite la validación de objetos de tipo `User`.

2. **`validate(Object target, Errors errors)`**:

   * **`target`**: El objeto que se está validando, en este caso un `User`.
   * **`errors`**: Un objeto `Errors` que se utiliza para almacenar los errores de validación encontrados durante el proceso de validación.

3. **Validaciones específicas**:

   * **`name`**: Se verifica que el nombre no esté vacío, y que su longitud esté entre 2 y 50 caracteres.
   * **`lastname`**: Se realiza la misma validación para el apellido.
   * **`email`**: Usamos una expresión regular para validar el formato del email. Si el email no es válido, se genera un error.
   * **`birthdate`**: Se asegura que la fecha de nacimiento no sea `null`.

### **7.4. Uso del Validador en el Controlador**

En el controlador, el validador personalizado se utiliza en el método `create` y `update` para validar los datos antes de proceder con el procesamiento de la solicitud.

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

### **7.5. Resumen y Beneficios de la Validación Personalizada**

1. **Control Total sobre la Validación**: Al implementar la interfaz `Validator`, puedes crear validaciones muy específicas que no están cubiertas por las anotaciones estándar. Esto es útil cuando tienes reglas de validación complejas o personalizadas.

2. **Mensajes de Error Personalizados**: Permite definir los mensajes de error de forma muy detallada, lo que mejora la experiencia de usuario al recibir mensajes claros y comprensibles.

3. **Seamless Integration**: La integración con los controladores es fluida, ya que se realiza dentro del ciclo de vida del controlador. El validador se ejecuta antes de que el objeto sea procesado, lo que previene problemas en etapas posteriores de la lógica.

4. **Flexibilidad**: Puedes validar múltiples campos con diferentes lógicas, lo que te da flexibilidad y control total sobre las reglas de negocio y los errores a retornar.

---

Con este enfoque de validación personalizada, tienes un control detallado sobre las reglas de validación y los mensajes de error que se devuelven al cliente. Esto es especialmente útil cuando las validaciones predeterminadas no cubren todas las necesidades de la aplicación.
