package com.matias.dittler.hotelbooking.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Configuración de CORS para la aplicación.
 *
 * Permite que el frontend pueda hacer peticiones HTTP a este backend
 * desde otros dominios sin ser bloqueado por el navegador.
 */
@Configuration
public class CorsConfig {

    /**
     * Bean que configura las reglas de CORS de la aplicación.
     *
     * @return WebMvcConfigurer con las configuraciones de CORS
     */
    @Bean
    public WebMvcConfigurer webMvcConfigurer(){
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**") // Aplica CORS a todos los endpoints
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // Métodos HTTP permitidos
                        .allowedOrigins("*") // Permite cualquier origen (desarrollo). En producción se recomienda especificar dominios
                        // Ejemplo de cómo usar dominios específicos en producción:
                        // .allowedOrigins(
                        //     "https://hotel-booking-frontend.vercel.app",
                        //     "https://hotel-booking.netlify.app"
                        // )
                        .allowedHeaders("*"); // Permite cualquier cabecera HTTP
            }
        };
    }
}
