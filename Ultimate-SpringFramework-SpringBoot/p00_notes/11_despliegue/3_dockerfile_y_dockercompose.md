# Parte 3: Crear contenedor para app y base de datos

Una vez que tenemos el archivo `.jar` listo, podemos contenerizar nuestra aplicación para facilitar su despliegue y aislamiento. Este proceso utiliza:

- Un `Dockerfile` para construir la imagen de la app.
- Un archivo `docker-compose.yml` para levantar la app y la base de datos en conjunto.

---

## 1. Dockerfile: Crear imagen de la aplicación

Este archivo indica cómo construir la imagen que ejecutará tu `.jar`:

```Dockerfile
FROM eclipse-temurin:17-jdk-alpine

WORKDIR /app

COPY target/p11-spring-security-jwt-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
```

**Explicación**:

| Instrucción    | Propósito                                                    |
| -------------- | ------------------------------------------------------------ |
| `FROM`         | Usa una imagen base de Java 17 optimizada.                   |
| `WORKDIR /app` | Define el directorio de trabajo dentro del contenedor.       |
| `COPY`         | Copia el `.jar` generado por Maven a la imagen.              |
| `EXPOSE`       | Expone el puerto 8080 donde se ejecutará la app.             |
| `ENTRYPOINT`   | Comando por defecto que se ejecuta al iniciar el contenedor. |

---

## 2. docker-compose.yml: Levantar app y base de datos

Este archivo define ambos servicios:

```yaml
version: '3.8'

services:
  app:
    build: .
    ports:
      - "8080:8080"
    depends_on:
      - db
    environment:
      SPRING_DATASOURCE_URL: jdbc:mysql://db:3306/db_jwt
      SPRING_DATASOURCE_USERNAME: felipep
      SPRING_DATASOURCE_PASSWORD: xxx
    networks:
      - red-interna

  db:
    image: mysql:8
    environment:
      MYSQL_DATABASE: db_jwt
      MYSQL_USER: felipep
      MYSQL_PASSWORD: xxx
      MYSQL_ROOT_PASSWORD: xxxx
    ports:
      - "3308:3306"
    volumes:
      - mysql-data:/var/lib/mysql
    networks:
      - red-interna

volumes:
  mysql-data:

networks:
  red-interna:
```

**Explicación por bloques**:

| Elemento             | Descripción                                                         |
| -------------------- | ------------------------------------------------------------------- |
| `services.app.build` | Construye la imagen usando el `Dockerfile` local.                   |
| `ports`              | Mapea el puerto local 8080 al 8080 del contenedor.                  |
| `depends_on`         | Espera a que `db` se inicie antes de iniciar `app`.                 |
| `environment`        | Configura variables de entorno para la conexión a la base de datos. |
| `services.db.image`  | Usa la imagen oficial de MySQL 8.                                   |
| `volumes`            | Crea persistencia de datos MySQL.                                   |
| `networks`           | Define una red interna para que ambos contenedores se comuniquen.   |

---

## 3. Ejecución del entorno

Desde la raíz del proyecto, con el `.jar` ya generado, ejecuta:

```bash
docker compose up --build
```

Esto:

- Construirá la imagen de tu app.
- Descargará e iniciará la imagen oficial de MySQL.
- Levantará ambos servicios en red.

**Resultado exitoso esperado en los logs**:

- MySQL inicializa correctamente en `port 3306`.
- Tomcat se inicia en `port 8080`.
- Spring Boot se conecta exitosamente a la base de datos (`HikariPool - Start completed`).
- El contexto se levanta (`Tomcat started on port 8080`).

---

## Observaciones finales

- El contenedor `app` es totalmente autónomo y puede ejecutarse en cualquier servidor con Docker.
- Si necesitas desplegar este sistema en la nube, basta con subir los archivos `.jar`, `Dockerfile`, y `docker-compose.yml`.
- La variable `SPRING_DATASOURCE_URL` conecta usando `db` como hostname gracias a la red `red-interna`.
