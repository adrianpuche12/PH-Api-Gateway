package com.proyecto.h.gateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
public class CorsConfig {

    @Bean
    public CorsWebFilter corsWebFilter() {
        final CorsConfiguration corsConfig = new CorsConfiguration();


        corsConfig.addAllowedOriginPattern("*");
        corsConfig.addAllowedHeader("expo-platform");
        corsConfig.addAllowedHeader("expo-version");

        // Métodos permitidos
        corsConfig.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS", "HEAD"));

        // Cabeceras permitidas
        corsConfig.setAllowedHeaders(Arrays.asList("Origin", "Content-Type", "Accept", "Authorization",
                "X-Requested-With", "Access-Control-Request-Method",
                "Access-Control-Request-Headers"));

        // Cabeceras expuestas al cliente
        corsConfig.setExposedHeaders(Arrays.asList("Access-Control-Allow-Origin",
                "Access-Control-Allow-Credentials",
                "Authorization"));

         corsConfig.setAllowCredentials(true);

        corsConfig.setMaxAge(3600L);

        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfig);

        return new CorsWebFilter(source);
    }
}
