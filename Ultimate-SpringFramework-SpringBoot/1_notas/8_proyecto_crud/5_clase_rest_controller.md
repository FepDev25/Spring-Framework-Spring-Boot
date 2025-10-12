### **6. `ProductController` - REST Controller para la Entidad `Product`**

El controlador REST `ProductController` maneja las operaciones CRUD básicas para la entidad `Product`, exponiendo los métodos a través de endpoints HTTP que permiten interactuar con el servicio correspondiente (`ProductService`). Los métodos soportan operaciones como crear, leer, actualizar y eliminar productos, y también realizan validaciones a nivel de los datos de entrada, las cuales serán tratadas en un subtema posterior.

#### **6.1. Explicación del Código**

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
```

1. **Anotaciones:**

   * **`@RestController`**: Marca la clase como un controlador REST de Spring, lo que significa que los métodos de esta clase estarán expuestos como servicios web. La respuesta será automáticamente convertida a JSON u otros formatos de acuerdo con el tipo de cliente.
   * **`@RequestMapping("/api/productos")`**: Define la ruta base para todas las operaciones en este controlador. Todas las peticiones a `/api/productos` serán gestionadas por este controlador.

2. **Inyección de Dependencias:**

   * **`@Autowired`**: Utilizado para inyectar el servicio `ProductService`, que contiene la lógica de negocio de los productos.

#### **6.2. Métodos del Controlador**

1. **`@GetMapping` - Listar todos los productos:**

   ```java
   @GetMapping
   public List<Product> list(){
       return service.findAll();
   }
   ```

   * **`@GetMapping`**: Anotación que mapea una petición GET a este método. Este endpoint permite obtener todos los productos almacenados en la base de datos.
   * **`service.findAll()`**: Llama al método `findAll()` del servicio para recuperar todos los productos.

2. **`@GetMapping("/{id}")` - Ver un producto por ID:**

   ```java
   @GetMapping("/{id}")
   public ResponseEntity<?> view(@PathVariable Long id) {
       Optional<Product> opt = service.findById(id);
       if (opt.isPresent()) {
           return ResponseEntity.ok(opt.orElseThrow());
       }
       return ResponseEntity.notFound().build();
   }
   ```

   * **`@GetMapping("/{id}")`**: Mapea las peticiones GET con un parámetro dinámico (`id`) en la URL.
   * **`service.findById(id)`**: Busca el producto por su `id`. Si el producto es encontrado, se devuelve con el código de estado `200 OK`, de lo contrario, se responde con `404 Not Found`.

3. **`@PostMapping` - Crear un nuevo producto:**

   ```java
   @PostMapping("")
   public ResponseEntity<?> create(@Valid @RequestBody Product product, BindingResult result) {
       if (result.hasFieldErrors()) {
           return validation(result);
       }
       Product newProduct = service.save(product);
       return ResponseEntity.status(HttpStatus.CREATED).body(newProduct);
   }
   ```

   * **`@PostMapping`**: Mapea las peticiones POST, que se utilizan para crear un nuevo recurso. En este caso, un nuevo producto.
   * **`@RequestBody`**: Indica que el objeto `product` se extrae del cuerpo de la petición, que debe estar en formato JSON.
   * **`@Valid`**: Activa las validaciones sobre el objeto `Product` (validaciones que definimos en la entidad `Product`).
   * **`BindingResult result`**: Almacena los errores de validación que ocurren durante el proceso de enlace de datos (binding) entre el cuerpo de la petición y el objeto `Product`.
   * **`validation(result)`**: Si hay errores de validación, se llama al método `validation()` para devolver los errores al cliente con una respuesta de error adecuada.
   * Si la validación es exitosa, se guarda el producto utilizando `service.save(product)` y se devuelve el producto recién creado con un estado `201 CREATED`.

4. **`@PutMapping("/{id}")` - Actualizar un producto:**

   ```java
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
   ```

   * **`@PutMapping("/{id}")`**: Mapea las peticiones PUT, que se utilizan para actualizar un recurso existente. En este caso, un producto.
   * **`service.update(id, product)`**: Llama al método `update()` del servicio, pasando el `id` y el nuevo objeto `product` para actualizar los datos.
   * Si el producto es actualizado exitosamente, se devuelve el producto actualizado con un estado `201 CREATED`. Si no se encuentra el producto, se devuelve `404 Not Found`.

5. **`@DeleteMapping("/{id}")` - Eliminar un producto:**

   ```java
   @DeleteMapping("/{id}")
   public ResponseEntity<?> delete(@PathVariable Long id) {
       Optional<Product> opt = service.delete(id);
       if (opt.isPresent()) {
           return ResponseEntity.ok(opt.orElseThrow());
       }
       return ResponseEntity.notFound().build();
   }
   ```

   * **`@DeleteMapping("/{id}")`**: Mapea las peticiones DELETE para eliminar un recurso. En este caso, elimina un producto por su `id`.
   * **`service.delete(id)`**: Llama al método `delete()` del servicio para eliminar el producto por su `id`.
   * Si el producto se elimina con éxito, se responde con el producto eliminado. Si no se encuentra el producto, se devuelve `404 Not Found`.

#### **6.3. Método `validation`**

Este método se encarga de devolver los errores de validación si existen. Si algún campo no cumple con las restricciones definidas en la entidad, se agregan al mapa de errores y se devuelven como respuesta.

```java
private ResponseEntity<?> validation(BindingResult result) {
    Map<String, String> errors = new HashMap<>();
    result.getFieldErrors().forEach(error -> {
        errors.put(error.getField(), "El campo " + error.getField() + " " + error.getDefaultMessage());
    });
    return ResponseEntity.badRequest().body(errors);
}
```

#### **6.4. Resumen de las Funcionalidades**

* **Creación (`POST`)**: Crea un nuevo producto con validación de los campos.
* **Lectura (`GET`)**: Permite obtener un producto por ID o todos los productos.
* **Actualización (`PUT`)**: Permite actualizar un producto existente.
* **Eliminación (`DELETE`)**: Permite eliminar un producto por ID.
