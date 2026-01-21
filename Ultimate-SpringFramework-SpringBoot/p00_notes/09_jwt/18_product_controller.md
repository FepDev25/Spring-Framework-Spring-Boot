# 18. Controlador ProductController: Gestión de Productos con Seguridad JWT

## Objetivo

La clase `ProductController` define los endpoints RESTful para administrar productos. Está protegida mediante roles utilizando anotaciones de seguridad (`@PreAuthorize`) y permite operaciones como:

- Listar productos
- Ver detalle
- Crear
- Editar
- Eliminar

---

## CORS: Comunicación con el Frontend

```java
@CrossOrigin(origins = "http://localhost:4200", originPatterns = "*")
```

Esta anotación permite que aplicaciones frontend (por ejemplo, Angular) alojadas en `http://localhost:4200` puedan comunicarse con este controlador, habilitando el **intercambio de recursos entre orígenes**.

Aunque también hay configuración CORS global en `SpringSecurityConfig`, esta anotación:

- Se puede usar para sobrescribir o **refinar reglas por controlador**.
- En desarrollo, se usa con `originPatterns = "*"` para evitar bloqueos.

---

## Seguridad con `@PreAuthorize`

Cada método tiene una anotación `@PreAuthorize` que especifica qué roles pueden acceder a él.

Esta funcionalidad **solo es posible** gracias a:

```java
@EnableMethodSecurity(prePostEnabled=true)
```

en la clase `SpringSecurityConfig`.

---

## Métodos del controlador

### `GET /api/productos`

```java
@PreAuthorize("hasAnyRole('ADMIN', 'USER')")
public List<Product> list()
```

- Devuelve todos los productos.
- Puede ser accedido por usuarios con rol `ADMIN` o `USER`.

---

### `GET /api/productos/{id}`

```java
@PreAuthorize("hasAnyRole('ADMIN', 'USER')")
public ResponseEntity<?> view(@PathVariable Long id)
```

- Devuelve los detalles de un producto específico.
- Si no se encuentra, retorna 404.

---

### `POST /api/productos`

```java
@PreAuthorize("hasRole('ADMIN')")
public ResponseEntity<?> create(@Valid @RequestBody ProductDTO dto, BindingResult result)
```

- Solo accesible por usuarios `ADMIN`.
- Crea un nuevo producto a partir de un `ProductDTO`.
- Si hay errores de validación, se responde con `400 Bad Request` y detalles de los errores.

---

### `PUT /api/productos/{id}`

```java
@PreAuthorize("hasRole('ADMIN')")
public ResponseEntity<?> update(@Valid @RequestBody ProductDTO dto, BindingResult result, @PathVariable Long id)
```

- Actualiza un producto existente.
- Devuelve el producto actualizado o un `404 Not Found` si el `id` no existe.

---

### `DELETE /api/productos/{id}`

```java
@PreAuthorize("hasRole('ADMIN')")
public ResponseEntity<?> delete(@PathVariable Long id)
```

- Elimina un producto por `id`.
- Solo accesible por `ADMIN`.
- Devuelve `200 OK` si fue exitoso, o `404 Not Found` si no existe.

---

## Validación de entradas

```java
private ResponseEntity<?> validation(BindingResult result)
```

Este método procesa los errores de validación de los `DTOs`. Si un campo no cumple con las reglas (por ejemplo, `@NotBlank`, `@Min`, etc.), construye un mapa con los errores y lo retorna como JSON.

---

## Relación con `filterChain(...)`

En la clase `SpringSecurityConfig`, si se usara la versión comentada:

```java
// .requestMatchers(HttpMethod.POST, "/api/productos").hasRole("ADMIN")
// .requestMatchers(HttpMethod.GET, "/api/productos").hasAnyRole("ADMIN", "USER")
```

Esto significaría que los permisos se gestionan directamente en la configuración global, lo cual puede ser útil si:

- No se desea anotar cada método con `@PreAuthorize`.
- Se quiere aplicar seguridad a nivel de ruta sin modificar los controladores.

**Sin embargo**, usar `@PreAuthorize` es más flexible y expresivo, permitiendo que cada método tenga control explícito de acceso, y se vea directamente en el código.

---

## Conclusión

El controlador `ProductController` es una implementación REST protegida con JWT y roles. Gracias a `@PreAuthorize`, se garantiza que solo los usuarios con permisos correctos pueden ejecutar ciertas acciones. El uso combinado de validación, respuesta estructurada y CORS lo hace ideal para integraciones frontend seguras.
