# Generador de Resúmenes Privado con Spring AI

Proyecto de nivel básico para aprender Spring AI, utilizando ChatClient y PromptTemplate con Ollama local.

## Características

- **Generación de resúmenes**: Convierte textos largos en 3 viñetas claras y concisas
- **Análisis de sentimiento**: Analiza el tono emocional de cualquier texto
- **Traducción a francés**: Traduce cualquier texto al francés manteniendo el significado y tono original
- **100% Privado**: Todo se procesa localmente con Ollama, tus datos nunca salen de tu computadora
- **Interface web simple**: Creada con Thymeleaf

## Tecnologías

- Spring Boot 4.0.2
- Spring AI 2.0.0-M2
- Ollama (llama3.1:latest)
- Thymeleaf
- Java 21

## Prerequisitos

1. Java 21 instalado
2. Ollama instalado y corriendo en puerto 11434
3. Modelo llama3.1 descargado en Ollama

Verificar que Ollama esté corriendo:
```bash
ollama list
curl http://localhost:11434/api/tags
```

## Cómo ejecutar

1. Navega al directorio del proyecto:
```bash
cd spring-ai-example
```

2. Ejecuta la aplicación:
```bash
./mvnw spring-boot:run
```

3. Abre tu navegador en:
```
http://localhost:8080
```

## Uso

1. Pega cualquier texto en el área de texto (artículo, correo, nota, etc.)
2. Selecciona una acción:
   - **Generar Resumen**: Obtén 3 viñetas con los puntos clave
   - **Analizar Sentimiento**: Descubre el tono emocional del texto
   - **Traducir al Francés**: Traduce el texto al francés manteniendo el significado original
3. Espera unos segundos mientras la IA procesa tu texto
4. Revisa los resultados y analiza otro texto si lo deseas

## Estructura del Proyecto

```
src/main/java/com/example/spring/ai/spring_ai_example/
├── SpringAiExampleApplication.java     # Clase principal
├── controller/
│   └── SummaryController.java          # Controlador web
└── service/
    └── SummaryService.java              # Servicio con lógica de IA (resumen, sentimiento, traducción)

src/main/resources/
├── application.properties               # Configuración de Ollama
└── templates/
    ├── index.html                       # Formulario principal
    └── result.html                      # Vista de resultados
```

## Conceptos de Spring AI aprendidos

### ChatClient
- Cliente principal para interactuar con modelos de IA
- Se inyecta via `ChatClient.Builder`
- Permite enviar prompts y recibir respuestas

### PromptTemplate
- Permite crear plantillas de prompts reutilizables
- Soporta variables con sintaxis `{variable}`
- Facilita el Prompt Engineering organizado

Ejemplo de uso:
```java
String promptTemplate = """
    Actúa como un experto y {accion} el siguiente texto:
    {text}
    """;

PromptTemplate template = new PromptTemplate(promptTemplate);
var prompt = template.create(Map.of(
    "accion", "resume",
    "text", userText
));
```
