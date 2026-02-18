package com.example.rag.rag_simple.service;

import com.example.rag.rag_simple.model.DocumentChunk;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.embedding.EmbeddingResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class RagService {

    private static final Logger log = LoggerFactory.getLogger(RagService.class);
    private static final int CHUNK_SIZE = 500;

    private final EmbeddingModel embeddingModel;
    private final ChatClient chatClient;
    private final List<DocumentChunk> documentChunks = new ArrayList<>();

    public RagService(EmbeddingModel embeddingModel, ChatClient.Builder chatClientBuilder) {
        this.embeddingModel = embeddingModel;
        this.chatClient = chatClientBuilder.build();
    }

    public String cargarDocumento(MultipartFile file) throws IOException {
        Path tempFile = Files.createTempFile("upload-", ".txt");
        Files.copy(file.getInputStream(), tempFile, StandardCopyOption.REPLACE_EXISTING);

        log.info("Procesando documento: {}", file.getOriginalFilename());

        String content = Files.readString(tempFile, StandardCharsets.UTF_8);
        List<String> chunks = dividirEnFragmentos(content);

        log.info("Documento dividido en {} fragmentos", chunks.size());

        int processedChunks = 0;
        for (String chunk : chunks) {
            EmbeddingResponse embeddingResponse = embeddingModel.embedForResponse(List.of(chunk));
            float[] embeddingArray = embeddingResponse.getResults().get(0).getOutput();
            List<Double> embedding = convertToDoubleList(embeddingArray);

            documentChunks.add(new DocumentChunk(chunk, embedding, file.getOriginalFilename()));
            processedChunks++;
        }

        Files.delete(tempFile);

        log.info("{} fragmentos procesados e indexados", processedChunks);

        return String.format("Documento '%s' cargado exitosamente. %d fragmentos indexados.",
                file.getOriginalFilename(), processedChunks);
    }

    private List<String> dividirEnFragmentos(String text) {
        List<String> chunks = new ArrayList<>();
        String[] paragraphs = text.split("\n\n+");

        StringBuilder currentChunk = new StringBuilder();
        for (String paragraph : paragraphs) {
            if (currentChunk.length() + paragraph.length() > CHUNK_SIZE) {
                if (currentChunk.length() > 0) {
                    chunks.add(currentChunk.toString().trim());
                    currentChunk = new StringBuilder();
                }
            }
            currentChunk.append(paragraph).append("\n\n");
        }

        if (currentChunk.length() > 0) {
            chunks.add(currentChunk.toString().trim());
        }

        return chunks;
    }

    public String preguntarSobreDocumentos(String pregunta) {
        if (documentChunks.isEmpty()) {
            return "No hay documentos cargados. Por favor, sube un documento primero.";
        }

        log.info("Buscando contexto relevante para: {}", pregunta);

        EmbeddingResponse questionEmbeddingResponse = embeddingModel.embedForResponse(List.of(pregunta));
        float[] questionEmbeddingArray = questionEmbeddingResponse.getResults().get(0).getOutput();
        List<Double> questionEmbedding = convertToDoubleList(questionEmbeddingArray);

        List<DocumentChunk> similarChunks = documentChunks.stream()
                .sorted(Comparator.comparingDouble((DocumentChunk chunk) ->
                        chunk.cosineSimilarity(questionEmbedding)).reversed())
                .limit(3)
                .collect(Collectors.toList());

        if (similarChunks.isEmpty()) {
            return "No se encontró información relevante en los documentos cargados.";
        }

        String contexto = similarChunks.stream()
                .map(DocumentChunk::getText)
                .collect(Collectors.joining("\n\n---\n\n"));

        log.info("Contexto recuperado: {} caracteres de {} fragmentos",
                contexto.length(), similarChunks.size());

        String promptTemplate = """
                Eres un asistente experto que responde preguntas basándose ÚNICAMENTE en el contexto proporcionado.

                Contexto de los documentos:
                {contexto}

                Pregunta del usuario: {pregunta}

                Instrucciones:
                - Responde la pregunta usando SOLO la información del contexto
                - Si la información no está en el contexto, di "No encuentro esa información en los documentos"
                - Sé claro y conciso
                - No inventes información
                """;

        PromptTemplate template = new PromptTemplate(promptTemplate);
        var prompt = template.create(Map.of(
                "contexto", contexto,
                "pregunta", pregunta
        ));

        return chatClient.prompt(prompt)
                .call()
                .content();
    }

    public int getDocumentCount() {
        return documentChunks.stream()
                .map(DocumentChunk::getSource)
                .distinct()
                .collect(Collectors.toList())
                .size();
    }

    public void limpiarDocumentos() {
        documentChunks.clear();
    }

    private List<Double> convertToDoubleList(float[] array) {
        List<Double> list = new ArrayList<>();
        for (float value : array) {
            list.add((double) value);
        }
        return list;
    }
}
