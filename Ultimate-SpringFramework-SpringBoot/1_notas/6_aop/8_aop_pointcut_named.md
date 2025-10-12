# 8. Uso de `@Pointcut` con Métodos Nombrados

En Spring AOP, las expresiones de pointcut pueden extraerse a métodos separados mediante la anotación `@Pointcut`. Esto mejora la legibilidad, la reutilización y la mantenibilidad del código.

---

## 🎯 Objetivo

Definir **puntos de corte reutilizables** y referenciarlos en múltiples `advice` de forma clara y centralizada.

---

## 📁 Clase de Pointcuts: `GreetingServicePointcuts`

```java
@Aspect
@Component
public class GreetingServicePointcuts {

    @Pointcut("execution(* com.cultodeportivo.p7_springboot_aop.services.GreetingService.sayHello(..))")    
    public void greetingLoggerPointCut(){}

    @Pointcut("execution(* com.cultodeportivo.p7_springboot_aop.services.GreetingService.sayHelloError(..))")    
    public void greetingLoggerPointCutError(){}

    @Pointcut("execution(* com.cultodeportivo.p7_springboot_aop.services.GreetingService.sayHelloAround(..))")    
    public void greetingLoggerPointCutAround(){}
}
```

> 📌 Estos métodos no contienen lógica, solo definen expresiones pointcut reutilizables.

---

## 🧱 Uso en Aspecto: `GreetingAspect`

```java
@Before("GreetingServicePointcuts.greetingLoggerPointCut()")
public void loggerBefore(JoinPoint joinPoint) {
    // Se ejecuta antes de sayHello(...)
}

@AfterThrowing("GreetingServicePointcuts.greetingLoggerPointCutError()")
public void loggerAfterThrowing(JoinPoint joinPoint) {
    // Se ejecuta si sayHelloError(...) lanza una excepción
}

@Around("GreetingServicePointcuts.greetingLoggerPointCutAround()")
public Object loggerAround(ProceedingJoinPoint joinPoint) throws Throwable {
    // Se ejecuta alrededor de sayHelloAround(...)
}
```

---

## ✅ Ventajas de `@Pointcut` nombrado

| Beneficio                        | Descripción                                                   |
|----------------------------------|---------------------------------------------------------------|
| 🔄 Reutilización                 | Puedes reutilizar el mismo pointcut en múltiples `advice`.   |
| 🔍 Legibilidad                   | Nombres semánticos hacen el código más entendible.           |
| 🛠️ Mantenibilidad               | Facilita modificar una expresión de corte en un solo lugar.  |

---

## 📌 Notas importantes

- Los métodos `@Pointcut` **no deben tener cuerpo**.
- El nombre del método puede ser cualquier identificador válido.
- Puedes combinarlos con operadores lógicos `&&`, `||`, `!` si deseas combinar varios pointcuts.

---

## ✅ Conclusión

Definir pointcuts nombrados usando `@Pointcut` mejora la claridad, la organización del código AOP y facilita el mantenimiento a largo plazo. Es una práctica recomendada en proyectos medianos y grandes que usan múltiples aspectos y tipos de advice.
