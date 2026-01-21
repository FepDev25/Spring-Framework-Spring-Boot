# 2. Ejemplo de Implementación de AOP con `@Before`

En este ejemplo práctico implementamos un **aspecto** que intercepta la ejecución de un método del servicio `GreetingService` y ejecuta lógica adicional antes de la ejecución del método objetivo.

---

## Objetivo

Interceptar el método `sayHello(String, String)` antes de su ejecución y registrar los parámetros y nombre del método.

---

## Estructura de Clases

### Aspecto: `GreetingAspect`

```java
@Aspect
@Component
public class GreetingAspect {
    
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Before("execution(String com.cultodeportivo.p7_springboot_aop.services.GreetingService.sayHello(String, String))")
    public void loggerBefore(JoinPoint joinPoint) {
        String method = joinPoint.getSignature().getName();
        String args =  Arrays.toString(joinPoint.getArgs());
        logger.info("Antes: " + method + ", args: " + args);
    }
}
```

#### Explicación

- `@Aspect`: Marca esta clase como un **aspecto AOP**.
- `@Component`: Permite a Spring registrar el bean.
- `@Before(...)`: Define el **pointcut** con el método a interceptar antes de su ejecución.
- `JoinPoint`: Provee acceso al método interceptado y sus argumentos.
- `execution(...)`: Expresión del **pointcut** que especifica el método objetivo exacto:

  ```bash
  execution(String com.cultodeportivo.p7_springboot_aop.services.GreetingService.sayHello(String, String))
  ```

---

### Servicio: `GreetingServiceImp`

```java
@Service
public class GreetingServiceImp implements GreetingService {
    
    @Override
    public String sayHello(String person, String phrase) {
        String saludo = "Hello " + person + ", " + phrase;
        System.out.println(saludo);
        return saludo;
    }
}
```

- Método objetivo interceptado por el aspecto.

---

### Controlador: `GreetingController`

```java
@RestController
public class GreetingController {

    @Autowired
    private GreetingService greetingService;

    @GetMapping("/greeting")
    public ResponseEntity<?> getMethodName() {
        return ResponseEntity.ok(
            Map.of("greeting", greetingService.sayHello("Felipe", "Hola cómo vas"))
        );
    }
}
```

- Al acceder a `/greeting`, se invoca el método `sayHello(...)`.

---

## Flujo de Ejecución

1. Se hace una petición HTTP `GET /greeting`.
2. Spring invoca el método `sayHello("Felipe", "Hola cómo vas")`.
3. Antes de que `sayHello` se ejecute:
   - El aspecto `GreetingAspect` intercepta la llamada.
   - Se ejecuta el método `loggerBefore(...)`.
   - Se imprime en consola:

     ```bash
     INFO ... GreetingAspect: Antes: sayHello, args: [Felipe, Hola cómo vas]
     ```

4. Luego se ejecuta `sayHello(...)`, que retorna la cadena y la imprime en consola:

   ```bash
   Hello Felipe, Hola cómo vas
   ```

---

## Conclusión

Este ejemplo demuestra cómo utilizar `@Aspect` y `@Before` para interceptar métodos específicos y añadir lógica transversal como logging. El uso de AOP en Spring facilita la separación de responsabilidades y mantiene el código de negocio limpio y enfocado.
