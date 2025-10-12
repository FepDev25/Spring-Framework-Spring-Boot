# 🚀 Despliegue Local de una Aplicación Spring Boot

Este documento explica cómo construir y ejecutar una aplicación Spring Boot localmente usando Maven Wrapper (`./mvnw`) y `java -jar`.

---

## 🧱 Paso 1: Construir el proyecto

Desde la raíz del proyecto, ejecuta el siguiente comando para limpiar y empaquetar el proyecto:

```bash
./mvnw clean package
```

Esto generará un archivo `.jar` ejecutable dentro del directorio `target/`.

---

## 📁 Paso 2: Ir al directorio `target`

```bash
cd target
ls
```

Deberías ver un archivo similar a:

```
p1-springboot-web-0.0.1-SNAPSHOT.jar
```

Este archivo contiene toda tu aplicación lista para ser ejecutada.

---

## ▶️ Paso 3: Ejecutar el JAR

Usa el siguiente comando para ejecutar la aplicación:

```bash
java -jar ./p1-springboot-web-0.0.1-SNAPSHOT.jar
```

Verás un resultado similar en consola:

```
:: Spring Boot ::                (v3.4.5)

... Starting P1SpringbootWebApplication ...
... Tomcat initialized with port 8080 (http) ...
... Started P1SpringbootWebApplication in X seconds ...
```

La aplicación estará disponible por defecto en:

```
http://localhost:8080
```

---

## 🧪 Verifica la ejecución

Abre tu navegador o usa `curl` para verificar que el servidor está funcionando:

```bash
curl http://localhost:8080
```

---

## ✅ Notas adicionales

- Si quieres cambiar el puerto, puedes modificar el archivo `application.properties`:

```properties
server.port=9090
```

- Si estás usando Spring DevTools, recuerda que no se incluye en el `.jar` final por defecto (solo para desarrollo).
- Asegúrate de tener Java 17+ o la versión requerida por tu proyecto.

---

Con esto, la aplicación Spring Boot está desplegada localmente y lista para pruebas o demostraciones.
