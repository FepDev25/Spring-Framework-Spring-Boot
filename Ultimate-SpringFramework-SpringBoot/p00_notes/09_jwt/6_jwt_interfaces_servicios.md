# 6. Interfaces de Servicios en Spring

Las **interfaces de servicios** definen la lógica de negocio que la aplicación debe implementar, permitiendo desacoplar la definición de las operaciones de su implementación concreta. En este caso, se manejan dos servicios principales: `UserService` y `ProductService`.

---

## 1. Interfaz `UserService`

```java
public interface UserService {
    List<User> findAll();
    User save(User user);
    boolean existsByUsername(String username);
}
```

### Métodos definidos

| Método                     | Descripción                                        |
| -------------------------- | -------------------------------------------------- |
| `findAll()`                | Retorna todos los usuarios registrados.            |
| `save(User user)`          | Guarda un nuevo usuario o actualiza uno existente. |
| `existsByUsername(String)` | Verifica si un `username` ya está registrado.      |

> Esta interfaz define el contrato para operaciones relacionadas con la gestión de usuarios. Es usada tanto en procesos de registro como en la administración interna.

---

## 2. Interfaz `ProductService`

```java
public interface ProductService {
    List<Product> findAll();
    Optional<Product> findById(Long id);
    Product save(ProductDTO product);
    Optional<Product> update(Long id, ProductDTO product);
    Optional<Product> delete(Long id);
    boolean existsBySku(String sku);
}
```

### Métodos definidos ProductService

| Método                                | Descripción                                  |
| ------------------------------------- | -------------------------------------------- |
| `findAll()`                           | Lista todos los productos disponibles.       |
| `findById(Long id)`                   | Busca un producto específico por su ID.      |
| `save(ProductDTO product)`            | Guarda un nuevo producto a partir de un DTO. |
| `update(Long id, ProductDTO product)` | Actualiza un producto existente.             |
| `delete(Long id)`                     | Elimina un producto si existe.               |
| `existsBySku(String sku)`             | Verifica si el `sku` ya existe.              |

> `ProductService` usa un `DTO` como parámetro, lo cual permite aplicar validaciones y transformar los datos antes de persistirlos. Esto ayuda a proteger la entidad `Product`.

---

## ¿Por qué usar interfaces?

* **Desacoplamiento**: permite cambiar la implementación sin afectar a los controladores.
* **Inyección de dependencias**: Spring puede gestionar automáticamente la implementación.
* **Testabilidad**: facilita la creación de pruebas unitarias con mocks.

---

## Conclusión

Las interfaces `UserService` y `ProductService` definen la lógica central para gestionar usuarios y productos en la aplicación. Al mantenerlas separadas de su implementación, se promueve una arquitectura limpia, escalable y fácil de mantener.
