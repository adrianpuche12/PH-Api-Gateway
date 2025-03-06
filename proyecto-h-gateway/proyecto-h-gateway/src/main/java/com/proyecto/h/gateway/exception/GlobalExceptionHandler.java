package com.proyecto.h.gateway.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.proyecto.h.gateway.model.ErrorResponse;

/**
 * Manejador global de excepciones para el API Gateway.
 * Captura las excepciones y devuelve respuestas de error formateadas uniformemente.
 */
@Component
@Order(-2) // Alta prioridad para capturar antes que otros manejadores
public class GlobalExceptionHandler implements ErrorWebExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {
        ServerHttpResponse response = exchange.getResponse();
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);

        // Determinar el código de estado HTTP apropiado basado en el tipo de excepción
        if (ex instanceof ResponseStatusException) {
            response.setStatusCode(((ResponseStatusException) ex).getStatusCode());
        } else if (ex.getMessage() != null && ex.getMessage().contains("404")) {
            // Si el mensaje contiene "404", asumimos que es un recurso no encontrado
            response.setStatusCode(HttpStatus.NOT_FOUND);
        } else {
            // Para cualquier otra excepción, usamos error interno del servidor
            response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        // Crear respuesta de error
        ErrorResponse errorResponse = new ErrorResponse(
                response.getStatusCode().value(),
                ex.getMessage(),
                exchange.getRequest().getPath().value()
        );

        // Log de la excepción para depuración
        logger.error("Error procesando solicitud: {}", ex.getMessage());

        DataBufferFactory bufferFactory = response.bufferFactory();
        try {
            String errorJson = objectMapper.writeValueAsString(errorResponse);
            return response.writeWith(Mono.just(bufferFactory.wrap(errorJson.getBytes())));
        } catch (JsonProcessingException e) {
            logger.error("Error al serializar respuesta de error", e);
            return Mono.error(e);
        }
    }
}