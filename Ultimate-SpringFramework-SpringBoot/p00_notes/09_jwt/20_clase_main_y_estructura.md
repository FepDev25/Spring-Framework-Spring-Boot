# 20. Clase principal y estructura del proyecto `p11_spring_security_jwt`

## Objetivo del proyecto

Esta aplicación Spring Boot implementa un sistema completo de autenticación y autorización usando **JWT (JSON Web Tokens)**, diseñado para proteger una API RESTful con roles diferenciados (`USER`, `ADMIN`) y validaciones robustas. También está preparado para integrarse con un frontend, como una app Angular.

---

## Clase principal: `P11SpringSecurityJwtApplication`

```java
@SpringBootApplication
public class P11SpringSecurityJwtApplication {
    public static void main(String[] args) {
        SpringApplication.run(P11SpringSecurityJwtApplication.class, args);
    }
}
```

### Explicación

| Elemento                 | Descripción                                                                                                                                                       |
| ------------------------ | ----------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| `@SpringBootApplication` | Anotación compuesta que incluye `@Configuration`, `@EnableAutoConfiguration` y `@ComponentScan`. Marca esta clase como punto de entrada del proyecto Spring Boot. |
| `main(...)`              | Método principal que ejecuta la aplicación usando el contexto de Spring.                                                                                          |

Esta clase **inicia todo el sistema**: configura beans, carga los controladores REST, conecta la base de datos y activa los filtros de seguridad JWT.

---

## Estructura del proyecto (según imagen)

```plaintext
p11_spring_security_jwt/
│
├── config/         → Configuraciones generales (mensajes, contexto Spring, etc.)
├── controllers/    → Controladores REST: ProductController, UserController
├── dto/            → Objetos de transferencia de datos (ProductDTO, etc.)
├── entities/       → Entidades JPA: User, Role, Product
├── repositories/   → Interfaces de acceso a datos (UserRepository, ProductRepository...)
├── security/       → Configuración de seguridad JWT: filtros, tokens, SecurityConfig
│   └── filter/     → Filtros personalizados: JwtAuthenticationFilter, JwtValidationFilter
├── services/       → Interfaces y clases de negocio (UserService, ProductService)
├── validations/    → Anotaciones y validadores personalizados
│
├── P11SpringSecurityJwtApplication.java  → Clase principal
│
├── resources/
│   ├── application.properties     → Configuración general del proyecto
│   ├── messages.properties        → Mensajes de validación/internacionalización
│   ├── static/                    → (Opcional) Archivos públicos como HTML o imágenes
│   └── templates/                 → (Opcional) Vistas Thymeleaf si se usara renderizado en servidor
```

---

## Organización basada en capas

La arquitectura sigue el patrón **MVC (Model-View-Controller)** con separación adicional por responsabilidades:

| Carpeta         | Rol                                                                             |
| --------------- | ------------------------------------------------------------------------------- |
| `entities/`     | Define los modelos de datos mapeados a la base de datos.                        |
| `repositories/` | Proveen acceso a la base de datos con JPA.                                      |
| `dto/`          | Abstracción entre el modelo y la vista (validaciones y transferencia de datos). |
| `controllers/`  | Exponen los servicios de backend a través de rutas REST.                        |
| `services/`     | Contienen la lógica de negocio.                                                 |
| `security/`     | Manejo completo de autenticación, JWT, y configuración de seguridad.            |
| `validations/`  | Validadores personalizados para asegurar integridad de datos.                   |

---

## Archivos de configuración

* **`application.properties`**: define propiedades como puerto del servidor, configuración de la base de datos, y formato de logs.
* **`messages.properties`**: almacena los mensajes de error utilizados por los validadores, soportando internacionalización.

---

## Conclusión

La clase principal junto con una estructura de carpetas bien definida permite escalar el sistema, mantenerlo fácilmente y reforzar buenas prácticas de desarrollo backend. Al seguir este diseño modular y segmentado, se favorece la separación de responsabilidades y la mantenibilidad a largo plazo.
