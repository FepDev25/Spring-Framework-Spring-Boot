# 5. Repositorios en Spring Data JPA

En esta sección se presentan los **repositorios** que permiten acceder y gestionar las entidades del sistema (`Product`, `User` y `Role`) a través de la infraestructura de **Spring Data JPA**. Estas interfaces extienden `CrudRepository`, lo que simplifica el desarrollo al proveer métodos listos para operaciones básicas como guardar, buscar, actualizar y eliminar.

---

## 📦 1. `ProductRepository`

```java
public interface ProductRepository extends CrudRepository<Product, Long> {
    boolean existsBySku(String sku);
}
```

### ✅ Funcionalidades:

* Hereda todos los métodos CRUD para `Product` (`findById`, `save`, `deleteById`, etc.).
* Método personalizado:

  * `existsBySku(String sku)`: retorna `true` si existe un producto con el `sku` proporcionado.

> 🧠 Útil para validar unicidad del `sku` antes de guardar.

---

## 📦 2. `RoleRepository`

```java
public interface RoleRepository extends CrudRepository<Role, Long> {
    Optional<Role> findByName(String name);
}
```

### ✅ Funcionalidades:

* Hereda todos los métodos CRUD para `Role`.
* Método personalizado:

  * `findByName(String name)`: busca un rol específico por su nombre (como `ROLE_USER` o `ROLE_ADMIN`).

> 🧠 Se utiliza comúnmente durante el registro o autenticación para asignar o verificar roles.

---

## 📦 3. `UserRepository`

```java
public interface UserRepository extends CrudRepository<User, Long> {
    boolean existsByUsername(String username);
    Optional<User> findByUsername(String username);
}
```

### ✅ Funcionalidades:

* Permite gestionar usuarios con operaciones básicas.
* Métodos personalizados:

  * `existsByUsername(String username)`: verifica si un usuario ya existe (para validaciones).
  * `findByUsername(String username)`: recupera el usuario por su nombre, fundamental para procesos de autenticación con Spring Security.

> 🧠 Estos métodos permiten validar unicidad y cargar datos del usuario en procesos de login.

---

## 🧠 ¿Por qué `CrudRepository`?

| Característica          | Beneficio                                                                                                                                              |
| ----------------------- | ------------------------------------------------------------------------------------------------------------------------------------------------------ |
| `CrudRepository<T, ID>` | Proporciona métodos CRUD genéricos sin necesidad de implementación manual.                                                                             |
| Tipado genérico         | Permite reutilizar la lógica con cualquier entidad (`Product`, `User`, `Role`).                                                                        |
| Personalización         | Es posible declarar métodos con nombres basados en convención (`findBy...`, `existsBy...`, etc.) para que Spring genere automáticamente las consultas. |

---

## ✅ Conclusión

El uso de repositorios en Spring Data JPA elimina la necesidad de escribir consultas SQL básicas. Permite crear métodos de búsqueda personalizados usando convenciones de nombres, manteniendo el código limpio, expresivo y alineado con la lógica de negocio. Esta base será fundamental para construir servicios, validaciones y controladores seguros en la aplicación.
