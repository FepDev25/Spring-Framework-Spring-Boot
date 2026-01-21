# 13. Clase `TokenJwtConfig` – Parámetros del Token JWT

En la arquitectura de seguridad basada en **JSON Web Tokens (JWT)**, es importante centralizar y reutilizar los parámetros que definen cómo se construyen y verifican los tokens. La clase `TokenJwtConfig` cumple este propósito, proporcionando constantes que serán utilizadas en el proceso de autenticación, generación y validación de tokens.

---

## Código de la clase

```java
public class TokenJwtConfig {
    public static final SecretKey SECRET_KEY = Jwts.SIG.HS256.key().build();
    public static final String PREFIX_TOKEN = "Bearer ";
    public static final String HEADER_AUTHORIZATION = "Authorization";
    public static final String CONTENT_TYPE = "application/json";
}
```

---

## Campos definidos

| Constante              | Descripción                                                                                     |
| ---------------------- | ----------------------------------------------------------------------------------------------- |
| `SECRET_KEY`           | Clave secreta generada con algoritmo HS256, usada para **firmar** y **verificar** el token JWT. |
| `PREFIX_TOKEN`         | Prefijo estándar para tokens enviados en el encabezado HTTP (`Bearer <token>`).                 |
| `HEADER_AUTHORIZATION` | Nombre del encabezado HTTP donde se envía el token (`Authorization`).                           |
| `CONTENT_TYPE`         | Tipo de contenido usado en las respuestas de autenticación (`application/json`).                |

---

## Detalles sobre `SECRET_KEY`

```java
public static final SecretKey SECRET_KEY = Jwts.SIG.HS256.key().build();
```

* Utiliza la API de **`jjwt`** para generar una clave secreta con el algoritmo **HMAC SHA-256**.
* Esta clave se usa para firmar el JWT al momento de emitirlo y para verificar su integridad al momento de consumirlo.
* **Importante**: En entornos de producción, se recomienda externalizar esta clave en una variable de entorno o en un sistema de configuración segura (por ejemplo, `application.properties` o Vault).

---

## Relación con otras clases

Esta clase es utilizada en:

* **Generador del token JWT**: para firmar el token con la clave.
* **Filtro de autenticación JWT**: para verificar si el token recibido es válido.
* **Controlador de autenticación**: para establecer el `Content-Type` y construir la respuesta con el token.
* **Middleware de autorización**: para leer el `Authorization` header y extraer el token.

---

## Conclusión

`TokenJwtConfig` centraliza los valores más importantes del proceso JWT, promoviendo la reutilización, la coherencia del sistema y la facilidad de mantenimiento. Es una práctica recomendada agrupar estos parámetros en una clase estática dedicada, especialmente en proyectos con múltiples filtros y proveedores de seguridad.
