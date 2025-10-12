# 5. Contextos / Scopes en Spring

En Spring, el *scope* define el **ciclo de vida y visibilidad de los beans** dentro del contenedor. Por defecto, todos los beans son `singleton`, pero existen otros scopes útiles en aplicaciones web como `request` y `session`.

---

## 5.1 Principio de Inmutabilidad

El principio de inmutabilidad sugiere que un objeto, una vez creado, **no debe cambiar su estado interno**. En el contexto de Spring:

- Favorece el uso de beans inmutables para servicios que **no dependen del estado**.
- Reduce errores en aplicaciones concurrentes.
- Se implementa comúnmente con inyección por constructor.

```java
@Service
public class ProductoService {

    private final ProductRepository repository;

    public ProductoService(ProductRepository repository) {
        this.repository = repository;
    }

    // Sin setters ni cambios de estado: inmutable
}
```

---

## 5.2 `@Scope("singleton")` (por defecto)

- Es el scope **predeterminado** en Spring.
- Solo se crea **una instancia** del bean en todo el contenedor.
- Todos los componentes que lo usen, comparten la misma instancia.

```java
@Component
@Scope("singleton") // opcional, ya que es el valor por defecto
public class SingletonBean {
    // lógica compartida
}
```

> 🔁 Ideal para servicios sin estado o lógica de negocio compartida.

---

## 5.3 `@RequestScope`

- Crea **una nueva instancia del bean por cada petición HTTP**.
- Útil cuando el bean depende de información específica de la solicitud (como headers o parámetros).

```java
@Component
@RequestScope
public class RequestScopedBean {
    // Bean diferente para cada request
}
```

> 🧾 Requiere una aplicación web configurada correctamente (Spring Web MVC).

---

## 5.4 `@SessionScope`

- Crea **una instancia del bean por sesión de usuario**.
- Se mantiene viva mientras la sesión HTTP del usuario esté activa.

```java
@Component
@SessionScope
public class SessionScopedBean {
    // Datos que deben mantenerse por sesión (ej: carrito de compras)
}
```

> 🔐 Útil para almacenar datos que deben persistir durante la navegación del usuario.

---

## ✅ Conclusión

Spring ofrece diferentes contextos o scopes que se adaptan a las necesidades del ciclo de vida de un componente:
- `singleton`: predeterminado, compartido.
- `request`: nueva instancia por solicitud HTTP.
- `session`: instancia por sesión de usuario.
- Aplicar el principio de inmutabilidad ayuda a tener código más limpio, predecible y seguro en entornos concurrentes.
