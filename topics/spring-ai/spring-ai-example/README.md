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
