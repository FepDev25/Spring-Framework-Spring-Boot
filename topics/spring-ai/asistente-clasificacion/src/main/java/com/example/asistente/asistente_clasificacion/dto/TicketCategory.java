package com.example.asistente.asistente_clasificacion.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TicketCategory {

    // las anotaciones ayudan al parser JSON a mapear los campos correctamente
    @JsonProperty("categoria")
    private String categoria;

    @JsonProperty("prioridad")
    private String prioridad;

    @JsonProperty("departamento")
    private String departamento;

    @JsonProperty("resumen")
    private String resumen;

    public TicketCategory() {
    }

    public TicketCategory(String categoria, String prioridad, String departamento, String resumen) {
        this.categoria = categoria;
        this.prioridad = prioridad;
        this.departamento = departamento;
        this.resumen = resumen;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getPrioridad() {
        return prioridad;
    }

    public void setPrioridad(String prioridad) {
        this.prioridad = prioridad;
    }

    public String getDepartamento() {
        return departamento;
    }

    public void setDepartamento(String departamento) {
        this.departamento = departamento;
    }

    public String getResumen() {
        return resumen;
    }

    public void setResumen(String resumen) {
        this.resumen = resumen;
    }

    @Override
    public String toString() {
        return "TicketCategory{" +
                "categoria='" + categoria + '\'' +
                ", prioridad='" + prioridad + '\'' +
                ", departamento='" + departamento + '\'' +
                ", resumen='" + resumen + '\'' +
                '}';
    }
}
