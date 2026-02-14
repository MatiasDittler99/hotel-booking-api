package com.matias.dittler.hotelbooking.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("local")  // Solo se activa en perfil local
public class SwaggerConfig {

    @Bean
    public OpenAPI hotelBookingOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Hotel Booking API")
                        .description("Documentación de la API para hotel booking")
                        .version("v1.0"));
    }
}
