package com.example.asistente.asistente_clasificacion.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

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

    public Ticket() {
        this.fechaCreacion = LocalDateTime.now();
        this.estado = "ABIERTO";
    }

    public Ticket(String descripcion, String categoria, String prioridad, String departamento, String resumen) {
        this.descripcion = descripcion;
        this.categoria = categoria;
        this.prioridad = prioridad;
        this.departamento = departamento;
        this.resumen = resumen;
        this.fechaCreacion = LocalDateTime.now();
        this.estado = "ABIERTO";
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
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

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getRespuestaSugerida() {
        return respuestaSugerida;
    }

    public void setRespuestaSugerida(String respuestaSugerida) {
        this.respuestaSugerida = respuestaSugerida;
    }
}
