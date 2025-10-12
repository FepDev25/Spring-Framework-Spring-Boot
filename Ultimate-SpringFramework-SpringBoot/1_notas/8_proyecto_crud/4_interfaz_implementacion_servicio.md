### **4. Interfaz e Implementación del Servicio: `ProductService` y `ProductServiceImpl`**

La clase `ProductService` es la interfaz que define los métodos de servicio que interactúan con la capa de repositorio para realizar operaciones sobre la entidad `Product`. La implementación de estos métodos se realiza en la clase `ProductServiceImpl`, que proporciona la lógica de negocio para manejar las operaciones CRUD de los productos.

#### **4.1. Interfaz `ProductService`**

La interfaz `ProductService` declara los métodos que proporcionarán la funcionalidad del servicio para gestionar productos. Estos métodos serán implementados en la clase `ProductServiceImpl`.

```java
package com.cultodeportivo.p10_springboot_crud.services;

import java.util.List;
import java.util.Optional;

import com.cultodeportivo.p10_springboot_crud.entities.Product;

public interface ProductService {

    List<Product> findAll();
    Optional<Product> findById(Long id);
    Product save(Product product);
    Optional<Product> update(Long id, Product product);
    Optional<Product> delete(Long id);

}
```

1. **`findAll()`**:

   * Recupera todos los productos de la base de datos.

2. **`findById(Long id)`**:

   * Busca un producto por su ID.

3. **`save(Product product)`**:

   * Guarda un nuevo producto o actualiza uno existente.

4. **`update(Long id, Product product)`**:

   * Actualiza un producto con el ID dado, si existe, con la nueva información proporcionada.

5. **`delete(Long id)`**:

   * Elimina un producto por su ID.

#### **4.2. Implementación `ProductServiceImpl`**

La clase `ProductServiceImpl` implementa la interfaz `ProductService` y proporciona la lógica concreta de las operaciones CRUD definidas en la interfaz. Utiliza el repositorio `ProductRepository` para interactuar con la base de datos.

```java
package com.cultodeportivo.p10_springboot_crud.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cultodeportivo.p10_springboot_crud.entities.Product;
import com.cultodeportivo.p10_springboot_crud.repositories.ProductRepository;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository repository;

    @Transactional(readOnly = true)
    @Override
    public List<Product> findAll() {
        return (List<Product>) repository.findAll();
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<Product> findById(Long id) {
        return repository.findById(id);
    }

    @Transactional
    @Override
    public Product save(Product product) {
        return repository.save(product);
    }

    @Transactional
    @Override
    public Optional<Product> update(Long id, Product product) {
        Optional<Product> productoOp = repository.findById(id);

        if (productoOp.isPresent()) {
            Product productDb = productoOp.orElseThrow();
            productDb.setName(product.getName());
            productDb.setPrice(product.getPrice());
            productDb.setDescription(product.getDescription());
            return Optional.of(repository.save(productDb));
        }

        return productoOp;
    }

    @Transactional
    @Override
    public Optional<Product> delete(Long id) {
        Optional<Product> productoOp = repository.findById(id);
        productoOp.ifPresent(productDb -> repository.delete(productDb));
        return productoOp;
    }
}
```

#### **4.3. Explicación de la Implementación:**

1. **`findAll()`**:

   * El método `findAll()` se marca con `@Transactional(readOnly = true)` para indicar que solo se realiza una lectura de la base de datos. Esto mejora el rendimiento y evita posibles bloqueos innecesarios en la base de datos.

2. **`findById(Long id)`**:

   * Este método busca un producto por su `id`. También está marcado como `@Transactional(readOnly = true)` porque solo recupera datos sin modificar nada.

3. **`save(Product product)`**:

   * Este método guarda un nuevo producto o actualiza uno existente. Está marcado con `@Transactional`, lo que significa que se ejecuta dentro de una transacción, asegurando que el producto se guarde correctamente en la base de datos.

4. **`update(Long id, Product product)`**:

   * El método `update()` primero busca el producto por su `id`. Si lo encuentra, actualiza los campos `name`, `price` y `description` con los valores del nuevo producto. Luego guarda el producto actualizado en la base de datos.
   * Si el producto no se encuentra, se devuelve un `Optional.empty()`.

5. **`delete(Long id)`**:

   * Este método busca un producto por su `id` y, si lo encuentra, lo elimina de la base de datos utilizando el repositorio `ProductRepository`.

#### **4.4. Uso de Transacciones (`@Transactional`)**

* La anotación `@Transactional` garantiza que las operaciones que se realizan dentro del método se ejecuten en una transacción. Si algo sale mal en el proceso (como una excepción lanzada), se revierte toda la transacción, asegurando que la base de datos no quede en un estado inconsistente.

* **`@Transactional(readOnly = true)`**:

  * Se utiliza en métodos que solo leen datos de la base de datos, lo que mejora el rendimiento, ya que Hibernate puede optimizar el acceso a la base de datos.

* **`@Transactional` sin parámetros**:

  * Se utiliza en métodos de escritura (como `save`, `update` y `delete`). Esto asegura que las modificaciones de la base de datos se gestionen adecuadamente, proporcionando coherencia y manejo de errores en caso de fallos.

#### **4.5. Resumen:**

La implementación de `ProductServiceImpl` proporciona la lógica para manejar productos a través de un conjunto de operaciones CRUD. Utiliza el repositorio `ProductRepository` para interactuar con la base de datos y gestiona las transacciones mediante la anotación `@Transactional`, garantizando la consistencia y el manejo adecuado de errores.

Este servicio es esencial en la capa de lógica de negocio, ya que abstrae la interacción directa con la base de datos y permite realizar operaciones de manera más sencilla y segura a través de los métodos `findAll()`, `findById()`, `save()`, `update()` y `delete()`.
