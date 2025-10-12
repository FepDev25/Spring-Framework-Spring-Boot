# 16. `SpringSecurityConfig` (Parte 1) – Filtro Principal y Beans de Seguridad

La clase `SpringSecurityConfig` es el **núcleo de configuración de seguridad** en Spring Security. Aquí se define cómo se autentican y autorizan los usuarios, qué filtros intervienen en el ciclo JWT y cómo se comporta el sistema ante las peticiones HTTP protegidas.

---

## 🧩 Anotaciones clave

```java
@Configuration
@EnableMethodSecurity(prePostEnabled = true)
```

| Anotación               | Función                                                                           |
| ----------------------- | --------------------------------------------------------------------------------- |
| `@Configuration`        | Declara esta clase como una fuente de beans para Spring.                          |
| `@EnableMethodSecurity` | Habilita anotaciones como `@PreAuthorize("hasRole('ADMIN')")` a nivel de métodos. |

---

## 🔐 Bean: `authenticationManager()`

```java
@Bean
AuthenticationManager authenticationManager() throws Exception {
    return authenticationConfiguration.getAuthenticationManager();
}
```

* Obtiene una instancia de `AuthenticationManager` a partir de la configuración interna de Spring Boot.
* Es necesario para que los filtros JWT puedan autenticar usuarios (`attemptAuthentication`) o validar tokens (`setAuthentication` en el contexto).

---

## 🔐 Bean: `passwordEncoder()`

```java
@Bean
PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
}
```

* Utiliza el algoritmo **BCrypt**, una función de hash fuerte, para codificar contraseñas.
* Se usa cuando un usuario se registra o se validan las credenciales en login.

> 🔐 BCrypt incluye salt y es resistente a ataques por diccionario o fuerza bruta.

---

## 🧰 Bean: `corsConfigurationSource()`

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

* Permite peticiones **CORS** desde cualquier origen (`*`).
* Solo permite ciertos métodos y encabezados.
* `AllowCredentials(true)` permite enviar cookies o tokens entre dominios.

---

## 🌐 Bean: `corsFilter()`

```java
@Bean
FilterRegistrationBean<CorsFilter> corsFilter() {
    FilterRegistrationBean<CorsFilter> bean = new FilterRegistrationBean<>(new CorsFilter(corsConfigurationSource()));
    bean.setOrder(Ordered.HIGHEST_PRECEDENCE);
    return bean;
}
```

* Registra el filtro de CORS con **la más alta prioridad**.
* Asegura que todas las peticiones sean verificadas para CORS antes que cualquier otro filtro de seguridad.

---

## 🔗 `filterChain(...)` – Cadena principal de seguridad

```java
@Bean
SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    return http
        .authorizeHttpRequests((authz) -> authz
            .requestMatchers(HttpMethod.GET, "/api/users").permitAll()
            .requestMatchers(HttpMethod.POST, "/api/users/register").permitAll()
            // .requestMatchers(HttpMethod.POST, "/api/users").hasRole("ADMIN")
            // .requestMatchers(HttpMethod.POST, "/api/productos").hasRole("ADMIN")
            // .requestMatchers(HttpMethod.PUT, "/api/productos/{id}").hasRole("ADMIN")
            // .requestMatchers(HttpMethod.DELETE, "/api/productos/{id}").hasRole("ADMIN")
            // .requestMatchers(HttpMethod.GET, "/api/productos", "/api/productos/{id}").hasAnyRole("ADMIN", "USER")
            .anyRequest().authenticated()
        )
        .addFilter(new JwtAuthenticationFilter(authenticationManager()))
        .addFilter(new JwtValidationFilter(authenticationManager()))
        .csrf(csrf -> csrf.disable())
        .cors(cors -> cors.configurationSource(corsConfigurationSource()))
        .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .build();
}
```

---

## 🔍 Explicación paso a paso

| Configuración                   | Función                                                                                |
| ------------------------------- | -------------------------------------------------------------------------------------- |
| `.authorizeHttpRequests(...)`   | Define las reglas de acceso para cada ruta HTTP.                                       |
| `.addFilter(...)`               | Agrega los **filtros personalizados de autenticación y validación JWT**.               |
| `.csrf(csrf -> csrf.disable())` | Desactiva protección CSRF porque no usamos sesiones (solo tokens).                     |
| `.cors(...)`                    | Activa la configuración CORS personalizada.                                            |
| `.sessionManagement(...)`       | Establece el sistema como **sin estado (stateless)**. No se crean sesiones de usuario. |

---

## 🔐 Rutas públicas y protegidas

```java
.requestMatchers(HttpMethod.GET, "/api/users").permitAll()
.requestMatchers(HttpMethod.POST, "/api/users/register").permitAll()
```

* Estas rutas son **públicas**, es decir, no requieren token JWT.

```java
// .requestMatchers(HttpMethod.POST, "/api/users").hasRole("ADMIN")
// .requestMatchers(HttpMethod.POST, "/api/productos").hasRole("ADMIN")
// .requestMatchers(HttpMethod.PUT, "/api/productos/{id}").hasRole("ADMIN")
// .requestMatchers(HttpMethod.DELETE, "/api/productos/{id}").hasRole("ADMIN")
// .requestMatchers(HttpMethod.GET, "/api/productos", "/api/productos/{id}").hasAnyRole("ADMIN", "USER")
```

* Estas líneas comentadas muestran ejemplos para:

  * Restringir la creación, actualización y eliminación de productos a usuarios con rol `ADMIN`.
  * Permitir que tanto `ADMIN` como `USER` puedan consultar productos.

> ✨ Puedes descomentar estas líneas según los requerimientos de control de acceso específicos de tu aplicación.

---

## 📌 Consideraciones

* **Filtro de autenticación (`JwtAuthenticationFilter`)**: solo actúa sobre `/login`.
* **Filtro de validación (`JwtValidationFilter`)**: actúa sobre todas las demás peticiones con token.
* **Todos los demás endpoints** (`.anyRequest().authenticated()`) requieren token JWT válido.

---

## ✅ Conclusión

El método `filterChain` es el eje central de la configuración de seguridad. Aquí se define el comportamiento del sistema frente a peticiones HTTP, se agregan los filtros JWT, se configura CORS y se desactiva la sesión. Esta arquitectura es **100% sin estado** y altamente escalable, ideal para APIs RESTful protegidas con tokens JWT.
