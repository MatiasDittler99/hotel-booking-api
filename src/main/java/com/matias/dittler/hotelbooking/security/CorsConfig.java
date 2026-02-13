package com.matias.dittler.hotelbooking.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig {

    @Bean
    public WebMvcConfigurer webMvcConfigurer(){
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                        .allowedOrigins("*")
                        // Ejemplo de como iria despues para que funcione con los cors configurados
                        // .allowedOrigins(
                        //     "https://hotel-booking-frontend.vercel.app",
                        //     "https://hotel-booking.netlify.app"
                        // )
                        .allowedHeaders("*");
            }
        };
    }
}
