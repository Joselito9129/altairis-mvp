package com.fdsa.altairis.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class AppConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")                          // Todos los endpoints
                .allowedOrigins("http://localhost:4200")    // Origen del frontend
                .allowedMethods("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS")
                .allowedHeaders("*")
                .exposedHeaders("Authorization")            // Si usas JWT o auth
                .allowCredentials(true)
                .maxAge(3600);                              // Cache preflight 1 hora
    }

}
