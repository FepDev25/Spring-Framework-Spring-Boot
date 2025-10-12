# 2. Manejo de Errores Personalizados

Spring Boot permite definir **excepciones personalizadas** para representar errores específicos del dominio de tu aplicación. Estas excepciones se pueden capturar con `@ExceptionHandler` para devolver una respuesta clara al cliente.

---

## 🧨 Crear una Excepción Personalizada

```java
public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException(Long id) {
        super("No se puede encontrar el usuario con el id: " + id);
    }
}
```

> 📌 Se extiende de `RuntimeException` para evitar el manejo obligatorio con `try/catch`.

---

## 🛡️ Manejar múltiples excepciones con un solo `@ExceptionHandler`

```java
@ExceptionHandler({NullPointerException.class, HttpMessageNotWritableException.class, UserNotFoundException.class})
@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public Map<String, Object> userNotFound(Exception ex) {
    return Map.of(
        "message", ex.getMessage(),
        "error", "El usuario o role no existe",
        "status", HttpStatus.INTERNAL_SERVER_ERROR.value(),
        "date", new Date()
    );
}
```

- Puedes manejar múltiples excepciones en un solo método.
- Se utiliza `Map.of(...)` para devolver una respuesta con clave-valor.
- `@ResponseStatus` fija el código HTTP en la respuesta.

---

## 🚦 Lanza la excepción desde un controlador

### Opción 1: Validar manualmente

```java
@GetMapping("/show/{id}")
public User show(@PathVariable Long id) {
    User user = userService.findById(id);
    if (user == null) {
        throw new UserNotFoundException(id);
    }
    return user;
}
```

### Opción 2: Usar `Optional` y `orElseThrow`

```java
@GetMapping("/show/{id}")
public User show(@PathVariable Long id) {
    return userService.findById(id)
                      .orElseThrow(() -> new UserNotFoundException(id));
}
```

> ✅ Usar `Optional` hace el código más conciso y expresivo.

---

## ✅ Conclusión

Las excepciones personalizadas:
- Mejoran la claridad y el control del flujo de errores.
- Permiten separar la lógica de validación del controlador.
- Son altamente reutilizables y ayudan a construir APIs REST más limpias y robustas.
