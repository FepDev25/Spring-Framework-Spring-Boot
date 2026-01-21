# 7. Implementación de Servicios

En esta sección se implementan las interfaces `ProductService` y `UserService`, que definen la lógica de negocio de los productos y usuarios, respectivamente. Las clases `ProductServiceImpl` y `UserServiceImpl` están anotadas con `@Service` y utilizan anotaciones de transacción para asegurar la consistencia de datos.

---

## 1. `ProductServiceImpl`

```java
@Service
public class ProductServiceImpl implements ProductService {
    
    @Autowired
    private ProductRepository repository;
    
    @Transactional(readOnly = true)
    public List<Product> findAll() { ... }

    @Transactional(readOnly = true)
    public Optional<Product> findById(Long id) { ... }

    @Transactional
    public Product save(ProductDTO product) { ... }

    @Transactional
    public Optional<Product> update(Long id, ProductDTO product) { ... }

    @Transactional
    public Optional<Product> delete(Long id) { ... }

    @Transactional(readOnly = true)
    public boolean existsBySku(String sku) { ... }
}
```

### Detalles destacados

* **Transformación DTO → Entidad**: En los métodos `save` y `update`, se recibe un `ProductDTO`, se mapea a una instancia de `Product` y luego se guarda en la base de datos.
* **Eliminación segura**: `delete()` verifica si el producto existe antes de eliminarlo.
* **Transacciones**:

  * `@Transactional(readOnly = true)` para consultas.
  * `@Transactional` para operaciones que modifican datos.

---

## 2. `UserServiceImpl`

```java
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RoleRepository roleService;

    @Transactional(readOnly = true)
    public List<User> findAll() { ... }

    @Transactional
    public User save(User user) { ... }

    @Transactional(readOnly = true)
    public boolean existsByUsername(String username) { ... }
}
```

### Lógica del método `save(User)`

1. **Asignación de roles**:

   * Se asigna por defecto el rol `ROLE_USER`.
   * Si el atributo `admin` del usuario es `true`, también se le asigna `ROLE_ADMIN`.

2. **Encriptación de contraseña**:

   * Antes de guardar, la contraseña es codificada con un `PasswordEncoder` (e.g., BCrypt).

3. **Persistencia**:

   * El usuario se guarda con sus roles y contraseña encriptada.

### Extras

* `System.out.println(...)` en `existsByUsername` puede usarse para depuración, pero debe evitarse en producción (se reemplazará con logging más adelante).

---

## Buenas prácticas observadas

| Práctica                                 | Explicación                                                         |
| ---------------------------------------- | ------------------------------------------------------------------- |
| Separación DTO ↔ Entidad                 | Se evita exponer directamente las entidades desde el frontend.      |
| Capa de servicio desacoplada             | Se mantiene una lógica clara entre el repositorio y el controlador. |
| Uso de transacciones                     | Asegura la integridad de los datos y mejora el rendimiento.         |
| Inyección de dependencias (`@Autowired`) | Permite que Spring gestione las instancias necesarias.              |

---

## Conclusión

La implementación de los servicios encapsula toda la lógica necesaria para gestionar productos y usuarios en el sistema. Esta capa es esencial para aplicar reglas de negocio como validación de roles, encriptación de contraseñas y transformación de datos, todo ello de forma coherente, segura y desacoplada del controlador.
