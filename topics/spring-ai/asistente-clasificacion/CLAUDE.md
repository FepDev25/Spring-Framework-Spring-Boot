# 2. Nivel Intermedio: "Asistente de Clasificación (Output Parsers)"

- Aquí el reto es que la IA no devuelva "texto libre", sino datos estructurados que el código Java pueda usar.

- Concepto: Un sistema de tickets de soporte o feedback. El usuario escribe un problema (ej. "Mi internet está lento"), y la IA debe categorizarlo automáticamente (ej. Categoría: RED, Prioridad: ALTA) y devolver un objeto Java.

- Lo que se debe aplicar:

    - BeanOutputParser: La característica más potente para desarrolladores. Obligar a Ollama a responder en formato JSON para que Spring lo convierta automáticamente a una clase Java (TicketCategory).
    - Lógica de Negocio: Usar la respuesta de la IA para guardar datos en una base de datos o mostrarlos en una tabla de Thymeleaf con colores según la prioridad (usemos sqlite).
