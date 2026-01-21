# 1. Manejo de Excepciones con `@RestControllerAdvice` y `@ExceptionHandler`

Spring Boot permite manejar errores globalmente usando `@RestControllerAdvice` combinado con métodos anotados con `@ExceptionHandler`. Esto permite capturar y personalizar las respuestas de error cuando ocurren excepciones en los controladores.

---

## `@RestControllerAdvice`

- Es una especialización de `@ControllerAdvice` y `@ResponseBody`.
- Permite aplicar manejo de errores **a todos los controladores REST** de forma centralizada.
- Se usa para definir una clase que manejará excepciones lanzadas en cualquier controlador.

---

## `@ExceptionHandler`

- Anota un método que manejará una **excepción específica**.
- Permite devolver una respuesta personalizada cuando ocurre un error.

---

## Ejemplo práctico

```java
@RestControllerAdvice
public class HandlerException {

    @ExceptionHandler(ArithmeticException.class)
    public ResponseEntity<Error> divisionByZero(Exception ex) {
        return ResponseEntity.internalServerError().body(
            new Error(
                ex.getMessage(),
                "Error de división por cero",
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                new Date()
            )
        );
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<Error> notFoundEx(Exception ex) {
        Error error = new Error();
        error.setDate(new Date());
        error.setError("Api REST no encontrada");
        error.setMessage(ex.getMessage());
        error.setStatus(HttpStatus.NOT_FOUND.value());
        
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }
}
```

> Nota importante:
> Para que `NoHandlerFoundException` funcione, debe estar configurado en `application.properties`:

```bash
spring.web.resources.add-mappings=false
```

---

## Clase `Error`

Debe ser una clase POJO simple con atributos como:

- `String message`
- `String error`
- `int status`
- `Date date`

---

## Conclusión

El uso de `@RestControllerAdvice` y `@ExceptionHandler` permite centralizar y personalizar el manejo de errores en APIs REST. Esta técnica mejora la claridad de las respuestas y facilita el mantenimiento del código en aplicaciones Spring Boot.
