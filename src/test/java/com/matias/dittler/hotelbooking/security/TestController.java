package com.matias.dittler.hotelbooking.security;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controlador de prueba para tests de seguridad.
 * 
 * Proporciona endpoints simples para verificar que SecurityConfig permita
 * o bloquee accesos según la configuración.
 * 
 * Todos devuelven un String fijo para simplificar los tests.
 */
@RestController
class TestController {

    /**
     * Endpoint de prueba bajo /auth/**
     * Simula un endpoint público accesible sin token.
     */
    @GetMapping("/auth/test")
    public String authEndpoint() {
        return "auth ok";
    }

    /**
     * Endpoint de prueba bajo /rooms/**
     * Simula un endpoint público accesible sin token.
     */
    @GetMapping("/rooms/test")
    public String roomsEndpoint() {
        return "rooms ok";
    }

    /**
     * Endpoint de prueba bajo /bookings/**
     * Simula un endpoint público accesible sin token.
     */
    @GetMapping("/bookings/test")
    public String bookingsEndpoint() {
        return "bookings ok";
    }

    /**
     * Endpoint de prueba protegido
     * Se usa para verificar la seguridad de endpoints que deberían
     * requerir autenticación o permisos específicos.
     */
    @GetMapping("/protected/test")
    public String protectedEndpoint() {
        return "protected ok";
    }
}

