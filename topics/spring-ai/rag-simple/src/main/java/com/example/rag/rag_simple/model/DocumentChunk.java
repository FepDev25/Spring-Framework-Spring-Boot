package com.example.rag.rag_simple.model;

import java.util.List;

public class DocumentChunk {
    private String text;
    private List<Double> embedding;
    private String source;

    public DocumentChunk(String text, List<Double> embedding, String source) {
        this.text = text;
        this.embedding = embedding;
        this.source = source;
    }

    public String getText() {
        return text;
    }

    public List<Double> getEmbedding() {
        return embedding;
    }

    public String getSource() {
        return source;
    }

    public double cosineSimilarity(List<Double> otherEmbedding) {
        double dotProduct = 0.0;
        double normA = 0.0;
        double normB = 0.0;

        for (int i = 0; i < embedding.size(); i++) {
            dotProduct += embedding.get(i) * otherEmbedding.get(i);
            normA += embedding.get(i) * embedding.get(i);
            normB += otherEmbedding.get(i) * otherEmbedding.get(i);
        }

        return dotProduct / (Math.sqrt(normA) * Math.sqrt(normB));
    }
}
