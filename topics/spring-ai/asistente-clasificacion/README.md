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

## Estructura del Proyecto

```
src/main/java/com/example/asistente/asistente_clasificacion/
├── AsistenteClasificacionApplication.java   # Clase principal
├── controller/
│   └── TicketController.java                # Controlador web
├── dto/
│   └── TicketCategory.java                  # DTO para BeanOutputParser
├── entity/
│   └── Ticket.java                          # Entidad JPA (incluye respuestaSugerida)
├── repository/
│   └── TicketRepository.java                # Repositorio Spring Data
└── service/
    └── TicketService.java                   # Servicio con lógica de IA (clasificación + respuestas)

src/main/resources/
├── application.properties                    # Configuración
└── templates/
    ├── index.html                           # Formulario principal
    ├── resultado.html                       # Vista de ticket clasificado
    └── lista.html                           # Lista de todos los tickets
```

## Prerequisitos

1. Java 21
2. Ollama instalado y corriendo en puerto 11434
3. Modelo llama3.1 descargado

Verificar:
```bash
ollama list
curl http://localhost:11434/api/tags
```

## Cómo Ejecutar

1. Navega al directorio:
```bash
cd asistente-clasificacion
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

### Crear un Ticket

1. Describe tu problema en lenguaje natural (ej: "Mi internet está muy lento")
2. La IA analiza y clasifica automáticamente:
   - **Categoría**: RED, SOFTWARE, HARDWARE, ACCESO, CORREO, OTRO
   - **Prioridad**: CRITICA, ALTA, MEDIA, BAJA
   - **Departamento**: SISTEMAS, SOPORTE, INFRAESTRUCTURA, SEGURIDAD
   - **Resumen**: Un breve resumen generado por la IA
3. El ticket se guarda en SQLite
4. Puedes ver el resultado con toda la clasificación

### Ver y Gestionar Tickets

1. Haz clic en "Ver Todos los Tickets"
2. Filtra por prioridad o estado
3. Cambia el estado de un ticket directamente desde la tabla
4. Los colores ayudan a identificar prioridades rápidamente
5. Haz clic en cualquier ticket para ver su detalle completo

### Generar Respuesta Sugerida

1. Después de clasificar un ticket, haz clic en "Generar Respuesta Sugerida"
2. La IA analiza el ticket y genera una respuesta profesional personalizada:
   - Adapta el tono según la prioridad (más empático para críticas)
   - Incluye sugerencias específicas según la categoría
   - Firma como "Tu Asistente de IA"
3. La respuesta se guarda automáticamente en el ticket
4. En el listado de tickets, verás ✨ en los tickets que tienen respuesta sugerida

## Base de Datos

Los tickets se almacenan en `tickets.db` (SQLite) con la siguiente estructura:

- **id**: Identificador único
- **descripcion**: Problema reportado por el usuario
- **categoria**: Clasificación automática
- **prioridad**: Nivel de urgencia
- **departamento**: Área responsable
- **resumen**: Generado por la IA
- **respuestaSugerida**: Respuesta generada por IA (opcional)
- **fechaCreacion**: Timestamp
- **estado**: ABIERTO, EN_PROCESO, RESUELTO, CERRADO

## Configuración Importante

En `application.properties`:

```properties
# Forzar respuesta en JSON
spring.ai.ollama.chat.options.format=json

# Temperatura baja para respuestas más consistentes
spring.ai.ollama.chat.options.temperature=0.3
```

La opción `format=json` es crucial para que Ollama responda siempre en JSON válido.

## Próximos Pasos

- Añadir asignación automática de tickets a técnicos
- Implementar notificaciones por email
- Crear dashboard con estadísticas
- Añadir búsqueda de texto completo
- Implementar comentarios en tickets
- Exportar reportes en PDF
- Permitir editar respuestas sugeridas antes de enviar
- Añadir opción para regenerar respuesta

## Ejemplos de Problemas para Probar

1. "Mi internet está muy lento y no puedo trabajar"
2. "No puedo acceder a mi correo electrónico corporativo"
3. "Mi computadora no enciende desde ayer"
4. "Necesito permisos para acceder a la carpeta compartida"
5. "El sistema de ventas está dando error al guardar"

La IA clasificará cada uno automáticamente con la categoría, prioridad y departamento apropiados.
