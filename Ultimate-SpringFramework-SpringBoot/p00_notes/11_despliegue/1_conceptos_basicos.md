# Despliegue – Parte 1: Conceptos básicos

El despliegue es el proceso mediante el cual una aplicación pasa del entorno de desarrollo local a un entorno accesible desde el exterior, ya sea un servidor remoto, una nube o un contenedor. En el contexto de Spring Boot, este proceso puede realizarse de varias formas, y para ello es importante entender algunos conceptos clave:

---

## ¿Qué es Maven?

Maven es una herramienta de automatización de construcción usada comúnmente con proyectos Java. Permite:

- Gestionar dependencias (bibliotecas externas).
- Compilar el código fuente.
- Ejecutar pruebas.
- Empaquetar la aplicación como `.jar` o `.war`.
- Ejecutar comandos desde terminal o desde IDEs como IntelliJ o VS Code.

El archivo central de Maven es `pom.xml`, donde se declaran:

- Dependencias
- Plugins
- Configuración de construcción (build)
- Nombre del artefacto y su tipo (jar o war)

---

## ¿Qué es Tomcat?

**Tomcat** es un servidor web que implementa las especificaciones de **Servlets** y **JSP**. Sirve para desplegar aplicaciones Java empaquetadas como **.war** (Web Application Archive).

Spring Boot puede:

- Incluir un servidor Tomcat embebido (por defecto): se empaqueta como `.jar`.
- Ser desplegado en un Tomcat externo: se empaqueta como `.war`.

---

## ¿Qué diferencia hay entre `.jar` y `.war`?

| Característica | `.jar` (Java ARchive) | `.war` (Web ARchive) |
| ---------------- | ------------------------ | ----------------------- |
| Tipo de ejecución | Ejecutable (contiene servidor embebido como Tomcat) | Necesita un servidor externo (Tomcat, Payara, etc.) |
| Arquitectura | Aplicación monolítica empaquetada | Aplicación web tradicional tipo Java EE |
| Despliegue | Simple (java -jar archivo.jar) | Requiere despliegue manual en servidor |
| Flexibilidad | Muy portable | Mejor control para entornos empresariales |

**Spring Boot usa `.jar` por defecto**, ideal para microservicios y despliegue con Docker.

---

## Explicación de los archivos y carpetas del proyecto

```bash

p11-spring-security-jwt/
│
├── .idea/                → Configuración del IDE (IntelliJ IDEA).
├── .mvn/                 → Configuración de Maven Wrapper.
├── .vscode/              → Configuración de Visual Studio Code.
├── src/                  → Código fuente (controladores, servicios, entidades, etc.).
├── target/               → Carpeta generada por Maven con los archivos compilados (como el .jar).
├── .gitattributes        → Configuración de atributos para Git (por ejemplo, finales de línea).
├── .gitignore            → Lista de archivos y carpetas que Git debe ignorar.
├── HELP.md               → Archivo de ayuda/documentación inicial del proyecto (opcional).
├── mvnw                  → Script de Linux/Mac para ejecutar Maven sin necesidad de tenerlo instalado.
├── mvnw\.cmd              → Script de Windows para ejecutar Maven.
├── pom.xml               → Archivo principal de configuración de Maven (dependencias, plugins, etc.).

```

---

## Conclusión

En esta primera parte se definieron los fundamentos del proceso de despliegue:

- Maven como herramienta de construcción.
- Tomcat como servidor de aplicaciones.
- Diferencias clave entre `.jar` y `.war`.
- Estructura general del proyecto Maven típico.

Estos conceptos son esenciales para elegir el tipo de empaquetado, estrategia de ejecución y entorno de despliegue más adecuados.
