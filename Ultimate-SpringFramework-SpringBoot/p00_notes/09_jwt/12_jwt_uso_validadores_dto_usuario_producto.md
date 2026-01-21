# 12. Uso de Validadores en `ProductDTO` y `User`

En esta sección se muestra cómo se integran las **validaciones estándar** de Jakarta Validation y las **validaciones personalizadas** previamente definidas en los modelos `ProductDTO` y `User`. Esta validación automática es fundamental para asegurar la calidad de los datos antes de ser procesados por el backend.

---

## 1. Validadores en `ProductDTO`

```java
public class ProductDTO {

    @NotBlank(message = "{NotEmpty.product.name}")
    @Size(min = 3, max = 45)
    private String name;

    @Min(0)
    @NotNull
    private Double price;

    @NotBlank
    private String description;

    @IsExistsDb
    @IsRequired
    private String sku;
}
```

### Validaciones estándar

| Anotación              | Campo                 | Función                                             |
| ---------------------- | --------------------- | --------------------------------------------------- |
| `@NotBlank`            | `name`, `description` | Verifica que no estén vacíos ni solo con espacios.  |
| `@Size(min=3, max=45)` | `name`                | Define una longitud mínima y máxima para el nombre. |
| `@Min(0)`              | `price`               | Restringe el valor mínimo a 0.                      |
| `@NotNull`             | `price`               | Obliga a que el campo no sea nulo.                  |

### Validaciones personalizadas

| Anotación     | Campo | Función                                                   |
| ------------- | ----- | --------------------------------------------------------- |
| `@IsExistsDb` | `sku` | Verifica que el SKU no esté repetido en la base de datos. |
| `@IsRequired` | `sku` | Verifica que el SKU tenga texto válido (no vacío).        |

> Estas validaciones se activan automáticamente cuando se usa `@Valid` en los controladores al recibir un `ProductDTO`.

---

## 2. Validadores en `User` (entidad)

```java
@Entity
public class User {

    @NotBlank
    @Size(min = 3, max = 20)
    @ExistsByUsername
    private String username;

    @NotBlank
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    ...
}
```

### Validaciones estándar User

| Anotación   | Campo                  | Función                                 |
| ----------- | ---------------------- | --------------------------------------- |
| `@NotBlank` | `username`, `password` | Asegura que los campos no estén vacíos. |
| `@Size`     | `username`             | Define el rango de caracteres válidos.  |

### Validaciones personalizadas User

| Anotación           | Campo      | Función                                                             |
| ------------------- | ---------- | ------------------------------------------------------------------- |
| `@ExistsByUsername` | `username` | Verifica que no se repita el nombre de usuario en la base de datos. |

> Esta validación es clave para garantizar unicidad y evitar conflictos al registrar nuevos usuarios.

---

## ¿Cómo se aplican?

1. Estas anotaciones son evaluadas cuando se recibe un objeto en una petición HTTP (ej. `@RequestBody @Valid ProductDTO productDto`).
2. Si una validación falla, Spring Boot lanza automáticamente una excepción `MethodArgumentNotValidException`.
3. El controlador puede capturarla y devolver un JSON con los errores.

---

## Conclusión

El uso conjunto de validadores estándar y personalizados garantiza que los datos de productos y usuarios cumplan con las reglas de negocio antes de ser procesados. Esto fortalece la seguridad, mejora la experiencia de usuario y reduce errores en la lógica de aplicación.

Estas validaciones son un paso previo esencial para asegurar integridad antes de emitir tokens JWT o acceder a recursos protegidos.
