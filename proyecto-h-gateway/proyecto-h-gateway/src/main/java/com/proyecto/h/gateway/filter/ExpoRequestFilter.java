package com.proyecto.h.gateway.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

/**
 * Filtro para detectar y manejar solicitudes de aplicaciones Expo Native.
 * Esta versión usa WebFilter en lugar de GlobalFilter para mayor compatibilidad.
 */
@Component
public class ExpoRequestFilter implements WebFilter {

    private static final Logger logger = LoggerFactory.getLogger(ExpoRequestFilter.class);

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        String userAgent = exchange.getRequest().getHeaders().getFirst("User-Agent");
        boolean isExpoRequest = userAgent != null &&
                (userAgent.contains("Expo") || userAgent.contains("ExpoGo"));

        if (isExpoRequest) {
            logger.debug("Solicitud desde aplicación Expo detectada");

            // Crear una nueva instancia de request con headers adicionales
            var modifiedRequest = exchange.getRequest().mutate()
                    .header("X-App-Type", "Expo-Native")
                    .build();

            // Reemplazar la solicitud original con la modificada
            var modifiedExchange = exchange.mutate().request(modifiedRequest).build();
            return chain.filter(modifiedExchange);
        }

        return chain.filter(exchange);
    }
}