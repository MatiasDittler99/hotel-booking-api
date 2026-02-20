package com.matias.dittler.hotelbooking.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * Controlador REST encargado de exponer un endpoint de verificación de estado (health check).
 *
 * Este endpoint permite comprobar rápidamente si la API está funcionando correctamente.
 * Es especialmente útil para:
 * - Monitoreo del servicio en producción
 * - Verificación automática en plataformas de despliegue (ej: Render)
 * - Pruebas de conectividad
 */
@RestController
public class HealthController {

    /**
     * Endpoint raíz que devuelve el estado del servicio en formato JSON.
     *
     * Método HTTP: GET
     * Ruta: "/"
     *
     * @return un mapa con información básica del estado del servicio
     *         {
     *           "status": "OK",
     *           "service": "Hotel Booking API"
     *         }
     */
    @GetMapping("/")
    public Map<String, String> health() {

        // Se utiliza Map.of para crear un JSON simple e inmutable
        // que indica que el servicio está activo y funcionando.
        return Map.of(
                "status", "OK",
                "service", "Hotel Booking API"
        );
    }
}
