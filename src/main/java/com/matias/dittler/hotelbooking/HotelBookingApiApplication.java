package com.matias.dittler.hotelbooking;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Clase principal de la aplicación Hotel Booking API.
 * 
 * @SpringBootApplication:
 * - Marca esta clase como configuración principal de Spring Boot.
 * - Habilita el escaneo de componentes (@ComponentScan) y configuración automática (@EnableAutoConfiguration).
 */
@SpringBootApplication
public class HotelBookingApiApplication {
    /**
     * Método main que arranca la aplicación.
     * Spring Boot se encarga de inicializar el contexto, cargar beans y levantar el servidor embebido.
     */
	public static void main(String[] args) {
		SpringApplication.run(HotelBookingApiApplication.class, args);
	}
}
