### **6. Validaciones con `@Valid` y `BindingResult` en Spring Boot**

Las validaciones en Spring Boot permiten garantizar que los datos que el usuario envía a través de los formularios, o en este caso a través de solicitudes HTTP, cumplan con ciertas reglas y restricciones antes de ser procesados o almacenados en la base de datos. En el ejemplo, vamos a ver cómo usar las anotaciones de validación para asegurar que los datos del producto sean correctos antes de ser procesados por el backend.

#### **6.1. Entidad `Product` - Validaciones en la Clase**

En la clase `Product`, hemos aplicado varias validaciones a los campos mediante anotaciones de la API `jakarta.validation.constraints`. Esto asegura que los datos proporcionados cumplan con las reglas que hemos definido.

```java
package com.cultodeportivo.p10_springboot_crud.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "{NotEmpty.product.name}") // Mensaje personalizado de archivo .properties
    @Size(min = 3, max = 45)
    private String name;

    @Min(0)
    @NotNull
    private double price;

    @NotBlank
    private String description;

    // Getters y Setters
}
```

1. **`@NotBlank(message = "{NotEmpty.product.name}")`**:

   * **Función**: Asegura que el campo `name` no esté vacío ni contenga solo espacios en blanco.
   * **Mensaje**: El mensaje de error se obtiene del archivo `messages.properties` (ejemplo: `NotEmpty.product.name=es requerido`).

2. **`@Size(min = 3, max = 45)`**:

   * **Función**: Valida que el nombre del producto tenga una longitud entre 3 y 45 caracteres.

3. **`@Min(0)`**:

   * **Función**: Asegura que el precio sea mayor o igual a 0.

4. **`@NotNull`**:

   * **Función**: Asegura que el precio no sea `null`.

5. **`@NotBlank`**:

   * **Función**: Asegura que la descripción no esté vacía ni contenga solo espacios en blanco.

#### **6.2. Controlador `ProductController` - Validación en el Endpoint**

En el controlador `ProductController`, los productos son validados antes de ser procesados usando las anotaciones `@Valid` y `BindingResult`. Cuando se realiza una solicitud de creación o actualización, Spring valida automáticamente el objeto `Product` antes de pasar al servicio para su procesamiento.

```java
package com.cultodeportivo.p10_springboot_crud.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cultodeportivo.p10_springboot_crud.entities.Product;
import com.cultodeportivo.p10_springboot_crud.services.ProductService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/productos")
public class ProductController {

    @Autowired
    private ProductService service;
    
    @GetMapping
    public List<Product> list() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> view(@PathVariable Long id) {
        Optional<Product> opt = service.findById(id);
        if (opt.isPresent()) {
            return ResponseEntity.ok(opt.orElseThrow());
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("")
    public ResponseEntity<?> create(@Valid @RequestBody Product product, BindingResult result) {
        if (result.hasFieldErrors()) {
            return validation(result);
        }
        Product newProduct = service.save(product);
        return ResponseEntity.status(HttpStatus.CREATED).body(newProduct);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@Valid @RequestBody Product product, BindingResult result, @PathVariable Long id) {
        if (result.hasFieldErrors()) {
            return validation(result);
        }

        Optional<Product> opt = service.update(id, product);
        if (opt.isPresent()) {
            return ResponseEntity.status(HttpStatus.CREATED).body(opt.orElseThrow());
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        Optional<Product> opt = service.delete(id);
        if (opt.isPresent()) {
            return ResponseEntity.ok(opt.orElseThrow());
        }
        return ResponseEntity.notFound().build();
    }

    private ResponseEntity<?> validation(BindingResult result) {
        Map<String, String> errors = new HashMap<>();

        result.getFieldErrors().forEach(error -> {
            errors.put(error.getField(), "El campo " + error.getField() + " " + error.getDefaultMessage());
        });

        return ResponseEntity.badRequest().body(errors);
    }
}
```

1. **`@Valid`**:

   * Utilizado en los parámetros `Product product` para que Spring realice la validación de las anotaciones definidas en la clase `Product` antes de que el objeto llegue al método del controlador.

2. **`BindingResult result`**:

   * Se utiliza para capturar los errores de validación si los hay. Si los errores son encontrados (es decir, si el resultado contiene errores), se llama al método `validation()` que genera una respuesta adecuada con los errores.

3. **`validation(result)`**:

   * Este método se encarga de formatear los errores y devolverlos al cliente en una respuesta `400 Bad Request` con un mapa que describe los errores encontrados.

#### **6.3. Mensajes de Validación - `messages.properties`**

Los mensajes de validación, tales como `"es requerido"` o `"no puede ser nulo"`, son definidos en el archivo `messages.properties` y se cargan automáticamente en el controlador y en la entidad `Product`.

```properties
NotEmpty.product.name=es requerido
NotBlank.product.description=es requerido, por favor
NotNull.product.price=no puede ser nulo, ok
```

1. **`NotEmpty.product.name`**: Mensaje personalizado para el campo `name` en la entidad `Product`.
2. **`NotBlank.product.description`**: Mensaje personalizado para el campo `description` en la entidad `Product`.
3. **`NotNull.product.price`**: Mensaje personalizado para el campo `price` en la entidad `Product`.

#### **6.4. Resumen de la Validación con `@Valid`**

* **`@Valid`**: Esta anotación se usa para activar la validación de un objeto antes de que llegue al método del controlador. En este caso, validamos el `Product` al momento de recibirlo en las peticiones HTTP.

* **`BindingResult`**: Es un contenedor que mantiene los resultados de la validación y se usa para capturar cualquier error que ocurra durante la validación.

* **`@NotBlank`, `@NotNull`, `@Size`, `@Min`**: Son anotaciones de validación proporcionadas por `jakarta.validation.constraints`, que se usan para definir las reglas de validación de los campos.

#### **6.5. Beneficios de la Validación en Spring Boot**

1. **Centralización de reglas de validación**: La validación se realiza en la entidad, lo que permite tener reglas centralizadas.
2. **Mensajes personalizados**: Se pueden personalizar los mensajes de error y localizarlos a través de archivos `properties`.
3. **Simplicidad**: Las validaciones son automáticas y transparentes para el desarrollador, lo que reduce la cantidad de código repetitivo.

