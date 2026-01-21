# 15. `JwtValidationFilter` – Filtro de Validación de Token JWT

El `JwtValidationFilter` extiende `BasicAuthenticationFilter` y se encarga de interceptar las peticiones HTTP que **contienen un token JWT**, validarlo, y si es válido, autenticar al usuario en el contexto de Spring Security.

Este filtro **no se encarga del login** ni de emitir tokens, sino de verificar que el token incluido en la cabecera de autorización siga siendo válido en las siguientes peticiones.

---

## Propósito

- Leer el token JWT enviado en el encabezado HTTP `Authorization`.
- Verificar que esté correctamente firmado y no haya expirado.
- Extraer el `username` y roles del token.
- Autenticar al usuario en el `SecurityContext` para que los endpoints protegidos puedan verificar su identidad y permisos.

---

## Constructor

```java
public JwtValidationFilter(AuthenticationManager authenticationManager) {
    super(authenticationManager);
}
```

- Recibe un `AuthenticationManager`, necesario para construir el filtro correctamente (aunque no se utiliza directamente aquí).

---

## Método principal: `doFilterInternal(...)`

```java
@Override
protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
        throws IOException, ServletException {
    
    String header = request.getHeader(HEADER_AUTHORIZATION);
    if (header == null || !header.startsWith(PREFIX_TOKEN)){
        chain.doFilter(request, response);
        return;
    }

    String token = header.replace(PREFIX_TOKEN, "");    

    try {
        Claims claims = Jwts.parser()
            .verifyWith(SECRET_KEY)
            .build()
            .parseSignedClaims(token)
            .getPayload();

        String username = claims.getSubject();
        List<String> roles = (List<String>) claims.get("authorities");

        Collection<? extends GrantedAuthority> authorities = roles.stream()
            .map(SimpleGrantedAuthority::new)
            .toList();

        UsernamePasswordAuthenticationToken auth =
            new UsernamePasswordAuthenticationToken(username, null, authorities);

        SecurityContextHolder.getContext().setAuthentication(auth);
        chain.doFilter(request, response);

    } catch (JwtException e) {
        Map<String, String> body = new HashMap<>();
        body.put("message", "Token inválido");
        body.put("error", e.getMessage());
        response.getWriter().write(new ObjectMapper().writeValueAsString(body));
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType(CONTENT_TYPE);
    }
}
```

---

## Explicación Paso a Paso

| Paso                                                        | Descripción                                                           |
| ----------------------------------------------------------- | --------------------------------------------------------------------- |
| `request.getHeader(...)`                                    | Se recupera el valor del encabezado `Authorization`.                  |
| Verificación del prefijo                                    | Solo se procesa si el encabezado comienza con `"Bearer "`.            |
| `parseSignedClaims(token)`                                  | Verifica la firma digital y extrae el contenido del token (`Claims`). |
| `getSubject()`                                              | Obtiene el `username` (quién es el usuario autenticado).              |
| `get("authorities")`                                        | Extrae la lista de roles del token.                                   |
| `SimpleGrantedAuthority`                                    | Se reconstruyen los roles como objetos que Spring Security entiende.  |
| `SecurityContextHolder.getContext().setAuthentication(...)` | Se establece el usuario autenticado en el contexto de seguridad.      |
| `chain.doFilter(...)`                                       | Continúa la cadena de filtros.                                        |

---

## Manejo de Errores

```java
catch (JwtException e) {
    Map<String, String> body = new HashMap<>();
    body.put("message", "Token inválido");
    body.put("error", e.getMessage());
    ...
    response.setStatus(HttpStatus.UNAUTHORIZED.value());
}
```

- Si el token está corrupto, caducado, o mal formado, se captura la excepción.
- Se responde con un **HTTP 401** y un mensaje JSON indicando el error.

---

## Flujo General de Validación

```plaintext
[Cliente] --------> [Requiere recurso protegido: GET /api/products]
  |
  |-- Header: Authorization: Bearer <jwt_token>
  ↓
[JwtValidationFilter]
  |
  |-- ¿Es válido el token?
         |-- No → Error 401
         |-- Sí → Spring Security autentica al usuario
  ↓
[Controller] (protegido por @PreAuthorize, roles, etc.)
```

---

## ¿Por qué es importante?

- Permite que una vez autenticado, el cliente use el token para acceder a múltiples recursos **sin volver a enviar usuario y contraseña**.
- Reemplaza completamente el uso de sesión (`HttpSession`) con una forma segura y escalable.
- Es un componente central en toda API segura basada en JWT.

---

## Buenas prácticas observadas

| Práctica                                             | Justificación                                          |
| ---------------------------------------------------- | ------------------------------------------------------ |
| ✔ Validación del token con firma                     | Asegura integridad y autenticidad.                     |
| ✔ Expulsión si el encabezado no existe o es inválido | Mejora rendimiento y evita errores innecesarios.       |
| ✔ Manejo detallado de excepciones                    | Mejora la experiencia del desarrollador frontend.      |
| ✔ Uso de `SecurityContextHolder`                     | Integra con el ecosistema completo de Spring Security. |

---

## Conclusión

`JwtValidationFilter` es el guardián de todos los recursos protegidos. Valida tokens en cada petición, autentica al usuario basado en la firma y contenido del JWT, y permite aplicar control de acceso usando anotaciones como `@PreAuthorize`. Junto al filtro de autenticación, forma la base del sistema de seguridad JWT.
