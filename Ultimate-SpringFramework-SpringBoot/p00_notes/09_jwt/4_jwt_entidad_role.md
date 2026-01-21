# 4. Entidad `Role` – Modelo de Roles para Autenticación

La entidad `Role` representa los permisos o roles asignables a los usuarios dentro del sistema. Se implementa como parte de una relación `@ManyToMany` con la entidad `User`, lo que permite que un usuario tenga múltiples roles y que un rol pueda estar asociado a múltiples usuarios.

---

## Entidad: `Role`

```java
@Entity
@Table(name = "roles")
public class Role {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String name;

    @ManyToMany(mappedBy = "roles")
    private List<User> users;
}
```

---

## Atributos y Relaciones

| Campo   | Descripción                                                                                              |
| ------- | -------------------------------------------------------------------------------------------------------- |
| `id`    | Identificador único del rol (clave primaria autogenerada).                                               |
| `name`  | Nombre del rol (ej. `ROLE_ADMIN`, `ROLE_USER`). Debe ser único.                                          |
| `users` | Lista de usuarios que tienen asignado este rol. Inverso de la relación `@ManyToMany` en la clase `User`. |

---

## Relación con `User`

Esta relación es bidireccional. Mientras que en `User` se declara con `@JoinTable`, aquí se referencia con `mappedBy = "roles"`.

```java
@ManyToMany(mappedBy = "roles")
private List<User> users;
```

* `@JsonIgnoreProperties`: evita problemas de serialización como referencias cíclicas o conflictos con proxies de Hibernate.

---

## Anotaciones Notables

| Anotación                         | Propósito                                                   |
| --------------------------------- | ----------------------------------------------------------- |
| `@Entity`                         | Marca la clase como entidad JPA.                            |
| `@Table(name = "roles")`          | La vincula con la tabla `roles`.                            |
| `@Column(unique = true)`          | Impone una restricción de unicidad sobre el nombre del rol. |
| `@ManyToMany(mappedBy = "roles")` | Define el lado inverso de la relación con `User`.           |

---

## Métodos sobrescritos

* `equals()` y `hashCode()` se sobrescriben para comparar roles basados en `id` y `name`, lo que es útil para evitar duplicados en listas o sets, y para búsquedas confiables en memoria.

---

## Conclusión

La clase `Role` proporciona la estructura necesaria para la gestión de autorizaciones basadas en roles dentro de la aplicación. Su relación bidireccional con la entidad `User` permite una gestión flexible y coherente de los permisos, compatible con Spring Security. Esta implementación es extensible a roles personalizados o jerarquías en aplicaciones más complejas.
