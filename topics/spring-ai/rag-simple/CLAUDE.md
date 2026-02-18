# 3. Nivel Avanzado: "Chat con tus Documentos (RAG Simple)"

- Este es el caso de uso más demandado en la industria actual (Retrieval Augmented Generation).

- Concepto: Subes un archivo de texto (ej. un manual de instrucciones) y le haces preguntas al chat sobre ese documento específico.

- Lo que se debe aplicar:

    - Embeddings: Usar Ollama para convertir texto a vectores numéricos.
    - SimpleVectorStore: Spring AI trae una base de datos vectorial en memoria simple. Aprenderás a guardar la información del documento allí.
    - Contexto: Cómo recuperar la información relevante del archivo y pasársela a Ollama antes de que responda la pregunta del usuario.