package com.matias.dittler.hotelbooking.security;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import com.matias.dittler.hotelbooking.service.CustomUserDetailsService;
import com.matias.dittler.hotelbooking.utils.JWTUtils;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * 🔹 SecurityConfigTest
 * ---------------------------------------------------------------------------
 * Clase de tests enfocada en la seguridad de los endpoints de la aplicación.
 * Responsabilidades:
 * - Validar que los endpoints públicos y protegidos se comporten según roles.
 * - Usar mocks de servicios para no depender del contexto completo de Spring.
 * - Probar autenticación y autorización usando @WithMockUser y MockMvc.
 */
@WebMvcTest(TestController.class) // Levanta solo el contexto MVC, no toda la app
class SecurityConfigTest {

    /**
     * 🔹 MockMvc
     * -----------------------------------------------------------------------
     * Componente proporcionado por Spring para simular requests HTTP
     * sin necesidad de levantar un servidor real.
     */
    @Autowired
    private MockMvc mockMvc;

    /**
     * 🔹 MockBean: CustomUserDetailsService
     * -----------------------------------------------------------------------
     * Se mockea el servicio de detalles de usuario para evitar llamadas
     * reales a la base de datos durante los tests.
     */
    @MockBean
    private CustomUserDetailsService userDetailsService;

    /**
     * 🔹 MockBean: JWTUtils
     * -----------------------------------------------------------------------
     * Se mockea la utilidad de JWT para que la seguridad se pueda probar
     * sin generar tokens reales.
     */
    @MockBean
    private JWTUtils jwtUtils;

    /**
     * 🔹 Test: Acceso a /auth/** con usuario simulado
     * -----------------------------------------------------------------------
     * Verifica que un usuario con rol USER pueda acceder a endpoints
     * de autenticación simulados.
     */
    @Test
    @DisplayName("Should allow /auth/** with mock user")
    @WithMockUser(username = "user", roles = {"USER"})
    void shouldAllowAuthEndpoints() throws Exception {
        mockMvc.perform(get("/auth/test"))
               .andExpect(status().isOk()); // 200 OK si el acceso es permitido
    }

    /**
     * 🔹 Test: Acceso a /rooms/** con usuario simulado
     * -----------------------------------------------------------------------
     * Verifica que un usuario con rol USER pueda acceder a endpoints
     * relacionados con habitaciones.
     */
    @Test
    @DisplayName("Should allow /rooms/** with mock user")
    @WithMockUser(username = "user", roles = {"USER"})
    void shouldAllowRoomsEndpoints() throws Exception {
        mockMvc.perform(get("/rooms/test"))
               .andExpect(status().isOk()); // 200 OK si el acceso es permitido
    }

    /**
     * 🔹 Test: Acceso a /bookings/** con usuario simulado
     * -----------------------------------------------------------------------
     * Verifica que un usuario con rol USER pueda acceder a endpoints
     * de reservas.
     */
    @Test
    @DisplayName("Should allow /bookings/** with mock user")
    @WithMockUser(username = "user", roles = {"USER"})
    void shouldAllowBookingsEndpoints() throws Exception {
        mockMvc.perform(get("/bookings/test"))
               .andExpect(status().isOk()); // 200 OK si el acceso es permitido
    }

    /**
     * 🔹 Test: Bloqueo de /protected/** sin autenticación
     * -----------------------------------------------------------------------
     * Verifica que un endpoint protegido devuelva 401 Unauthorized
     * si no se proporciona ningún usuario autenticado.
     */
    @Test
    @DisplayName("Should block /protected/** without authentication")
    void shouldBlockProtectedEndpoints() throws Exception {
        mockMvc.perform(get("/protected/test"))
               .andExpect(status().isUnauthorized()); // 401 para no autenticados
    }
}