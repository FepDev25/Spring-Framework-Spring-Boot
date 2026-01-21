# 2. Repositorios en JPA: CrudRepository vs JpaRepository

## ¿Qué contiene JPA?

JPA define un conjunto de **anotaciones** y **interfaces** que permiten mapear clases Java a tablas de bases de datos, así como manipular objetos persistentes de forma sencilla. Entre sus elementos más importantes se encuentran:

- **Anotaciones**: `@Entity`, `@Table`, `@Id`, `@GeneratedValue`, `@Column`, `@ManyToOne`, `@OneToMany`, etc.
- **EntityManager**: Interfaz clave para realizar operaciones de persistencia (persist, find, remove, merge...).
- **JPQL**: Lenguaje de consulta orientado a objetos.
- **Repositorios Spring Data**: Abstracciones para manejar CRUD sin escribir consultas SQL.

---

## CrudRepository vs JpaRepository

| Característica          | `CrudRepository`                                     | `JpaRepository` (hereda de CrudRepository)             |
|-------------------------|------------------------------------------------------|--------------------------------------------------------|
| Funcionalidad básica    | Operaciones CRUD (crear, leer, actualizar, eliminar) | Incluye todo lo de `CrudRepository` y más              |
| Métodos adicionales     | No                                                   | Sí: `findAll(Sort sort)`, `findAll(Pageable p)`, etc.  |
| Herencia                | Interface base en Spring Data                        | Extiende a `PagingAndSortingRepository`                |
| Paginación/Ordenamiento | No                                                   | Sí, integrada                                          |

> Nota: En general, se recomienda usar `JpaRepository` ya que incluye todas las funcionalidades de `CrudRepository` y más.

---

## Métodos por defecto (`CrudRepository` y `JpaRepository`)

- `save(S entity)`  
- `saveAll(Iterable<S> entities)`  
- `findById(ID id)`  
- `findAll()`  
- `deleteById(ID id)`  
- `deleteAll()`  
- `count()`  
- `existsById(ID id)`

Ejemplo:

```java
public interface UserRepository extends JpaRepository<User, Long> {
}
```

Uso en un servicio:

```java
userRepository.save(new User("Carlos", "carlos@example.com"));
List<User> todos = userRepository.findAll();
```

---

## Métodos personalizados por convención

Spring Data JPA permite crear métodos personalizados **sin escribir consultas SQL o JPQL**, solo siguiendo el nombre del método:

```java
List<User> findByEmail(String email);
List<User> findByNameContaining(String fragmento);
List<User> findByEmailAndName(String email, String name);
List<User> findTop3ByOrderByNameAsc();
```

> Nota: Spring construye automáticamente la consulta basada en el nombre del método.

---

## Otros ejemplos útiles

| Método personalizado                    | Resultado esperado                       |
|-----------------------------------------|------------------------------------------|
| `findByNombre(String nombre)`           | Usuarios con ese nombre                  |
| `findByEdadGreaterThan(int edad)`       | Usuarios con edad mayor a cierto valor   |
| `findByActivoTrue()`                    | Usuarios activos                         |
| `findByNombreLike(String patron)`       | Búsqueda con comodines (`%`)             |
| `findFirstByOrderByFechaCreacionDesc()` | Último usuario creado (por fecha)        |
| `deleteByNombre(String nombre)`         | Elimina usuarios con ese nombre          |
| `existsByEmail(String email)`           | Verifica si hay un usuario con ese email |
| `countByActivoFalse()`                  | Cuenta los usuarios inactivos            |

---

## Conclusión

Los repositorios de Spring Data JPA ofrecen una forma poderosa y concisa de acceder a la base de datos sin necesidad de escribir código SQL explícito. `JpaRepository` es la opción más completa, mientras que los métodos personalizados ofrecen gran flexibilidad al construir consultas complejas con solo definir nombres descriptivos.
