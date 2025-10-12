# Despliegue – Parte 2: Generar y ejecutar el archivo `.jar`

Una vez que el desarrollo y pruebas de la aplicación han concluido, el siguiente paso es **empaquetarla** como un archivo ejecutable `.jar`. Este archivo incluye:

- El código compilado del proyecto
- Las dependencias necesarias
- Un servidor Tomcat embebido
- La clase principal con el método `main(...)`

---

## ⚙️ 1. Comando para construir el `.jar`

Desde la raíz del proyecto ejecutamos:

```bash
./mvnw clean package
```

### 🔍 ¿Qué hace este comando?

| Fase      | Acción                                                       |
| --------- | ------------------------------------------------------------ |
| `clean`   | Elimina la carpeta `target/` para asegurar un build limpio.  |
| `package` | Compila el código, corre las pruebas y crea un `.jar` final. |

---

## 📦 2. Salida del proceso

Al finalizar correctamente, se genera un archivo como:

```bash
target/p11-spring-security-jwt-0.0.1-SNAPSHOT.jar
```

Este archivo `.jar` contiene:

* Todo tu código fuente compilado.
* Todas las dependencias del proyecto.
* Archivos `application.properties`, `messages.properties`, etc.
* Estructura especial `BOOT-INF/` para permitir ejecución directa.

---

## 🚀 3. Ejecutar el `.jar`

Una vez generado, puedes iniciar tu aplicación con:

```bash
java -jar ./target/p11-spring-security-jwt-0.0.1-SNAPSHOT.jar
```

---

## 🧠 ¿Qué ocurre internamente al ejecutarlo?

* Spring Boot inicia con el banner:

  ```plaintext
   :: Spring Boot :: (v3.5.0)
  ```
* Se levanta el servidor **Tomcat embebido** en el puerto 8080 (por defecto).
* Se inicia la conexión con la base de datos (en tu caso, MySQL 8.0.41).
* Se configuran los filtros de seguridad JWT y el contexto de autenticación.
* La API queda lista para recibir peticiones REST desde el frontend.

---

## ✅ Logs relevantes del inicio exitoso

| Log                                       | Significado                                                           |
| ----------------------------------------- | --------------------------------------------------------------------- |
| `Tomcat initialized with port 8080`       | Se ha levantado el servidor embebido en el puerto HTTP 8080.          |
| `HikariPool - Start completed`            | Se ha establecido correctamente la conexión al pool de base de datos. |
| `Started P11SpringSecurityJwtApplication` | La aplicación se encuentra ejecutándose exitosamente.                 |

---

## 📎 Observaciones útiles

* El `.jar` generado es **autocontenible**, lo que significa que:

  * No necesitas instalar Tomcat ni otro servidor.
  * Puedes mover este archivo a cualquier sistema con Java instalado y ejecutarlo allí.
* El empaquetado por defecto es `.jar` porque en `pom.xml` no se definió como `.war`.

---

## 🔒 Seguridad y configuración

Recuerda que al ejecutarlo así:

* El perfil activo es `"default"` si no se especifica uno.
* Usará los parámetros definidos en `src/main/resources/application.properties`.

---

## 📌 Conclusión

Generar y ejecutar un `.jar` con Spring Boot es un proceso directo gracias a Maven. El archivo resultante puede ser usado para:

* Despliegues manuales en servidores Linux
* Contenerización con Docker
* Hosting en plataformas cloud como Heroku, EC2, Render, etc.
