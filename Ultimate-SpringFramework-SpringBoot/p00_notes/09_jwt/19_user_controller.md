# 19. Controlador UserController: Registro y Gestión de Usuarios

## Objetivo

El `UserController` define los endpoints encargados de:

- Listar todos los usuarios registrados.
- Crear nuevos usuarios desde el backend (requiere rol ADMIN).
- Registrar usuarios desde el frontend (registro público, sin autenticación previa).

---

## CORS: Permitir acceso desde el frontend

```java
@CrossOrigin(origins = "http://localhost:4200", originPatterns = "*")
```

Permite que el frontend (ej. Angular en el puerto 4200) pueda comunicarse con los endpoints del controlador. Aunque existe configuración global de CORS, esta línea asegura que:

- Se pueda registrar o autenticar desde interfaces web.
- Se evite el error de política de mismo origen (CORS policy).

---

## Seguridad con @PreAuthorize

```java
@PreAuthorize("hasRole('ADMIN')")
```

Este decorador protege el endpoint `POST /api/users` permitiendo que solo usuarios autenticados con el rol `ADMIN` puedan crear nuevos usuarios desde el backend.

Esta seguridad se activa gracias a:

```java
@EnableMethodSecurity(prePostEnabled=true)
```

en la clase de configuración `SpringSecurityConfig`.

---

## Métodos del controlador

### `GET /api/users`

```java
public List<User> findAll()
```

- Lista todos los usuarios.
- **No requiere autenticación** (permiso abierto).
- Útil para el frontend si se desea mostrar usuarios públicamente.

---

### `POST /api/users`

```java
@PreAuthorize("hasRole('ADMIN')")
public ResponseEntity<?> save(@Valid @RequestBody User user, BindingResult result)
```

- Permite crear un nuevo usuario.
- **Solo accesible por usuarios con rol ADMIN**.
- Usa anotación `@Valid` para validar los campos del objeto `User`.
- En caso de errores de validación, se responde con `400 Bad Request`.

---

### `POST /api/users/register`

```java
public ResponseEntity<?> register(@Valid @RequestBody User user, BindingResult result)
```

- Endpoint **público**, sin restricciones de rol.
- Ideal para formularios de registro del usuario desde el frontend.
- Por seguridad, siempre se le asigna `admin = false`:

```java
user.setAdmin(false);
```

Esto asegura que un usuario normal no pueda autoregistrarse como administrador.

---

## Validación de datos

```java
private ResponseEntity<?> validation(BindingResult result)
```

Si al validar el objeto `User` se encuentran errores (por ejemplo, nombre vacío, email mal formado, etc.), este método recopila los errores y los devuelve en formato JSON como respuesta 400.

Ejemplo de respuesta:

```json
{
  "username": "El campo username no puede estar vacío",
  "email": "El campo email debe tener un formato válido"
}
```

---

## Conexión con el sistema de seguridad

Este controlador se relaciona directamente con la configuración de seguridad definida en `SpringSecurityConfig`, especialmente en el método `filterChain()`:

- El endpoint `/api/users/register` está explícitamente permitido:

```java
.requestMatchers(HttpMethod.POST, "/api/users/register").permitAll()
```

- Los demás endpoints requieren autenticación:

```java
.anyRequest().authenticated()
```

---

## Resumen

| Endpoint              | Método | Rol requerido | Público | Descripción                  |
| --------------------- | ------ | ------------- | ------- | ---------------------------  |
| `/api/users`          | GET    | Ninguno       | Si      | Listar todos los usuarios    |
| `/api/users`          | POST   | `ADMIN`       | No      | Crear usuario desde backend  |
| `/api/users/register` | POST   | Ninguno       | Si      | Registro público de usuario  |

---

## Conclusión

`UserController` combina flexibilidad y seguridad:

- Permite el registro libre de usuarios comunes.
- Restringe la creación manual de usuarios a administradores.
- Asegura integridad de datos mediante validaciones.
- Brinda una respuesta clara para el frontend.
