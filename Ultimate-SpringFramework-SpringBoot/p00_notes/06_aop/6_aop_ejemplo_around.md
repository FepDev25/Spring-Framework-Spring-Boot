# 6. Ejemplo de Implementación de AOP con `@Around`

El tipo de `Advice` más poderoso en AOP es `@Around`, ya que **rodea** completamente la ejecución del método objetivo. Esto permite ejecutar lógica **antes y después**, manejar errores, y **controlar directamente si el método se ejecuta o no**.

---

## Objetivo

Interceptar el método `sayHelloAround(...)`, ejecutar lógica antes y después, capturar su resultado, y manejar posibles excepciones.

---

## Aspecto: `GreetingAspect`

```java
@Around("execution(* com.cultodeportivo.p7_springboot_aop.services.GreetingService.sayHelloAround(..))")
public Object loggerAround(ProceedingJoinPoint joinPoint) throws Throwable {
    String method = joinPoint.getSignature().getName();
    String args = Arrays.toString(joinPoint.getArgs());

    try {
        logger.info("ANTES: El método: " + method + "() con los parámetros: " + args);

        Object result = joinPoint.proceed();  // Ejecuta el método interceptado

        logger.info("DESPUÉS: El método: " + method + "() retorna: " + result);

        return result;
    } catch (Throwable e) {
        logger.info("ERROR: Error en la llamada del método: " + method + "(), error: " + e.getMessage());
        throw e;
    }
}
```

### Detalles

- `@Around(...)`: marca el método como interceptador total.
- `ProceedingJoinPoint`: permite **ejecutar manualmente** el método objetivo.
- `proceed()`: ejecuta el método interceptado.
- Puedes modificar los argumentos o el resultado si es necesario.

---

## Servicio: `GreetingServiceImp`

```java
@Override
public String sayHelloAround(String person, String phrase) {
    String saludo = "Hello " + person + ", " + phrase;
    System.out.println(saludo);
    return saludo;
}
```

---

## Controlador: `GreetingController`

```java
@GetMapping("/greeting-around")
public ResponseEntity<?> greetingAround() {
    return ResponseEntity.ok(
        Map.of("greeting", greetingService.sayHelloAround("Felipe", "Hola cómo vas"))
    );
}
```

---

## Resultado en consola

```bash
INFO  GreetingAspect : ANTES: El método: sayHelloAround() con los parámetros: [Felipe, Hola cómo vas]
INFO  GreetingAspect : Antes: sayHelloAround, args: [Felipe, Hola cómo vas]
Hello Felipe, Hola cómo vas
INFO  GreetingAspect : Después: sayHelloAround, args: [Felipe, Hola cómo vas]
INFO  GreetingAspect : DESPUÉS: El método: sayHelloAround() retorna: Hello Felipe, Hola cómo vas
```

---

## Conclusión

El uso de `@Around` te da control total sobre el método interceptado:

- Puedes modificar argumentos o bloquear su ejecución.
- Puedes medir tiempos, validar condiciones o capturar errores.
- Es la herramienta más flexible en AOP, ideal para tareas como auditoría, seguridad, y monitoreo avanzado.
