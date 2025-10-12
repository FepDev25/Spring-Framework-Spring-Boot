# 10. Validaciones Personalizadas en Producto (Parte 1)

Spring permite implementar validaciones mediante anotaciones estándar como `@NotBlank`, `@Size`, `@Min`, etc. Sin embargo, en muchos casos es necesario crear **validaciones personalizadas**, especialmente cuando la lógica requiere interacción con la base de datos u otras reglas específicas.

En esta parte se explican dos anotaciones personalizadas aplicadas a los productos: `@IsExistsDb` y `@IsRequired`.

---

## 🧩 1. `@IsExistsDb` – Validación de unicidad en base de datos

### 📄 Anotación

```java
@Constraint(validatedBy = IsExistsDbValidation.class)
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface IsExistsDb {
    String message() default "Ya existe en la base de datos";
    Class<?>[] groups() default { };
    Class<? extends Payload>[] payload() default { };
}
```

### 📄 Validador asociado

```java
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
```

### 🎯 ¿Qué hace?

* Comprueba si el SKU ingresado **ya existe en la base de datos**.
* Si ya existe, la validación **falla** y muestra un mensaje personalizado.

### 🧠 Uso de `SpringContext`

Como los validadores no permiten inyección directa con `@Autowired`, se usa `SpringContext.getBean(...)` para acceder al `ProductService`.

---

## 🧩 2. `@IsRequired` – Campo requerido personalizado

### 📄 Anotación

```java
@Constraint(validatedBy = RequiredValidation.class)
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD})
public @interface IsRequired {
    String message() default "El campo es requerido.";
    Class<?>[] groups() default { };
    Class<? extends Payload>[] payload() default { };
}
```

### 📄 Validador asociado

```java
public class RequiredValidation implements ConstraintValidator<IsRequired, String> {
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return StringUtils.hasText(value);
    }
}
```

### 🎯 ¿Qué hace?

* Valida que un campo **no esté vacío** ni contenga solo espacios.
* Alternativa a `@NotBlank` cuando se quiere **personalizar completamente la lógica o el mensaje**.

---

## 🧪 Ejemplo de uso en DTO

```java
@IsExistsDb
@IsRequired
private String sku;
```

* `@IsExistsDb`: garantiza unicidad del SKU.
* `@IsRequired`: asegura que el campo no esté vacío.

---

## ✅ Conclusión

Las validaciones personalizadas como `@IsExistsDb` y `@IsRequired` son herramientas poderosas que permiten llevar la lógica de validación más allá de lo estándar. Son especialmente útiles para validaciones cruzadas, reglas de negocio complejas o validaciones basadas en la base de datos.
