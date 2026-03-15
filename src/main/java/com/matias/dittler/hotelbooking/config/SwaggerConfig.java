package com.matias.dittler.hotelbooking.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

/**
 * Configuración de Swagger / OpenAPI para la documentación automática de la API.
 *
 * Esta clase define el bean principal de OpenAPI utilizado por Swagger UI
 * para generar una documentación interactiva de los endpoints disponibles
 * en la aplicación.
 *
 * La documentación permite:
 * - Visualizar los endpoints expuestos por la API.
 * - Consultar los parámetros y modelos de datos utilizados.
 * - Probar los endpoints directamente desde la interfaz de Swagger.
 *
 * La configuración se habilita únicamente para los perfiles:
 * - local: entorno de desarrollo.
 * - prod: entorno de producción desplegado.
 *
 * Esto permite que la documentación esté disponible tanto durante el
 * desarrollo como en el entorno desplegado para facilitar pruebas
 * e integración con clientes.
 */
@Configuration
@Profile({"local", "prod"})
public class SwaggerConfig {

    /**
     * Bean que configura la información principal de la documentación OpenAPI.
     *
     * Define los metadatos de la API que se mostrarán en Swagger UI,
     * incluyendo título, descripción y versión del servicio.
     *
     * @return instancia de OpenAPI con la información básica de la API
     */
    @Bean
    public OpenAPI hotelBookingOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Hotel Booking API")
                        .description("Documentación de la API para el sistema de reservas de habitación de un hotel.")
                        .version("v1.0"));
    }
}