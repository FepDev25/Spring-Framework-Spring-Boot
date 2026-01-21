# 3. Anotaciones en Spring Boot (Profundización)

Spring Boot utiliza anotaciones especializadas para marcar clases con roles específicos dentro de la arquitectura. Estas anotaciones son variantes de `@Component`, por lo que también son detectadas automáticamente por el escaneo de componentes.

---

## 3.1 `@Repository`

- Se utiliza para marcar la capa de acceso a datos.
- Permite a Spring manejar **excepciones específicas de base de datos** y convertirlas en excepciones del tipo `DataAccessException`.
- Ideal para clases que implementan consultas con JPA, JDBC o cualquier motor de persistencia.

```java
@Repository
public class ProductRepositoryImpl implements ProductRepository {
    // Implementación de métodos para acceder a los datos
}
```

> Spring Data JPA también permite definir interfaces que extienden `JpaRepository`, y no necesitas implementar métodos básicos.

---

## 3.2 `@Service`

- Representa la **lógica de negocio**.
- Es una especialización de `@Component`, usada para la capa de servicio.
- Se recomienda su uso para separar la lógica del controlador y del acceso a datos.

```java
@Service
public class ProductService {

    private final ProductRepository repository;

    public ProductService(ProductRepository repository) {
        this.repository = repository;
    }

    public List<Product> obtenerTodos() {
        return repository.findAll();
    }
}
```

> Aporta claridad semántica al proyecto indicando que esta clase tiene reglas de negocio.

---

## 3.3 `@RestController`

- Es una combinación de `@Controller` + `@ResponseBody`.
- Se utiliza para exponer **servicios RESTful**, devolviendo respuestas JSON directamente.
- No necesita usar `@ResponseBody` en cada método.

```java
@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService service;

    public ProductController(ProductService service) {
        this.service = service;
    }

    @GetMapping
    public List<Product> getAllProducts() {
        return service.obtenerTodos();
    }
}
```

> Muy usado en arquitecturas REST, ideal para proyectos API-first.

---

## Conclusión

Las anotaciones `@Repository`, `@Service` y `@RestController` ayudan a mantener una separación clara de responsabilidades dentro de una aplicación Spring Boot. Además de mejorar la legibilidad del código, permiten que el framework gestione de forma automática múltiples aspectos como la inyección de dependencias, conversión de excepciones o generación de respuestas JSON.
