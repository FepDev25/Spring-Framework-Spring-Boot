# Uso de `application.properties` en Spring Boot

El archivo `application.properties` es una parte esencial de una aplicación Spring Boot. Sirve para definir configuraciones clave del proyecto en un solo lugar, facilitando el mantenimiento, la lectura y la modificación del comportamiento de la aplicación sin necesidad de cambiar el código fuente.

## ¿Dónde se encuentra?

Generalmente se ubica en el directorio `src/main/resources` del proyecto:

```
src/
 └── main/
     └── resources/
         └── application.properties
```

## Principales usos

### 1. Configuración del servidor

```properties
server.port=8081
server.servlet.context-path=/api
```

- `server.port`: define el puerto en el que se ejecuta la aplicación.
- `server.servlet.context-path`: define la ruta base del contexto.

---

### 2. Configuración de base de datos

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/mi_basedatos
spring.datasource.username=usuario
spring.datasource.password=contrasena
spring.datasource.driver-class-name=org.postgresql.Driver
```

- Establece los parámetros de conexión a la base de datos.

---

### 3. Configuración de JPA / Hibernate

```properties
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
```

- `ddl-auto=update`: actualiza el esquema automáticamente al iniciar.
- `show-sql=true`: muestra las consultas SQL por consola.

---

### 4. Mensajes personalizados

```properties
spring.messages.basename=messages
```

- Define el archivo base para mensajes de internacionalización (`messages.properties`).

---

### 5. Configuración personalizada

Puedes definir tus propias propiedades para usarlas en el código:

```properties
app.nombre=MiAplicacion
```

Y acceder en una clase usando `@Value`:

```java
@Value("${app.nombre}")
private String nombreApp;
```

---

## Ventajas

- Centralización de configuración.
- Fácil adaptación a distintos entornos (`application-dev.properties`, `application-prod.properties`, etc.).
- Separación entre lógica y configuración.

---

## Archivos por entorno

Spring Boot permite tener múltiples archivos para distintos perfiles:

```properties
# application-dev.properties
server.port=8082

# application-prod.properties
server.port=80
```

Y activarlos con:

```properties
spring.profiles.active=dev
```

---

## Conclusión

El archivo `application.properties` permite una configuración flexible y desacoplada, facilitando el desarrollo, pruebas y despliegue de aplicaciones Spring Boot.