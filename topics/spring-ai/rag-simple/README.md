# Chat con tus Documentos - RAG Simple

Proyecto de nivel avanzado para aprender **RAG (Retrieval Augmented Generation)**, el caso de uso más demandado en la industria de IA actualmente.

## ¿Qué es RAG?

RAG es una técnica que permite a la IA responder preguntas basándose en tus propios documentos, en lugar de solo su conocimiento pre-entrenado. Es como darle a la IA una biblioteca personal de información.

## Características

- **Carga de Documentos**: Sube archivos .txt con información
- **Embeddings Automáticos**: Convierte texto a vectores usando Ollama
- **Vector Store**: Almacena embeddings en SimpleVectorStore (memoria)
- **Búsqueda Semántica**: Encuentra información relevante por significado, no solo palabras clave
- **Contexto Preciso**: La IA responde SOLO con información de tus documentos
- **100% Local**: Todo se procesa en tu computadora

## Tecnologías

- Spring Boot 4.0.2
- Spring AI 2.0.0-M2
- Ollama (llama3.1 para chat, nomic-embed-text para embeddings)
- SimpleVectorStore
- Thymeleaf
- Java 21

## Conceptos de Spring AI Aprendidos

### 1. Embeddings

Los embeddings son representaciones numéricas (vectores) de texto que capturan el significado semántico.

```java
// Ollama crea embeddings automáticamente
EmbeddingModel embeddingModel; // Inyectado por Spring AI
List<Double> vector = embeddingModel.embed("Hola mundo");
// Resultado: [0.234, -0.567, 0.123, ...] (768 dimensiones)
```

**Ventaja**: Textos con significado similar tienen vectores cercanos, aunque usen palabras diferentes.

### 2. Document y TextSplitter

Spring AI tiene clases para manejar documentos:

```java
// Leer documento
TextReader reader = new TextReader("archivo.txt");
List<Document> docs = reader.get();

// Dividir en fragmentos pequeños (chunks)
TextSplitter splitter = new TokenTextSplitter();
List<Document> chunks = splitter.apply(docs);
// Un doc grande → Muchos fragmentos pequeños
```

**¿Por qué dividir?**: Los modelos tienen límite de tokens. Fragmentos pequeños son más fáciles de buscar y procesar.

### 3. VectorStore

Es una base de datos especializada en buscar vectores similares.

```java
// Guardar documentos (se crean embeddings automáticamente)
vectorStore.add(chunks);

// Buscar documentos similares a una pregunta
List<Document> similares = vectorStore.similaritySearch(
    SearchRequest.query("¿Cómo solicito vacaciones?")
               .withTopK(3)  // Top 3 más relevantes
);
```

**SimpleVectorStore**: Implementación en memoria de Spring AI. Perfecto para aprender, pero los datos se pierden al reiniciar.

### 4. Flujo Completo de RAG

```
1. Usuario sube documento.txt
   ↓
2. TextSplitter divide en fragmentos
   ↓
3. EmbeddingModel crea vectores para cada fragmento
   ↓
4. VectorStore guarda fragmentos + vectores
   ↓
5. Usuario pregunta: "¿Cuál es el procedimiento?"
   ↓
6. VectorStore busca fragmentos similares
   ↓
7. ChatClient recibe: pregunta + contexto relevante
   ↓
8. IA responde usando SOLO el contexto
```

## Limitaciones de SimpleVectorStore

- **En memoria**: Los documentos se pierden al reiniciar
- **No persistente**: No hay archivo de base de datos
- **Perfecto para aprender**: Fácil de usar, sin configuración

Para producción, considera:
- **Chroma**: Vector DB con persistencia
- **Pinecone**: Vector DB en la nube
- **Weaviate**: Vector DB open source

## Experimentos para Aprender

1. **Múltiples Documentos**: Sube varios archivos .txt sobre diferentes temas
2. **TopK**: Cambia `withTopK(3)` a `withTopK(5)` para más contexto
3. **Chunk Size**: Modifica el TokenTextSplitter para chunks más grandes/pequeños
4. **Temperatura**: Cambia la temperatura del modelo en application.properties

## Próximos Pasos

- Persistir vectores en disco (Chroma DB)
- Agregar soporte para PDF y DOCX
- Implementar historial de conversación
- Crear API REST para integración
- Agregar metadatos a los documentos (fecha, autor, etc.)
- Implementar re-ranking de resultados

## Caso de Uso Real

Este patrón se usa en:
- **Chatbots corporativos**: Responden sobre manuales internos
- **Asistentes de código**: Buscan en documentación técnica
- **Soporte al cliente**: Consultan base de conocimiento
- **Investigación**: Analizan papers científicos
