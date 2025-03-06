package com.proyecto.h.gateway.model;

import java.time.LocalDateTime;

/**
 * Modelo para respuestas de error uniformes.
 */
public class ErrorResponse {
    private int status;           // Cambiado a tipo primitivo int
    private String message;
    private String path;
    private LocalDateTime timestamp;

    // Constructor sin argumentos requerido para deserialización JSON
    public ErrorResponse() {
        this.timestamp = LocalDateTime.now();
    }

    // Constructor con los tres parámetros necesarios
    public ErrorResponse(int status, String message, String path) {
        this();  // Llama al constructor sin argumentos para inicializar timestamp
        this.status = status;
        this.message = message;
        this.path = path;
    }

    // Getters y Setters
    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}