# 5. Ejemplo de Implementación de AOP con `@AfterThrowing`

Este ejemplo demuestra cómo capturar e interceptar errores lanzados desde un método utilizando el `Advice` `@AfterThrowing`, permitiendo realizar tareas como logging de excepciones, auditorías, o alertas.

---

## Objetivo

Registrar un mensaje específico cuando un método lanza una excepción durante su ejecución.

---

## Aspecto: `GreetingAspect`

```java
@Aspect
@Component
public class GreetingAspect {
    
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Before("execution(* com.cultodeportivo.p7_springboot_aop.services.GreetingService.*(..))")
    public void loggerBefore(JoinPoint joinPoint) {
        String method = joinPoint.getSignature().getName();
        String args = Arrays.toString(joinPoint.getArgs());
        logger.info("Antes: " + method + ", args: " + args);
    }

    @After("execution(* com.cultodeportivo.p7_springboot_aop.services.GreetingService.*(..))")
    public void loggerAfter(JoinPoint joinPoint) {
        String method = joinPoint.getSignature().getName();
        String args = Arrays.toString(joinPoint.getArgs());
        logger.info("Después: " + method + ", args: " + args);
    }

    @AfterReturning("execution(* com.cultodeportivo.p7_springboot_aop.services.GreetingService.*(..))")
    public void loggerAfterReturning(JoinPoint joinPoint) {
        String method = joinPoint.getSignature().getName();
        String args = Arrays.toString(joinPoint.getArgs());
        logger.info("Después de retornar: " + method + ", args: " + args);
    }

    @AfterThrowing("execution(* com.cultodeportivo.p7_springboot_aop.services.GreetingService.*(..))")
    public void loggerAfterThrowing(JoinPoint joinPoint) {
        String method = joinPoint.getSignature().getName();
        String args = Arrays.toString(joinPoint.getArgs());
        logger.info("Después de lanzar una excepción: " + method + ", args: " + args);
    }
}
```

---

## Servicio: `GreetingServiceImp`

```java
@Service
public class GreetingServiceImp implements GreetingService {

    @Override
    public String sayHello(String person, String phrase) {
        String saludo = "Hello " + person + ", " + phrase;
        System.out.println(saludo);
        return saludo;
    }

    @Override
    public String sayHelloError(String person, String phrase) {
        throw new RuntimeException("Algún error.");
    }
}
```

- `sayHelloError(...)` lanza intencionalmente una excepción para probar el `@AfterThrowing`.

---

## Resultado en consola

Cuando se llama a `sayHelloError("Felipe", "Hola cómo vas")`, se observa:

```bash
INFO  GreetingAspect : Antes: sayHelloError, args: [Felipe, Hola cómo vas]
INFO  GreetingAspect : Después de lanzar una excepción: sayHelloError, args: [Felipe, Hola cómo vas]
INFO  GreetingAspect : Después: sayHelloError, args: [Felipe, Hola cómo vas]
ERROR DispatcherServlet : Request processing failed: java.lang.RuntimeException: Algún error.
```

---

## Comportamiento

| Advice              | Se ejecuta si...                       |
|---------------------|----------------------------------------|
| `@Before`           | Siempre, antes del método              |
| `@AfterThrowing`    | Solo si el método lanza una excepción  |
| `@After`            | Siempre, al finalizar (éxito o fallo)  |

---

## Conclusión

`@AfterThrowing` permite capturar y reaccionar ante errores de forma elegante y centralizada. Combinado con otros tipos de advice, proporciona un control total sobre el ciclo de vida del método, manteniendo el código del servicio limpio y sin lógica de manejo de errores innecesaria.
