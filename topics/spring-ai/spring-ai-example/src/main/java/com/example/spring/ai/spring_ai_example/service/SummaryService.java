package com.example.spring.ai.spring_ai_example.service;

import java.util.Map;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.stereotype.Service;

@Service
public class SummaryService {

    private final ChatClient chatClient;

    public SummaryService(ChatClient.Builder chatClientBuilder) {
        this.chatClient = chatClientBuilder.build();
    }

    public String generateSummary(String text) {
        String promptTemplate = """
                Actúa como un editor experto y resume el siguiente texto en exactamente 3 viñetas.
                Cada viñeta debe ser clara, concisa y capturar los puntos más importantes.

                Texto a resumir:
                {text}

                IMPORTANTE: Responde SOLO en texto plano. NO uses markdown, NO uses asteriscos (**),
                NO uses negritas, NO uses formato especial. Solo escribe las 3 viñetas con texto simple.
                Proporciona solo las 3 viñetas, sin introducción ni conclusión adicional.
                """;

        PromptTemplate template = new PromptTemplate(promptTemplate);

        // Crear el prompt con el texto proporcionado
        var prompt = template.create(Map.of("text", text));

        return chatClient.prompt(prompt) // chatclient preparar el mensaje
                .call() // enviar el mensaje al modelo de lenguaje
                .content(); // retornar solo el contenido de la respuesta
    }

    public String analyzeSentiment(String text) {
        String promptTemplate = """
                Actúa como un experto en análisis de sentimientos.
                Analiza el siguiente texto y determina:
                1. El sentimiento general (positivo, negativo o neutral)
                2. El nivel de intensidad emocional (bajo, medio, alto)
                3. Las emociones principales detectadas

                Texto a analizar:
                {text}

                IMPORTANTE: Responde SOLO en texto plano. NO uses markdown, NO uses asteriscos (**),
                NO uses negritas, NO uses formato especial. Solo escribe texto simple y claro.
                Proporciona un análisis conciso y estructurado.
                """;

        PromptTemplate template = new PromptTemplate(promptTemplate);
        var prompt = template.create(Map.of("text", text));

        return chatClient.prompt(prompt)
                .call()
                .content();
    }

    public String translateToFrench(String text) {
        String promptTemplate = """
                Actúa como un traductor profesional.
                Traduce el siguiente texto al francés manteniendo el significado y el tono original.

                Texto a traducir:
                {text}

                IMPORTANTE: Responde SOLO en texto plano. NO uses markdown, NO uses asteriscos (**),
                NO uses negritas, NO uses formato especial. Solo escribe el texto traducido en francés.
                """;

        PromptTemplate template = new PromptTemplate(promptTemplate);
        var prompt = template.create(Map.of("text", text));

        return chatClient.prompt(prompt)
                .call()
                .content();
    }
}
