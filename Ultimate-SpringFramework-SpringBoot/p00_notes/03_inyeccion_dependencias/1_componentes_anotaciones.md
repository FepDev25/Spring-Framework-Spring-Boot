# 1. Componentes y Anotaciones Principales en Spring Boot

Spring Boot utiliza un conjunto de anotaciones que permiten construir aplicaciones web de forma rápida y con poco código. A continuación, se describen los principales componentes y anotaciones que forman la base de cualquier aplicación Spring.

---

## Principales Componentes

### 1. `@Component`

- Marca una clase como un **componente gestionado por Spring**.
- Se detecta automáticamente mediante el escaneo de componentes (`component scanning`).
- Superclase de otras anotaciones especializadas.

```java
@Component
public class MiComponente {
    public void saludar() {
        System.out.println("Hola desde un componente!");
    }
}
```

---

### 2. `@Service`

- Es una especialización de `@Component`.
- Indica que la clase contiene **lógica de negocio**.
- Se usa típicamente en la capa de servicios.

```java
@Service
public class ProductoService {
    public List<String> obtenerProductos() {
        return List.of("Producto A", "Producto B");
    }
}
```

---

### 3. `@Repository`

- También hereda de `@Component`.
- Se usa para la **capa de acceso a datos (DAO)**.
- Ofrece funcionalidades extra como el manejo automático de excepciones de base de datos.

```java
@Repository
public class ProductoRepository {
    // Simulación de acceso a datos
}
```

---

### 4. `@Controller` y `@RestController`

- `@Controller`: define una clase que manejará peticiones web y puede devolver vistas (MVC).
- `@RestController`: combinación de `@Controller` + `@ResponseBody`; se usa para APIs REST.

```java
@RestController
public class SaludoController {

    @GetMapping("/saludo")
    public String saludar() {
        return "Hola desde Spring Boot";
    }
}
```

---

### 5. `@Autowired`

- Permite la **inyección automática de dependencias**.
- Se puede usar sobre atributos, constructores o métodos.

```java
@RestController
public class ClienteController {

    @Autowired
    private ClienteService clienteService;
}
```

---

## Otras Anotaciones Comunes

- `@SpringBootApplication`: punto de entrada principal; combina `@Configuration`, `@EnableAutoConfiguration` y `@ComponentScan`.
- `@GetMapping`, `@PostMapping`, `@PutMapping`, `@DeleteMapping`: manejan diferentes tipos de solicitudes HTTP.
- `@RequestParam`: obtiene valores desde la URL o formulario.
- `@PathVariable`: obtiene valores dinámicos desde la URL.

---

## Conclusión

Estas anotaciones permiten que Spring Boot configure automáticamente los componentes, servicios y controladores necesarios. Gracias a esto, se puede construir una aplicación robusta con muy poco código inicial, enfocándose más en la lógica del negocio que en la configuración.
