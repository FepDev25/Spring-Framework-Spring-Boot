# 1. Nivel Básico: "El Generador de Resúmenes Privado"

- Este proyecto es ideal para entender los objetos ChatClient y PromptTemplate.

- Concepto: Una aplicación simple donde pegas un texto largo (ej. un artículo de noticias o un correo) y la IA te devuelve un resumen en 3 viñetas o un análisis de sentimiento.

- Características clave a implementar:
    - Prompt Engineering básico: Cómo inyectar el texto del usuario en una plantilla de instrucciones para Ollama (ej. "Actúa como un editor experto y resume el siguiente texto...").
    - Thymeleaf: Crear un formulario simple para la entrada y una vista para mostrar el resultado generado.
    - Configuración: Conectar Spring Boot a instancia local de Ollama (puerto 11434).

## IA

- Modelos de IA que tengo:

~/Documentos/programacion/proyectos/spring-ai ❯ ollama list                                                                                                             12:12:14 
NAME                       ID              SIZE      MODIFIED    
llama3.1:latest            46e0c10c039e    4.9 GB    12 days ago    
nomic-embed-text:latest    0a109f422b47    274 MB    7 weeks ago    
mistral-nemo:latest        e7e06d107c6c    7.1 GB    7 weeks ago    

- Uso el modelo "llama3.1:latest" para este proyecto.
