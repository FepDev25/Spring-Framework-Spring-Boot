# 14. `JwtAuthenticationFilter` – Filtro de Autenticación con JWT

La clase `JwtAuthenticationFilter` extiende `UsernamePasswordAuthenticationFilter` de Spring Security y se encarga de **interceptar las peticiones de login**, validar las credenciales del usuario y, si son correctas, generar un **JSON Web Token (JWT)** que será devuelto al cliente.

---

## Propósito

Este filtro tiene 3 responsabilidades principales:

1. Leer el `username` y `password` del cuerpo de la petición.
2. Validar las credenciales usando el `AuthenticationManager`.
3. Si son válidas, generar y devolver un token JWT firmado con los roles del usuario.

---

## Constructor

```java
public JwtAuthenticationFilter(AuthenticationManager authenticationManager) {
    this.authenticationManager = authenticationManager;
}
```

* Recibe e inyecta el `AuthenticationManager`, que será responsable de verificar las credenciales del usuario con los datos almacenados en la base de datos.

---

## `attemptAuthentication(...)`

```java
@Override
public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
        throws AuthenticationException {
    
    User user;
    String username = null;
    String password = null;

    try {
        user = new ObjectMapper().readValue(request.getInputStream(), User.class);
        username = user.getUsername();
        password = user.getPassword();
    } catch (...) {
        // Manejo simple de errores de lectura
    }

    UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(username, password);
    return authenticationManager.authenticate(token);
}
```

### Qué hace

* Lee el cuerpo de la petición HTTP (JSON) y lo transforma en un objeto `User`.
* Extrae `username` y `password`.
* Crea un token de autenticación (`UsernamePasswordAuthenticationToken`).
* Lo envía al `AuthenticationManager` que delega la validación a `UserDetailsService`.

> Si las credenciales son incorrectas, se lanza una excepción y se ejecuta el método `unsuccessfulAuthentication`.

---

## `successfulAuthentication(...)`

```java
@Override
protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
        Authentication authResult) throws IOException, ServletException {

    UserDetails user = (UserDetails) authResult.getPrincipal();
    String username = user.getUsername();
    Collection<? extends GrantedAuthority> roles =  authResult.getAuthorities();

    Claims claims = Jwts.claims()
        .add("authorities", roles.stream()
            .map(GrantedAuthority::getAuthority)
            .collect(Collectors.toList()))
        .build();

    String token = Jwts.builder()
        .subject(username)
        .claims(claims)
        .issuedAt(new Date())
        .expiration(new Date(System.currentTimeMillis() + 3600000)) // 1 hora
        .signWith(SECRET_KEY)
        .compact();

    response.addHeader(HEADER_AUTHORIZATION, PREFIX_TOKEN + token); 

    Map<String, String> body = new HashMap<>();
    body.put("token", token);
    body.put("username", username);
    body.put("message", String.format("Hola %s, te has logueado con éxito", username));

    response.getWriter().write(new ObjectMapper().writeValueAsString(body));
    response.setStatus(200);
    response.setContentType(CONTENT_TYPE);
}
```

### Detalles clave

| Parte                             | Descripción                                                       |
| --------------------------------- | ----------------------------------------------------------------- |
| `authResult.getPrincipal()`       | Obtiene el `UserDetails` autenticado.                             |
| `authResult.getAuthorities()`     | Obtiene los roles del usuario.                                    |
| `Jwts.claims().add(...)`          | Agrega los roles al token como `claims`.                          |
| `Jwts.builder()`                  | Construye el JWT firmado con `HS256` y la clave secreta.          |
| `response.addHeader(...)`         | Añade el token al encabezado HTTP (`Authorization: Bearer ...`).  |
| `response.getWriter().write(...)` | También devuelve el token y mensaje en formato JSON en el cuerpo. |

---

## `unsuccessfulAuthentication(...)`

```java
@Override
protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
        AuthenticationException failed) throws IOException, ServletException {

    Map<String, String> body = new HashMap<>();
    body.put("message", "El usuario o la contraseña son incorrectos");
    body.put("error", failed.getMessage());

    response.getWriter().write(new ObjectMapper().writeValueAsString(body));
    response.setStatus(401);
    response.setContentType(CONTENT_TYPE);
}
```

* Se ejecuta automáticamente si el login falla.
* Devuelve un código `401 Unauthorized` y un mensaje descriptivo.

---

## Flujo de Autenticación JWT

```plaintext
[Cliente Frontend]
     |
     | (POST /login con JSON: { username, password })
     ↓
[JwtAuthenticationFilter]
     |
     | -- validate with AuthenticationManager
     |
     | -- [OK] --> generate JWT
     ↓
[Response JSON]
{
  "token": "...",
  "username": "juan",
  "message": "Hola juan, te has logueado con éxito"
}
```

---

## Buenas prácticas implementadas

| Práctica                               | Descripción                                                    |
| -------------------------------------- | -------------------------------------------------------------- |
| ✔ JWT con roles como claims            | Permite autorización basada en roles más adelante.             |
| ✔ Expiración del token                 | El token tiene un tiempo de vida definido (1 hora).            |
| ✔ Separación de errores                | Mensajes claros si falla el login.                             |
| ✔ Integración con `UserDetailsService` | El filtro trabaja con el servicio estándar de Spring Security. |

---

## Conclusión

`JwtAuthenticationFilter` es uno de los componentes más importantes del sistema de seguridad JWT. Se encarga de interceptar peticiones de login, validar las credenciales del usuario, y generar un token seguro firmado digitalmente. Esta clase marca el inicio del ciclo JWT que protegerá el resto de la aplicación.

Este token será luego verificado por el **filtro de autorización**, que analizaremos a continuación.
