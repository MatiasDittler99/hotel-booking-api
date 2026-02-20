package com.matias.dittler.hotelbooking.controller;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Pruebas de integración para el controlador de verificación de estado (HealthController).
 *
 * Este test levanta el contexto completo de Spring Boot para simular el comportamiento real
 * de la aplicación en ejecución. Se utiliza MockMvc para realizar peticiones HTTP sin necesidad
 * de iniciar un servidor embebido.
 *
 * La configuración de seguridad se desactiva explícitamente para evitar que filtros de autenticación
 * interfieran con la prueba del endpoint público de salud del sistema.
 */
@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@TestPropertySource(properties = {
        // Se excluye la auto-configuración de seguridad para simplificar el contexto de pruebas
        "spring.autoconfigure.exclude=" +
        "org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration," +
        "org.springframework.boot.autoconfigure.security.servlet.SecurityFilterAutoConfiguration"
})
class HealthControllerTest {

    /**
     * MockMvc permite simular peticiones HTTP y validar respuestas del controlador
     * sin desplegar la aplicación en un servidor real.
     */
    @Autowired
    private MockMvc mockMvc;

    /**
     * Verifica que el endpoint raíz responda correctamente con estado HTTP 200
     * y devuelva la estructura JSON esperada.
     *
     * Escenario validado:
     * - El servicio está activo
     * - La API responde correctamente
     * - El formato de respuesta es consistente
     */
    @Test
    void deberiaResponderEstadoOK() throws Exception {

        // Se ejecuta una petición GET al endpoint raíz "/"
        mockMvc.perform(get("/"))

                // Se valida que la respuesta HTTP sea 200 OK
                .andExpect(status().isOk())

                // Se valida el contenido del JSON devuelto por el controlador
                .andExpect(jsonPath("$.status").value("OK"))
                .andExpect(jsonPath("$.service").value("Hotel Booking API"));
    }
}
