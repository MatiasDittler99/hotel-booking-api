package com.matias.dittler.hotelbooking.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

import com.matias.dittler.hotelbooking.service.R2StorageService;
import com.matias.dittler.hotelbooking.service.implementation.BookingService;
import com.matias.dittler.hotelbooking.service.implementation.RoomService;
import com.matias.dittler.hotelbooking.utils.JWTUtils;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * 🔹 HealthControllerTest
 * ---------------------------------------------------------------------------
 * Clase de tests enfocada en el endpoint de salud de la API ("/").
 * Responsabilidades:
 * - Verificar que el endpoint de health check responda correctamente.
 * - Usar @SpringBootTest para levantar el contexto completo de Spring.
 * - Deshabilitar filtros de seguridad con @AutoConfigureMockMvc(addFilters = false)
 *   para no depender de JWT u otros filtros de seguridad en este test.
 */
@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
class HealthControllerTest {

    /**
     * 🔹 MockMvc
     * -----------------------------------------------------------------------
     * Permite simular peticiones HTTP a los endpoints sin levantar un servidor real.
     */
    @Autowired
    private MockMvc mockMvc;

    /**
     * 🔹 MockBean: JWTUtils
     * -----------------------------------------------------------------------
     * Mockeamos la utilidad JWT para evitar validaciones de token en este test,
     * ya que el endpoint de health check no requiere autenticación.
     */
    @MockBean
    private JWTUtils jwtUtils;

    /**
     * 🔹 MockBean: BookingService
     * -----------------------------------------------------------------------
     * Mockeamos BookingService para evitar llamadas reales a la lógica de reservas.
     */
    @MockBean
    private BookingService bookingService;

    /**
     * 🔹 MockBean: RoomService
     * -----------------------------------------------------------------------
     * Mockeamos RoomService para evitar llamadas reales a la lógica de habitaciones.
     */
    @MockBean
    private RoomService roomService;

    /**
     * 🔹 MockBean: R2StorageService
     * -----------------------------------------------------------------------
     * Mockeamos el servicio de almacenamiento para no depender de recursos externos.
     */
    @MockBean
    private R2StorageService r2StorageService;

    /**
     * 🔹 Test: Health check endpoint
     * -----------------------------------------------------------------------
     * Verifica que el endpoint "/" responda correctamente con:
     * - HTTP status 200 OK
     * - JSON con clave "status" = "OK"
     * - JSON con clave "service" = "Hotel Booking API"
     * 
     * Esto asegura que la API está funcionando y lista para recibir requests.
     */
    @Test
    void deberiaResponderEstadoOK() throws Exception {
        mockMvc.perform(get("/"))
               .andExpect(status().isOk()) // HTTP 200 OK
               .andExpect(jsonPath("$.status").value("OK")) // Clave status correcta
               .andExpect(jsonPath("$.service").value("Hotel Booking API")); // Nombre del servicio
    }
}