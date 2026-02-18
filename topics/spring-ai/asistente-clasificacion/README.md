# Asistente de Clasificación de Tickets con Spring AI

Proyecto de nivel intermedio para aprender **BeanOutputParser** de Spring AI, que permite obtener datos estructurados (objetos Java) en lugar de texto libre.

## Características

- **Clasificación Automática con IA**: Analiza descripciones de problemas y clasifica automáticamente en categoría, prioridad y departamento
- **Generación de Respuestas Sugeridas**: La IA genera respuestas profesionales personalizadas según la categoría, prioridad y departamento del ticket
- **BeanOutputParser**: Convierte la respuesta JSON de Ollama en objetos Java automáticamente
- **Persistencia con SQLite**: Almacena tickets y sus respuestas sugeridas en base de datos local
- **Interfaz Web Completa**: Crear tickets, ver listado, filtrar, cambiar estados y generar respuestas
- **100% Local**: Todo se procesa localmente con Ollama

## Tecnologías

- Spring Boot 4.0.2
- Spring AI 2.0.0-M2 (BeanOutputParser)
- Spring Data JPA
- SQLite
- Ollama (llama3.1:latest)
- Thymeleaf
- Java 21

## Conceptos de Spring AI Aprendidos

### BeanOutputParser

Es la característica más potente de Spring AI para desarrolladores. Permite:

1. **Definir una clase Java** con las propiedades que quieres extraer
2. **Spring AI genera automáticamente** las instrucciones JSON para el modelo
3. **Ollama responde en formato JSON** estructurado
4. **BeanOutputParser convierte** el JSON a tu objeto Java

Ejemplo:
```java
// 1. Define tu clase de salida
public class TicketCategory {
    private String categoria;
    private String prioridad;
    private String departamento;
    private String resumen;
    // getters y setters
}

// 2. Crea el parser
BeanOutputConverter<TicketCategory> outputConverter =
    new BeanOutputConverter<>(TicketCategory.class);

// 3. Obtiene el formato JSON esperado
String format = outputConverter.getFormat();

// 4. Incluye el formato en tu prompt
String prompt = "Clasifica este ticket: {descripcion}\n{format}";

// 5. Envía a la IA y convierte la respuesta
String response = chatClient.prompt(prompt).call().content();
TicketCategory result = outputConverter.convert(response);
```

### Ventajas del BeanOutputParser

- No más parsing manual de texto
- Type-safe: obtienes objetos Java tipados
- Validación automática de estructura
- Reutilizable en toda la aplicación

## Ejemplos de Problemas para Probar

1. "Mi internet está muy lento y no puedo trabajar"
2. "No puedo acceder a mi correo electrónico corporativo"
3. "Mi computadora no enciende desde ayer"
4. "Necesito permisos para acceder a la carpeta compartida"
5. "El sistema de ventas está dando error al guardar"
