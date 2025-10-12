# 3. Entidad `User` – Modelo de Usuario en el Sistema

La entidad `User` representa a los usuarios del sistema en la capa de persistencia. Esta clase está directamente mapeada a la tabla `users` en la base de datos y contiene la información necesaria para la autenticación y autorización de usuarios en el contexto de Spring Security con JWT.

---

## 🧱 Entidad: `User`

```java
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String username;

    private String password;
    private Boolean enable;

    @Transient
    private boolean admin;

    @ManyToMany
    @JoinTable(
        name = "users_roles",
        joinColumns = @JoinColumn(name = "user_id"),
        inverseJoinColumns = @JoinColumn(name = "role_id"),
        uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "role_id"})
    )
    private List<Role> roles;
}
```

---

## 🔍 Atributos y Comportamiento

| Campo      | Descripción                                                                                                     |
| ---------- | --------------------------------------------------------------------------------------------------------------- |
| `id`       | Identificador único del usuario (clave primaria).                                                               |
| `username` | Nombre de usuario único. Es la credencial principal para el login.                                              |
| `password` | Contraseña cifrada del usuario. Marcada como de solo escritura en JSON.                                         |
| `enable`   | Bandera que indica si el usuario está habilitado. Se inicializa como `true` por defecto mediante `@PrePersist`. |
| `admin`    | Campo transitorio no persistido. Útil para lógica auxiliar como diferenciar admins al crear el usuario.         |
| `roles`    | Lista de roles asociados mediante una relación `@ManyToMany` con la tabla `users_roles`.                        |

---

## 🔐 Mapeo de Roles

La relación entre usuarios y roles se implementa mediante una tabla intermedia `users_roles`. Esto permite que cada usuario tenga múltiples roles, y cada rol pueda ser compartido por varios usuarios.

```java
@ManyToMany
@JoinTable(
    name = "users_roles",
    joinColumns = @JoinColumn(name = "user_id"),
    inverseJoinColumns = @JoinColumn(name = "role_id"),
    uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "role_id"})
)
private List<Role> roles;
```

---

## 🧠 Anotaciones Notables

| Anotación                   | Propósito                                                |
| --------------------------- | -------------------------------------------------------- |
| `@Entity`                   | Marca esta clase como una entidad JPA.                   |
| `@Table(name = "users")`    | La vincula con la tabla `users`.                         |
| `@Id`, `@GeneratedValue`    | Define el identificador único con generación automática. |
| `@Transient`                | Excluye el campo `admin` de la persistencia.             |
| `@PrePersist`               | Inicializa `enable` como `true` al crear el usuario.     |
| `@JsonProperty(WRITE_ONLY)` | Oculta el password al serializar respuestas JSON.        |
| `@JsonIgnoreProperties`     | Evita problemas con la carga perezosa de roles.          |

---

## ♻️ Métodos sobrescritos

* `equals()` y `hashCode()` se implementan para comparar usuarios correctamente según su `id` y `username`, lo cual es útil en colecciones y operaciones lógicas.

---

## ✅ Conclusión

La clase `User` constituye el núcleo del sistema de autenticación. Define la información necesaria para que Spring Security gestione usuarios y sus permisos a través de la relación con `Role`. La estructura está preparada para una implementación segura, flexible y fácilmente escalable de autenticación basada en JWT.
