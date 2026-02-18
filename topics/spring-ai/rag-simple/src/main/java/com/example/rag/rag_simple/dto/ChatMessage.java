package com.example.rag.rag_simple.dto;

public class ChatMessage {
    private String pregunta;
    private String respuesta;
    private String contexto;

    public ChatMessage() {
    }

    public ChatMessage(String pregunta, String respuesta, String contexto) {
        this.pregunta = pregunta;
        this.respuesta = respuesta;
        this.contexto = contexto;
    }

    public String getPregunta() {
        return pregunta;
    }

    public void setPregunta(String pregunta) {
        this.pregunta = pregunta;
    }

    public String getRespuesta() {
        return respuesta;
    }

    public void setRespuesta(String respuesta) {
        this.respuesta = respuesta;
    }

    public String getContexto() {
        return contexto;
    }

    public void setContexto(String contexto) {
        this.contexto = contexto;
    }
}
