# 7. Orden de Ejecución de Aspectos en Spring AOP

En Spring AOP, cuando existen múltiples aspectos (`@Aspect`) que apuntan a los mismos métodos (mismo pointcut), el orden de ejecución puede ser controlado mediante la anotación `@Order`.

---

## 🎯 Objetivo

Controlar la prioridad con la que se ejecutan los aspectos cuando hay más de uno aplicado sobre el mismo método objetivo.

---

## 🧱 Aspectos Definidos

### 📁 Aspecto 1: `GreetingFooAspect`

```java
@Order(1)
@Aspect
@Component
public class GreetingFooAspect {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Before("execution(* com.cultodeportivo.p7_springboot_aop.services.GreetingService.*(..))")
    public void loggerBefore(JoinPoint joinPoint) {
        String method = joinPoint.getSignature().getName();
        String args = Arrays.toString(joinPoint.getArgs());
        logger.info("Antes foo: " + method + ", args: " + args);
    }
}
```

---

### 📁 Aspecto 2: `GreetingAspect`

```java
@Order(2)
@Aspect
@Component
public class GreetingAspect {

    @Before("execution(* com.cultodeportivo.p7_springboot_aop.services.GreetingService.sayHello(..))")
    public void loggerBefore(JoinPoint joinPoint) {
        logger.info("Antes: " + joinPoint.getSignature().getName() + ", args: " + Arrays.toString(joinPoint.getArgs()));
    }

    @AfterReturning("execution(* com.cultodeportivo.p7_springboot_aop.services.GreetingService.sayHello(..))")
    public void loggerAfterReturning(JoinPoint joinPoint) {
        logger.info("Después de retornar: " + joinPoint.getSignature().getName());
    }

    @After("execution(* com.cultodeportivo.p7_springboot_aop.services.GreetingService.sayHello(..))")
    public void loggerAfter(JoinPoint joinPoint) {
        logger.info("Después: " + joinPoint.getSignature().getName());
    }
}
```

---

## 🔄 Resultado del Orden de Ejecución

Cuando se ejecuta el método `sayHello(...)`, se observa:

```
INFO GreetingFooAspect : Antes foo: sayHello, args: [...]
INFO GreetingAspect    : Antes: sayHello, args: [...]
Hello Felipe, Hola cómo vas
INFO GreetingAspect    : Después de retornar: sayHello
INFO GreetingAspect    : Después: sayHello
```

---

## 🧩 ¿Cómo funciona `@Order`?

- **`@Order(1)`** se ejecuta antes que **`@Order(2)`**.
- Los valores más bajos tienen mayor prioridad.
- Si no se especifica `@Order`, el orden no está garantizado.

---

## ✅ Conclusión

El uso de `@Order` es esencial cuando necesitas controlar la secuencia de ejecución entre múltiples aspectos, asegurando que ciertas acciones se realicen antes o después de otras de forma consistente. Esto es especialmente útil en tareas de seguridad, validación y auditoría.
