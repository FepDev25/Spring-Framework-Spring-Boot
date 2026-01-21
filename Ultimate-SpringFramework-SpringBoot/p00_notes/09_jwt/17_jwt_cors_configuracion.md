# 17. Configuración de CORS en Spring Security

## ¿Qué es CORS?

**CORS (Cross-Origin Resource Sharing)** es un mecanismo de seguridad implementado en los navegadores modernos que **restringe** las peticiones HTTP entre diferentes dominios u orígenes.

Por ejemplo:

- Tu frontend (Angular) corre en `http://localhost:4200`
- Tu backend (Spring Boot) corre en `http://localhost:8080`

Esto se considera **una petición entre orígenes distintos** y, por defecto, los navegadores la bloquean si el servidor no indica explícitamente que la permite.

---

## Solución: Configurar CORS en el backend

En esta clase `SpringSecurityConfig`, se configuran dos mecanismos clave:

---

## 1. `corsConfigurationSource()`: Define las reglas de CORS

```java
@Bean
CorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration config = new CorsConfiguration();
    config.setAllowedOriginPatterns(Arrays.asList("*"));
    config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE"));
    config.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type"));
    config.setAllowCredentials(true);

    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", config);
    
    return source;
}
```

### Explicación línea por línea

| Línea                                          | Descripción                                                                                                        |
| ---------------------------------------------- | ------------------------------------------------------------------------------------------------------------------ |
| `setAllowedOriginPatterns(Arrays.asList("*"))` | Permite peticiones desde **cualquier origen** (útil en desarrollo). En producción es mejor especificar el dominio. |
| `setAllowedMethods(...)`                       | Solo acepta los métodos HTTP indicados.                                                                            |
| `setAllowedHeaders(...)`                       | Permite solo ciertos encabezados, como `Authorization` (para el JWT) y `Content-Type`.                             |
| `setAllowCredentials(true)`                    | Permite el uso de **cookies y encabezados de autorización** en peticiones entre orígenes.                          |
| `registerCorsConfiguration("/**", config)`     | Aplica esta configuración a **todas las rutas** del backend.                                                       |

---

## 2. Aplicar CORS en la cadena de filtros

```java
.cors(cors -> cors.configurationSource(corsConfigurationSource()))
```

- Esta línea se encuentra dentro del método `filterChain(...)`.
- Le dice a Spring Security que use la configuración CORS personalizada definida anteriormente.
- Si esta línea se omite, **Spring Security bloqueará las peticiones entre orígenes**, incluso si `@CrossOrigin` está presente.

---

## 3. Filtro de CORS con prioridad alta

```java
@Bean
FilterRegistrationBean<CorsFilter> corsFilter() {
    FilterRegistrationBean<CorsFilter> bean = new FilterRegistrationBean<>(new CorsFilter(corsConfigurationSource()));
    bean.setOrder(Ordered.HIGHEST_PRECEDENCE);
    return bean;
}
```

- Registra el filtro de CORS directamente en el contexto de Spring Boot.
- Le da la **más alta prioridad** (`Ordered.HIGHEST_PRECEDENCE`) para que las reglas CORS se apliquen **antes que cualquier otro filtro**, incluso los de seguridad JWT.

> **¿Por qué es útil este filtro extra?**
> Porque en algunas configuraciones (especialmente en Spring Boot embebido), el `CorsFilter` registrado como Bean puede asegurar que las respuestas `OPTIONS` preflight no sean bloqueadas.

---

## Ejemplo práctico: Cómo se usa

Supón que tu frontend Angular está corriendo en `http://localhost:4200` y quieres hacer una petición `GET /api/users`.

Si no hay configuración CORS, el navegador **bloqueará** esta petición, incluso si el backend responde correctamente.

Con esta configuración:

- El backend indica que **acepta solicitudes de cualquier origen**.
- Permite el encabezado `Authorization`, necesario para enviar el JWT.
- Soporta los métodos básicos (`GET`, `POST`, etc.).
- No necesitas agregar `@CrossOrigin` en cada controlador (pero puedes si deseas más control granular).

---

## ¡Importante! Producción vs Desarrollo

En producción:

- No deberías usar `"*"` como origen.
- Usa dominios específicos como: `https://miapp.com`

Ejemplo:

```java
config.setAllowedOriginPatterns(Arrays.asList("https://miapp.com"));
```

---

## Conclusión

La configuración de CORS es fundamental para que el frontend pueda comunicarse correctamente con la API segura. Esta clase:

- Permite todas las solicitudes en desarrollo.
- Configura correctamente encabezados y métodos.
- Asegura que el filtro de CORS se ejecute antes que cualquier otro.

Esto soluciona los errores típicos como:

```bash
Access to fetch at 'http://localhost:8080/api/users' from origin 'http://localhost:4200' has been blocked by CORS policy
```
