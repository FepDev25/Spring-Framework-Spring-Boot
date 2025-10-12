# 4. Ejemplo de Implementación de AOP con `@Before`, `@AfterReturning` y `@After`

Este ejemplo demuestra cómo aplicar múltiples tipos de `Advice` para interceptar la ejecución de un método desde diferentes momentos: **antes**, **después de retornar**, y **después de completar la ejecución**.

---

## 🎯 Objetivo

Agregar lógica transversal de logging que se ejecute:
- Antes de la ejecución de un método.
- Después de que el método retorne con éxito.
- Después de que el método haya finalizado, sin importar su resultado.

---

## 📁 Aspecto: `GreetingAspect`

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
}
```

---

## 🔄 Flujo de ejecución

Al acceder al endpoint `/greeting`, se invoca el método:

```java
greetingService.sayHello("Felipe", "Hola cómo vas");
```

La ejecución se registra así:

```
INFO  GreetingAspect : Antes: sayHello, args: [Felipe, Hola cómo vas]
Hello Felipe, Hola cómo vas
INFO  GreetingAspect : Después de retornar: sayHello, args: [Felipe, Hola cómo vas]
INFO  GreetingAspect : Después: sayHello, args: [Felipe, Hola cómo vas]
```

---

## 🧠 Comportamiento de los `Advice`

| Advice              | Momento de ejecución                                  |
|---------------------|--------------------------------------------------------|
| `@Before`           | Antes del método objetivo                              |
| `@AfterReturning`   | Solo si el método retorna correctamente                |
| `@After`            | Siempre, tanto si retorna como si lanza excepción      |

> 📌 Este patrón permite capturar todos los puntos relevantes del ciclo de vida del método interceptado.

---

## ✅ Conclusión

Este ejemplo muestra cómo combinar distintos tipos de `Advice` para lograr un control completo sobre la ejecución de métodos con Spring AOP. Usar `@Before`, `@AfterReturning` y `@After` permite cubrir casi todos los escenarios comunes de logging y monitoreo sin modificar el código del servicio.
