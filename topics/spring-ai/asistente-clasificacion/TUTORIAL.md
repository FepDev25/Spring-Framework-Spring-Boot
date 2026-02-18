# Tutorial Completo: Asistente de Clasificación de Tickets con Spring AI

## Tabla de Contenidos

1. [Introducción](#introducción)
2. [Conceptos Fundamentales](#conceptos-fundamentales)
3. [Arquitectura del Proyecto](#arquitectura-del-proyecto)
4. [Configuración del Entorno](#configuración-del-entorno)
5. [Componentes del Sistema](#componentes-del-sistema)
6. [Flujo de Datos](#flujo-de-datos)
7. [Implementación Detallada](#implementación-detallada)
8. [Ejecución y Pruebas](#ejecución-y-pruebas)
9. [Conceptos Avanzados](#conceptos-avanzados)
10. [Conclusiones](#conclusiones)

---

## Introducción

### ¿Qué problema resuelve este proyecto?

En un sistema de soporte técnico tradicional, cuando un usuario reporta un problema, un operador humano debe leerlo, entenderlo y clasificarlo manualmente. Este proceso:

- Consume tiempo valioso
- Es propenso a errores humanos
- Requiere personal capacitado
- No escala eficientemente

Este proyecto automatiza completamente ese proceso usando inteligencia artificial local, clasificando tickets automáticamente en categorías, prioridades y departamentos, además de generar respuestas sugeridas profesionales.

### Objetivos de Aprendizaje

Al completar este tutorial, comprenderás:

1. Cómo usar **BeanOutputParser** de Spring AI para obtener datos estructurados desde un LLM
2. Cómo integrar Ollama (LLM local) con Spring Boot
3. Cómo persistir datos clasificados por IA en SQLite usando Spring Data JPA
4. Cómo construir una interfaz web completa con Thymeleaf
5. Patrones de diseño para aplicaciones con IA

### Tecnologías Utilizadas

- **Spring Boot 4.0.2**: Framework principal de la aplicación
- **Spring AI 2.0.0-M2**: Framework para integración con LLMs
- **Spring Data JPA**: Persistencia de datos
- **SQLite**: Base de datos embebida
- **Ollama**: Servidor de LLMs local (llama3.1)
- **Thymeleaf**: Motor de plantillas para la interfaz web
- **Java 21**: Lenguaje de programación

---

## Conceptos Fundamentales

### ¿Qué es un LLM (Large Language Model)?

Un LLM es un modelo de inteligencia artificial entrenado con enormes cantidades de texto para entender y generar lenguaje natural. Ejemplos: GPT-4, Claude, Llama.

### ¿Qué es Ollama?

Ollama es un servidor que permite ejecutar LLMs localmente en tu computadora, sin necesidad de APIs externas ni conexión a internet. Esto ofrece:

- **Privacidad**: Los datos nunca salen de tu máquina
- **Sin costos**: No hay pagos por tokens o llamadas API
- **Baja latencia**: Procesamiento local más rápido
- **Control total**: Puedes elegir el modelo y configuración

### El Problema de las Respuestas en Texto Libre

Tradicionalmente, cuando consultas a un LLM obtienes texto libre:

```
Usuario: "Clasifica este ticket: Mi internet está lento"
IA: "Este problema parece estar relacionado con la red..."
```

Este formato es excelente para humanos, pero terrible para sistemas automatizados que necesitan tomar decisiones basadas en la respuesta.

### La Solución: BeanOutputParser

**BeanOutputParser** es la característica más importante de Spring AI para desarrolladores. Permite transformar respuestas de texto libre en objetos Java estructurados:

```java
// En lugar de texto libre, obtienes un objeto Java tipado:
TicketCategory result = {
    categoria: "RED",
    prioridad: "ALTA",
    departamento: "SISTEMAS",
    resumen: "Problema de conectividad de red"
}
```

#### ¿Cómo Funciona BeanOutputParser?

1. **Defines una clase Java** con las propiedades que necesitas extraer
2. **Spring AI genera automáticamente** instrucciones JSON para el LLM
3. **El LLM responde en formato JSON** estructurado
4. **BeanOutputParser convierte** el JSON a tu objeto Java

Este proceso elimina completamente el parsing manual y garantiza type-safety.

---

## Arquitectura del Proyecto

### Estructura de Directorios

```
asistente-clasificacion/
├── src/main/java/com/example/asistente/asistente_clasificacion/
│   ├── AsistenteClasificacionApplication.java   # Punto de entrada
│   ├── controller/
│   │   └── TicketController.java                # Controladores HTTP
│   ├── dto/
│   │   └── TicketCategory.java                  # DTO para BeanOutputParser
│   ├── entity/
│   │   └── Ticket.java                          # Entidad JPA
│   ├── repository/
│   │   └── TicketRepository.java                # Acceso a datos
│   └── service/
│       └── TicketService.java                   # Lógica de negocio + IA
├── src/main/resources/
│   ├── application.properties                    # Configuración
│   └── templates/
│       ├── index.html                           # Formulario de creación
│       ├── resultado.html                       # Vista de ticket individual
│       └── lista.html                           # Lista de tickets
└── pom.xml                                       # Dependencias Maven
```

### Patrón Arquitectónico

El proyecto sigue una arquitectura en capas:

```
┌─────────────────────────────────────┐
│   Capa de Presentación (Thymeleaf) │
│   - index.html, resultado.html      │
└─────────────────────────────────────┘
              ↓
┌─────────────────────────────────────┐
│   Capa de Controladores (Spring)   │
│   - TicketController                │
└─────────────────────────────────────┘
              ↓
┌─────────────────────────────────────┐
│   Capa de Servicios (IA + Lógica)  │
│   - TicketService + BeanOutputParser│
└─────────────────────────────────────┘
              ↓
┌─────────────────────────────────────┐
│   Capa de Persistencia (JPA)       │
│   - TicketRepository + SQLite       │
└─────────────────────────────────────┘
```

---

## Configuración del Entorno

### Prerequisitos

1. **Java 21 instalado**
   ```bash
   java -version
   # Debe mostrar: openjdk version "21" o superior
   ```

2. **Maven instalado** (opcional, el proyecto incluye Maven Wrapper)
   ```bash
   mvn -version
   ```

3. **Ollama instalado y en ejecución**
   ```bash
   # Instalar Ollama (Linux/macOS)
   curl -fsSL https://ollama.com/install.sh | sh

   # Descargar el modelo llama3.1
   ollama pull llama3.1:latest

   # Verificar que Ollama está corriendo
   curl http://localhost:11434/api/tags
   ```

### Configuración de la Aplicación

El archivo `application.properties` contiene toda la configuración:

```properties
# Nombre de la aplicación
spring.application.name=asistente-clasificacion

# Configuración de Ollama
spring.ai.ollama.base-url=http://localhost:11434
spring.ai.ollama.chat.options.model=llama3.1:latest
spring.ai.ollama.chat.options.temperature=0.3
spring.ai.ollama.chat.options.format=json

# Configuración de SQLite
spring.datasource.url=jdbc:sqlite:tickets.db
spring.datasource.driver-class-name=org.sqlite.JDBC
spring.jpa.database-platform=org.hibernate.community.dialect.SQLiteDialect
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
```

#### Explicación de Parámetros Clave

**Ollama:**
- `temperature=0.3`: Baja temperatura para respuestas más consistentes y predecibles (rango: 0.0-1.0)
- `format=json`: Fuerza a Ollama a responder siempre en JSON válido (crítico para BeanOutputParser)

**SQLite:**
- `hibernate.ddl-auto=update`: Crea/actualiza automáticamente las tablas según las entidades
- `show-sql=true`: Muestra en consola las consultas SQL ejecutadas (útil para debugging)

---

## Componentes del Sistema

### 1. TicketCategory (DTO)

**Ubicación**: `src/main/java/.../dto/TicketCategory.java`

```java
public class TicketCategory {
    @JsonProperty("categoria")
    private String categoria;

    @JsonProperty("prioridad")
    private String prioridad;

    @JsonProperty("departamento")
    private String departamento;

    @JsonProperty("resumen")
    private String resumen;

    // Constructor, getters, setters...
}
```

**Propósito**: Representa la estructura de datos que esperamos del LLM.

**Detalles Importantes**:
- Las anotaciones `@JsonProperty` aseguran el mapeo correcto desde JSON
- No es una entidad JPA (no se persiste directamente)
- Actúa como un "contrato" entre la IA y nuestra aplicación
- Es inmutable conceptualmente (solo se crea, no se modifica)

**¿Por qué usar un DTO separado?**
- Separa la representación de la IA de la base de datos
- Permite validación específica de respuestas de IA
- Facilita cambios en el formato sin afectar la entidad

### 2. Ticket (Entidad JPA)

**Ubicación**: `src/main/java/.../entity/Ticket.java`

```java
@Entity
@Table(name = "tickets")
public class Ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 1000)
    private String descripcion;

    @Column(nullable = false)
    private String categoria;

    @Column(nullable = false)
    private String prioridad;

    @Column(nullable = false)
    private String departamento;

    @Column(length = 500)
    private String resumen;

    @Column(nullable = false)
    private LocalDateTime fechaCreacion;

    @Column(nullable = false)
    private String estado;

    @Column(length = 2000)
    private String respuestaSugerida;

    // Constructor, getters, setters...
}
```

**Propósito**: Modelo de datos persistente en la base de datos.

**Campos Explicados**:
- `id`: Identificador único auto-incremental
- `descripcion`: Problema original reportado por el usuario (max 1000 caracteres)
- `categoria`: Clasificación automática (RED, SOFTWARE, HARDWARE, etc.)
- `prioridad`: Urgencia (CRITICA, ALTA, MEDIA, BAJA)
- `departamento`: Área responsable (SISTEMAS, SOPORTE, etc.)
- `resumen`: Resumen generado por IA (max 500 caracteres)
- `fechaCreacion`: Timestamp automático al crear
- `estado`: Ciclo de vida (ABIERTO, EN_PROCESO, RESUELTO, CERRADO)
- `respuestaSugerida`: Respuesta generada por IA (opcional, max 2000 caracteres)

**Constructor por Defecto**:
```java
public Ticket() {
    this.fechaCreacion = LocalDateTime.now();
    this.estado = "ABIERTO";
}
```
Inicializa valores predeterminados automáticamente.

### 3. TicketRepository (Acceso a Datos)

**Ubicación**: `src/main/java/.../repository/TicketRepository.java`

```java
@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {
    List<Ticket> findByEstadoOrderByFechaCreacionDesc(String estado);
    List<Ticket> findAllByOrderByFechaCreacionDesc();
    List<Ticket> findByPrioridadOrderByFechaCreacionDesc(String prioridad);
    List<Ticket> findByCategoriaOrderByFechaCreacionDesc(String categoria);
}
```

**Propósito**: Capa de acceso a datos usando Spring Data JPA.

**Ventajas de Spring Data JPA**:
- No necesitas escribir SQL manualmente
- Los métodos se generan automáticamente basándose en su nombre
- Soporte para paginación, ordenamiento y queries complejas

**Método Query Derivation**:
```java
findByEstadoOrderByFechaCreacionDesc
↓
SELECT * FROM tickets
WHERE estado = ?
ORDER BY fecha_creacion DESC
```

Spring Data genera automáticamente la consulta SQL basándose en la nomenclatura del método.

### 4. TicketService (Lógica de Negocio + IA)

**Ubicación**: `src/main/java/.../service/TicketService.java`

Esta es la clase más importante del proyecto, donde ocurre toda la magia de la IA.

#### Constructor e Inyección de Dependencias

```java
@Service
public class TicketService {
    private final ChatClient chatClient;
    private final TicketRepository ticketRepository;
    private final BeanOutputConverter<TicketCategory> outputConverter;

    public TicketService(ChatClient.Builder chatClientBuilder,
                         TicketRepository ticketRepository) {
        this.chatClient = chatClientBuilder.build();
        this.ticketRepository = ticketRepository;
        this.outputConverter = new BeanOutputConverter<>(TicketCategory.class);
    }
}
```

**Componentes Inyectados**:
- `ChatClient`: Cliente de Spring AI para comunicarse con Ollama
- `TicketRepository`: Acceso a la base de datos
- `BeanOutputConverter<TicketCategory>`: Conversor de JSON a objeto Java

#### Método clasificarTicket() - El Corazón del Sistema

```java
public TicketCategory clasificarTicket(String descripcion) {
    // 1. Obtener el formato JSON esperado
    String format = outputConverter.getFormat();

    // 2. Construir el prompt con instrucciones claras
    String promptTemplate = """
        Eres un asistente experto en clasificación de tickets de soporte técnico.

        Analiza el siguiente problema reportado por un usuario y clasifícalo:

        Problema: {descripcion}

        Debes clasificar el ticket en:
        - categoria: RED, SOFTWARE, HARDWARE, ACCESO, CORREO, OTRO
        - prioridad: CRITICA, ALTA, MEDIA, BAJA
        - departamento: SISTEMAS, SOPORTE, INFRAESTRUCTURA, SEGURIDAD
        - resumen: Un resumen breve del problema en una línea

        {format}
        """;

    // 3. Crear el prompt con valores dinámicos
    PromptTemplate template = new PromptTemplate(promptTemplate);
    var prompt = template.create(Map.of(
        "descripcion", descripcion,
        "format", format
    ));

    // 4. Enviar a Ollama y obtener respuesta JSON
    String response = chatClient.prompt(prompt)
            .call()
            .content();

    // 5. Convertir JSON a objeto Java
    TicketCategory category = outputConverter.convert(response);

    return category;
}
```

**Flujo Detallado**:

1. **Obtención del Formato**: `outputConverter.getFormat()` genera automáticamente instrucciones JSON basadas en la clase `TicketCategory`:
   ```json
   {
     "categoria": "string",
     "prioridad": "string",
     "departamento": "string",
     "resumen": "string"
   }
   ```

2. **Construcción del Prompt**: Se usa un template con placeholders `{descripcion}` y `{format}` que se reemplazan dinámicamente.

3. **Llamada a la IA**: `chatClient.prompt(prompt).call().content()` envía el prompt a Ollama y obtiene la respuesta.

4. **Conversión Automática**: `outputConverter.convert(response)` parsea el JSON y crea un objeto `TicketCategory`.

#### Método generarYGuardarRespuesta()

```java
public Ticket generarYGuardarRespuesta(Long ticketId) {
    Ticket ticket = obtenerPorId(ticketId);

    if (ticket.getRespuestaSugerida() != null) {
        return ticket; // Ya existe, no regenerar
    }

    String promptTemplate = """
        Actúa como un agente de soporte técnico del departamento de {departamento}.
        Escribe una respuesta corta y profesional para el siguiente ticket:

        Problema: {descripcion}
        Prioridad: {prioridad}
        Categoría: {categoria}

        Instrucciones de tono:
        - Si la prioridad es CRITICA, sé muy empático y asegura atención inmediata.
        - Si la categoría es ACCESO, sugiere verificar mayúsculas o reiniciar el router.
        - Firma como "Tu Asistente de IA".
        """;

    PromptTemplate template = new PromptTemplate(promptTemplate);
    var prompt = template.create(Map.of(
        "departamento", ticket.getDepartamento(),
        "descripcion", ticket.getDescripcion(),
        "prioridad", ticket.getPrioridad(),
        "categoria", ticket.getCategoria()
    ));

    String respuesta = chatClient.prompt(prompt).call().content();

    ticket.setRespuestaSugerida(respuesta);
    return ticketRepository.save(ticket);
}
```

**Diferencia con clasificarTicket()**:
- No usa BeanOutputParser (queremos texto libre, no JSON estructurado)
- Genera una respuesta profesional adaptada al contexto del ticket
- Usa información previamente clasificada para personalizar la respuesta

### 5. TicketController (Controladores HTTP)

**Ubicación**: `src/main/java/.../controller/TicketController.java`

```java
@Controller
public class TicketController {
    private final TicketService ticketService;

    @GetMapping("/")
    public String index(Model model) {
        List<Ticket> tickets = ticketService.obtenerTodos();
        model.addAttribute("tickets", tickets);
        return "index";
    }

    @PostMapping("/clasificar")
    public String clasificarTicket(@RequestParam String descripcion, Model model) {
        Ticket ticket = ticketService.clasificarYGuardar(descripcion);
        model.addAttribute("ticket", ticket);
        return "resultado";
    }

    @PostMapping("/tickets/{id}/respuesta")
    public String generarRespuesta(@PathVariable Long id, Model model) {
        Ticket ticket = ticketService.generarYGuardarRespuesta(id);
        model.addAttribute("ticket", ticket);
        return "resultado";
    }

    @GetMapping("/tickets/{id}")
    public String verTicket(@PathVariable Long id, Model model) {
        Ticket ticket = ticketService.obtenerPorId(id);
        model.addAttribute("ticket", ticket);
        return "resultado";
    }

    @GetMapping("/tickets")
    public String listarTickets(@RequestParam(required = false) String filtro,
                                @RequestParam(required = false) String valor,
                                Model model) {
        List<Ticket> tickets;

        if (filtro != null && valor != null) {
            tickets = switch (filtro) {
                case "prioridad" -> ticketService.obtenerPorPrioridad(valor);
                case "estado" -> ticketService.obtenerPorEstado(valor);
                default -> ticketService.obtenerTodos();
            };
        } else {
            tickets = ticketService.obtenerTodos();
        }

        model.addAttribute("tickets", tickets);
        return "lista";
    }

    @PostMapping("/tickets/{id}/estado")
    public String cambiarEstado(@PathVariable Long id, @RequestParam String estado) {
        ticketService.actualizarEstado(id, estado);
        return "redirect:/tickets";
    }
}
```

**Endpoints Explicados**:

| Endpoint | Método | Propósito |
|----------|--------|-----------|
| `/` | GET | Página principal con formulario |
| `/clasificar` | POST | Clasifica y guarda un nuevo ticket |
| `/tickets/{id}/respuesta` | POST | Genera respuesta sugerida para un ticket |
| `/tickets/{id}` | GET | Ver detalles de un ticket específico |
| `/tickets` | GET | Lista todos los tickets (con filtros opcionales) |
| `/tickets/{id}/estado` | POST | Cambia el estado de un ticket |

**Patrón Model-View-Controller**:
1. El controlador recibe la petición HTTP
2. Llama al servicio para ejecutar la lógica
3. Agrega datos al Model
4. Retorna el nombre de la vista Thymeleaf

### 6. Vistas Thymeleaf

#### index.html - Formulario de Creación

**Funcionalidades**:
- Formulario para reportar problemas
- Muestra los últimos 5 tickets creados
- Información educativa sobre el proceso

**Snippet Importante**:
```html
<form method="post" action="/clasificar">
    <textarea id="descripcion" name="descripcion"
              rows="6" required></textarea>
    <button type="submit">Clasificar y Crear Ticket</button>
</form>
```

#### resultado.html - Vista de Ticket Individual

**Funcionalidades**:
- Muestra todos los detalles del ticket clasificado
- Badges de colores según prioridad
- Botón para generar respuesta sugerida (si no existe)
- Muestra respuesta sugerida (si existe)

**Snippet Importante - Visualización de Respuesta**:
```html
<div th:if="${ticket.respuestaSugerida != null}">
    <div class="section-title">Respuesta Sugerida por IA</div>
    <div class="respuesta-box" th:text="${ticket.respuestaSugerida}"></div>
</div>
```

#### lista.html - Gestión de Tickets

**Funcionalidades**:
- Tabla completa de tickets
- Filtros por prioridad y estado
- Cambio de estado inline
- Click en cualquier fila para ver detalles
- Indicador de tickets con respuesta sugerida

**Snippet Importante - Cambio de Estado**:
```html
<select class="action-select"
        th:onchange="'cambiarEstado(' + ${ticket.id} + ', this)'">
    <option th:value="${ticket.estado}" selected></option>
    <option value="ABIERTO">ABIERTO</option>
    <option value="EN_PROCESO">EN_PROCESO</option>
    <option value="RESUELTO">RESUELTO</option>
    <option value="CERRADO">CERRADO</option>
</select>
```

---

## Flujo de Datos

### Flujo de Clasificación de Ticket

```
1. Usuario escribe problema
   "Mi internet está muy lento"
          ↓
2. Formulario HTML envía POST /clasificar
          ↓
3. TicketController recibe descripcion
          ↓
4. Llama a ticketService.clasificarYGuardar()
          ↓
5. TicketService crea prompt con BeanOutputParser
          ↓
6. ChatClient envía prompt a Ollama
          ↓
7. Ollama (llama3.1) analiza y responde JSON:
   {
     "categoria": "RED",
     "prioridad": "ALTA",
     "departamento": "SISTEMAS",
     "resumen": "Problema de conectividad de red"
   }
          ↓
8. BeanOutputConverter convierte JSON a TicketCategory
          ↓
9. Se crea entidad Ticket con los datos clasificados
          ↓
10. TicketRepository guarda en SQLite
          ↓
11. Controller retorna vista resultado.html
          ↓
12. Usuario ve ticket clasificado
```

### Flujo de Generación de Respuesta

```
1. Usuario hace clic en "Generar Respuesta Sugerida"
          ↓
2. POST /tickets/{id}/respuesta
          ↓
3. TicketService.generarYGuardarRespuesta(id)
          ↓
4. Recupera ticket de la base de datos
          ↓
5. Verifica si ya tiene respuesta (evita regeneración)
          ↓
6. Construye prompt con contexto del ticket
          ↓
7. ChatClient envía a Ollama (sin BeanOutputParser)
          ↓
8. Ollama genera respuesta profesional personalizada
          ↓
9. Actualiza ticket.respuestaSugerida
          ↓
10. Guarda en base de datos
          ↓
11. Muestra ticket actualizado con respuesta
```

---

## Implementación Detallada

### Dependencias Maven (pom.xml)

```xml
<dependencies>
    <!-- Spring Boot Starters -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-webmvc</artifactId>
    </dependency>

    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-thymeleaf</artifactId>
    </dependency>

    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-data-jpa</artifactId>
    </dependency>

    <!-- Spring AI - Integración con Ollama -->
    <dependency>
        <groupId>org.springframework.ai</groupId>
        <artifactId>spring-ai-starter-model-ollama</artifactId>
    </dependency>

    <!-- SQLite -->
    <dependency>
        <groupId>org.xerial</groupId>
        <artifactId>sqlite-jdbc</artifactId>
    </dependency>

    <dependency>
        <groupId>org.hibernate.orm</groupId>
        <artifactId>hibernate-community-dialects</artifactId>
    </dependency>
</dependencies>
```

**Dependencias Clave Explicadas**:

1. **spring-ai-starter-model-ollama**: La estrella del proyecto
   - Proporciona ChatClient para comunicarse con Ollama
   - Incluye BeanOutputConverter
   - Maneja automáticamente la serialización/deserialización

2. **sqlite-jdbc**: Driver JDBC para SQLite
   - Base de datos embebida (sin necesidad de servidor)
   - Perfecta para desarrollo y aplicaciones pequeñas
   - El archivo `tickets.db` se crea automáticamente

3. **hibernate-community-dialects**: Soporte de Hibernate para SQLite
   - Hibernate no soporta SQLite oficialmente
   - Este módulo comunitario agrega el dialecto necesario

### Prompt Engineering - Mejores Prácticas

El prompt es la comunicación entre tu código y la IA. Un buen prompt es crucial.

#### Ejemplo de Prompt Efectivo

```java
String promptTemplate = """
    Eres un asistente experto en clasificación de tickets de soporte técnico.

    Analiza el siguiente problema reportado por un usuario y clasifícalo:

    Problema: {descripcion}

    Debes clasificar el ticket en:
    - categoria: RED, SOFTWARE, HARDWARE, ACCESO, CORREO, OTRO
    - prioridad: CRITICA, ALTA, MEDIA, BAJA
    - departamento: SISTEMAS, SOPORTE, INFRAESTRUCTURA, SEGURIDAD
    - resumen: Un resumen breve del problema en una línea

    {format}
    """;
```

**Elementos de un Buen Prompt**:

1. **Rol Claro**: "Eres un asistente experto..." establece el contexto
2. **Tarea Específica**: "Analiza y clasifícalo" es una acción clara
3. **Valores Permitidos**: Lista explícita de opciones válidas
4. **Formato de Salida**: `{format}` insertado automáticamente por BeanOutputParser

**Prompts a Evitar**:

```java
// MAL: Demasiado vago
"Clasifica esto: {descripcion}"

// MAL: Sin valores específicos
"Dame la categoría del ticket"

// MAL: Sin formato de salida
"Analiza: {descripcion} y responde con categoría y prioridad"
```

---

## Ejecución y Pruebas

### Paso 1: Verificar Ollama

```bash
# Verificar que Ollama está corriendo
curl http://localhost:11434/api/tags

# Deberías ver una respuesta JSON con tus modelos instalados
```

### Paso 2: Compilar el Proyecto

```bash
cd asistente-clasificacion
./mvnw clean package
```

### Paso 3: Ejecutar la Aplicación

```bash
./mvnw spring-boot:run
```

Verás en la consola:
```
Started AsistenteClasificacionApplication in 3.245 seconds
```

### Paso 4: Acceder a la Aplicación

Abre tu navegador en: `http://localhost:8080`

### Ejemplos de Prueba

#### Caso 1: Problema de Red

**Entrada**:
```
Mi internet está muy lento desde esta mañana y no puedo
acceder a la página web de la empresa
```

**Clasificación Esperada**:
- Categoría: RED
- Prioridad: ALTA
- Departamento: INFRAESTRUCTURA
- Resumen: "Problema de conectividad de red lenta"

#### Caso 2: Problema de Acceso

**Entrada**:
```
No puedo acceder a mi correo electrónico corporativo,
dice que mi contraseña es incorrecta
```

**Clasificación Esperada**:
- Categoría: ACCESO
- Prioridad: MEDIA
- Departamento: SOPORTE
- Resumen: "Problema de acceso a correo electrónico"

#### Caso 3: Problema Crítico

**Entrada**:
```
El servidor de base de datos principal está caído,
ningún cliente puede hacer transacciones
```

**Clasificación Esperada**:
- Categoría: HARDWARE/SOFTWARE
- Prioridad: CRITICA
- Departamento: SISTEMAS
- Resumen: "Caída del servidor de base de datos"

### Verificar la Base de Datos

```bash
# Instalar sqlite3 si no lo tienes
sudo apt install sqlite3  # Linux
brew install sqlite       # macOS

# Abrir la base de datos
sqlite3 tickets.db

# Ver todas las tablas
.tables

# Ver todos los tickets
SELECT * FROM tickets;

# Salir
.exit
```

---

## Conceptos Avanzados

### 1. Temperature en LLMs

El parámetro `temperature` controla la "creatividad" del modelo:

- **Temperature = 0.0**: Determinista, siempre elige la opción más probable
  - Uso: Clasificación, extracción de datos
  - Ventaja: Respuestas consistentes y predecibles

- **Temperature = 0.7-1.0**: Creativo, explora opciones menos probables
  - Uso: Generación de texto creativo, respuestas variadas
  - Ventaja: Respuestas más naturales y diversas

En nuestro proyecto:
```properties
spring.ai.ollama.chat.options.temperature=0.3
```

Usamos 0.3 para clasificación porque queremos consistencia, pero con algo de flexibilidad.

### 2. Formato JSON Forzado

```properties
spring.ai.ollama.chat.options.format=json
```

Esta configuración es crucial. Sin ella, Ollama podría responder:
```
El ticket parece ser de la categoría RED con prioridad ALTA...
```

Con `format=json`, garantizamos:
```json
{
  "categoria": "RED",
  "prioridad": "ALTA",
  "departamento": "SISTEMAS",
  "resumen": "Problema de red"
}
```

### 3. Estrategias de Manejo de Errores

Actualmente, el proyecto confía en que Ollama siempre responderá correctamente. En producción, deberías agregar:

```java
public TicketCategory clasificarTicket(String descripcion) {
    try {
        String response = chatClient.prompt(prompt).call().content();
        return outputConverter.convert(response);
    } catch (JsonProcessingException e) {
        // La IA no respondió JSON válido
        log.error("Error al parsear respuesta de IA: {}", e.getMessage());
        return crearCategoriaDefault(descripcion);
    } catch (Exception e) {
        // Error de conexión con Ollama
        log.error("Error al comunicarse con Ollama: {}", e.getMessage());
        throw new ServiceUnavailableException("El servicio de IA no está disponible");
    }
}
```

### 4. Caché de Respuestas

Para mejorar el rendimiento, podrías cachear respuestas de la IA:

```java
@Cacheable(value = "clasificaciones", key = "#descripcion")
public TicketCategory clasificarTicket(String descripcion) {
    // Si la descripción es idéntica a una anterior,
    // Spring devuelve el resultado cacheado
}
```

### 5. Validación de Respuestas de IA

Agregar validación para asegurar que la IA clasificó correctamente:

```java
private void validarCategoria(TicketCategory categoria) {
    List<String> categoriasValidas = List.of(
        "RED", "SOFTWARE", "HARDWARE", "ACCESO", "CORREO", "OTRO"
    );

    if (!categoriasValidas.contains(categoria.getCategoria())) {
        log.warn("Categoría inválida: {}", categoria.getCategoria());
        categoria.setCategoria("OTRO");
    }
}
```

### 6. Modelos Alternativos

Puedes cambiar fácilmente el modelo de IA:

```properties
# Modelos más pequeños y rápidos
spring.ai.ollama.chat.options.model=llama3.1:8b

# Modelos más grandes y precisos
spring.ai.ollama.chat.options.model=llama3.1:70b

# Modelos especializados
spring.ai.ollama.chat.options.model=codellama:latest
```

### 7. Procesamiento por Lotes

Para clasificar múltiples tickets a la vez:

```java
public List<Ticket> clasificarLote(List<String> descripciones) {
    return descripciones.parallelStream()
        .map(this::clasificarYGuardar)
        .collect(Collectors.toList());
}
```

---

## Conclusiones

### Lo que has aprendido

1. **BeanOutputParser**: Cómo obtener datos estructurados desde un LLM
2. **Integración de IA**: Cómo conectar Spring Boot con Ollama
3. **Persistencia con JPA**: Cómo guardar datos clasificados por IA
4. **Arquitectura en capas**: Separación de responsabilidades
5. **Prompt Engineering**: Cómo escribir prompts efectivos
6. **Interfaz web completa**: CRUD completo con Thymeleaf

### Ventajas de este Enfoque

1. **100% Local**: Sin costos, sin límites de tasa, privacidad total
2. **Type-Safe**: Objetos Java en lugar de strings
3. **Mantenible**: Arquitectura clara y modular
4. **Escalable**: Fácil agregar nuevas categorías o campos
5. **Educativo**: Código comentado y estructura clara

### Limitaciones Actuales

1. **Sin manejo robusto de errores**: Confía en respuestas perfectas
2. **Sin autenticación**: Cualquiera puede crear/modificar tickets
3. **Sin pruebas unitarias**: Falta cobertura de testing
4. **Sin validación avanzada**: No valida que la IA clasifique correctamente
5. **Sin monitoreo**: No rastrea precisión de clasificaciones

### Próximos Pasos Recomendados

1. **Agregar validación de respuestas IA**
   ```java
   @Service
   public class ValidadorCategoria {
       public boolean esValido(TicketCategory categoria) {
           // Validar que todos los campos estén en listas permitidas
       }
   }
   ```

2. **Implementar sistema de feedback**
   ```java
   @Column
   private Boolean clasificacionCorrecta;

   @PostMapping("/tickets/{id}/feedback")
   public String marcarComoIncorrecto(@PathVariable Long id) {
       // Permitir al usuario indicar si la clasificación fue correcta
   }
   ```

3. **Agregar métricas y monitoreo**
   ```java
   @Aspect
   public class MetricasIA {
       @Around("execution(* clasificarTicket(..))")
       public Object medirTiempo(ProceedingJoinPoint pjp) {
           long inicio = System.currentTimeMillis();
           Object result = pjp.proceed();
           long duracion = System.currentTimeMillis() - inicio;
           log.info("Clasificación tomó {} ms", duracion);
           return result;
       }
   }
   ```

4. **Implementar asignación automática**
   ```java
   @Entity
   public class Tecnico {
       private String nombre;
       private List<String> especialidades;
   }

   public Tecnico asignarAutomaticamente(Ticket ticket) {
       return tecnicoRepository.findByEspecialidad(ticket.getCategoria());
   }
   ```

5. **Agregar búsqueda semántica**
   ```java
   public List<Ticket> buscarSimilares(Long ticketId) {
       // Usar embeddings para encontrar tickets similares
   }
   ```

### Recursos Adicionales

- **Documentación Spring AI**: https://docs.spring.io/spring-ai/reference/
- **Ollama Models**: https://ollama.com/library
- **Spring Data JPA**: https://spring.io/projects/spring-data-jpa
- **Thymeleaf**: https://www.thymeleaf.org/documentation.html

### Palabras Finales

Este proyecto demuestra cómo la inteligencia artificial puede integrarse naturalmente en aplicaciones empresariales. No necesitas APIs externas costosas ni arquitecturas complejas: con Spring AI y Ollama puedes construir soluciones potentes que se ejecutan completamente en tu infraestructura.

El patrón de BeanOutputParser es especialmente valioso porque transforma la IA de un "generador de texto" a un "proveedor de datos estructurados", permitiendo que tus aplicaciones tomen decisiones automáticas basadas en análisis de lenguaje natural.

Experimenta cambiando los prompts, agregando nuevas categorías, o integrando otros modelos de Ollama. El código está diseñado para ser extensible y educativo.

---

Tutorial creado para el proyecto Asistente de Clasificación de Tickets.
Versión: 1.0 - Fecha: 2026-02-03
