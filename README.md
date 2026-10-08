# Spring Framework & Spring Boot

Repositorio de aprendizaje de **Spring Framework** y **Spring Boot** con proyectos prácticos, notas y una ruta de estudio progresiva.

## Contenido

| Ruta | Descripción |
| --- | --- |
| `roadmap.md` | Ruta de estudio completa (Módulos 1 a 24): fundamentos, IoC/DI, Spring Boot internals, MVC, datos, seguridad, WebFlux, testing, microservicios, despliegue, etc. |
| `Ultimate-SpringFramework-SpringBoot/` | Curso **"Spring Framework 6 & Spring Boot 3"** (Udemy): proyectos `p01`–`p14` y notas en `p00_notes/`. |
| `topics/spring-ai/` | Proyectos de **Spring AI** (chat, clasificación estructurada y RAG simple con Ollama). |
| `topics/spring-security/` | Proyectos de **Spring Security** organizados en fases progresivas (HTTP Basic, Form Login, JDBC, JPA, Method Security, JWT, OAuth2 y un stack integrador). |

## Requisitos

- **JDK 21** (algunos proyectos del curso usan `java.version=17`, compatibles con JDK 21).
- Conexión a internet la primera vez para descargar dependencias de Maven.
- Las bases de datos MySQL de algunos proyectos del curso se crean con los scripts en `Ultimate-SpringFramework-SpringBoot/databases/`.
- Los proyectos de `topics/spring-ai` requieren **Ollama** en `http://localhost:11434`.

## Cómo ejecutar un proyecto

Cada proyecto es un proyecto Maven independiente con Maven Wrapper:

```bash
./mvnw spring-boot:run
```

O para compilar y empaquetar:

```bash
./mvnw clean package
```

## Estructura general

```
.
├── roadmap.md
├── Ultimate-SpringFramework-SpringBoot/
│   ├── p00_notes/          # Apuntes por tema
│   ├── databases/          # Scripts SQL de apoyo
│   ├── p01-springboot-web/
│   ├── ...
│   └── p14-frontend-angular-backend-springboot/
└── topics/
    ├── spring-ai/
    │   ├── spring-ai-example/
    │   ├── asistente-clasificacion/
    │   └── rag-simple/
    └── spring-security/
        ├── fase01/ ... fase04/
        └── full-security-stack/
```
