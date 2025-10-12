# 11. Validaciones Personalizadas en Usuario

Para garantizar que los nombres de usuario sean únicos en la base de datos, se implementa una validación personalizada utilizando la anotación `@ExistsByUsername`. Esta validación evita que se registre un usuario con un nombre ya existente, reforzando así la integridad de los datos y mejorando la experiencia del usuario.

---

## 🧩 1. `@ExistsByUsername` – Anotación personalizada

```java
@Constraint(validatedBy = ExistsByUsernameValidator.class)
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ExistsByUsername {

    String message() default "Ya existe el usuario en la base de datos, escoja otro";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };
}
```

### 🎯 ¿Qué hace?

* Aplica una validación sobre campos tipo `String`.
* Lanza un mensaje personalizado si el `username` ya está registrado en la base de datos.

---

## 🧪 2. Validador: `ExistsByUsernameValidator`

```java
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
            return true; // Evita errores si el contexto no está listo
        }
        return !userService.existsByUsername(username);
    }
}
```

### 🧠 Lógica del validador

* Usa `SpringContext` para obtener el bean `UserService`, ya que los validadores no permiten `@Autowired` directamente.
* Devuelve `true` si el usuario **no existe**, y `false` si **ya existe**, causando el fallo de validación.

---

## 📌 Consideraciones adicionales

| Consideración                | Detalle                                              |
| ---------------------------- | ---------------------------------------------------- |
| `@Component`                 | Registra el validador en el contexto de Spring.      |
| `ConstraintValidator`        | Interfaz usada para definir la lógica personalizada. |
| `SpringContext.getBean(...)` | Accede al servicio en tiempo de ejecución.           |
| `message`                    | Mensaje personalizado mostrado al usuario.           |

---

## 🧪 Ejemplo de uso en entidad `User`

```java
@ExistsByUsername
private String username;
```

> 📌 Esto asegura que cada nombre de usuario sea único, aplicando la validación antes de guardar o actualizar el usuario.

---

## ✅ Conclusión

La validación `@ExistsByUsername` ofrece un mecanismo eficaz para evitar duplicación de usuarios en la base de datos. Al integrarse con `UserService`, combina las capacidades de JPA y Spring Validation para mantener la integridad y consistencia del sistema de autenticación.

Esta validación forma parte esencial del flujo de registro de usuarios, especialmente en aplicaciones que generan tokens JWT basados en identificadores únicos como el `username`.
