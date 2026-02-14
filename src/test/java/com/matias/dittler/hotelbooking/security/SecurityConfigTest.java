package com.matias.dittler.hotelbooking.security;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import com.matias.dittler.hotelbooking.service.CustomUserDetailsService;
import com.matias.dittler.hotelbooking.utils.JWTUtils;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Clase de tests para verificar la configuración de seguridad (SecurityConfig) de la aplicación.
 * 
 * Utiliza SpringBootTest para levantar un contexto completo y AutoConfigureMockMvc para
 * poder realizar peticiones HTTP simuladas a los endpoints.
 * Se importa TestController explícitamente para que esté disponible durante los tests.
 */
@SpringBootTest // Levanta el contexto completo de Spring Boot para pruebas de integración
@AutoConfigureMockMvc // Configura automáticamente MockMvc para simular peticiones HTTP
@Import(TestController.class) // Se importa el controlador de prueba con endpoints simulados
class SecurityConfigTest {

    @Autowired
    private MockMvc mockMvc; // MockMvc permite simular peticiones HTTP sin levantar un servidor real

    @MockBean
    private CustomUserDetailsService userDetailsService; 
    // Se mockea el UserDetailsService para que Spring no intente cargar usuarios reales

    @MockBean
    private JWTUtils jwtUtils;
    // Se mockea JWTUtils para evitar validación real de tokens JWT en los tests

    /**
     * Verifica que los endpoints bajo /auth/** sean accesibles sin autenticación.
     */
    @Test
    @DisplayName("Should allow /auth/** without authentication")
    void shouldAllowAuthEndpoints() throws Exception {
        mockMvc.perform(get("/auth/test")) // Simula una petición GET a /auth/test
                .andExpect(status().isOk()); // Espera que devuelva HTTP 200 OK
    }

    /**
     * Verifica que los endpoints bajo /rooms/** sean accesibles sin autenticación.
     */
    @Test
    @DisplayName("Should allow /rooms/** without authentication")
    void shouldAllowRoomsEndpoints() throws Exception {
        mockMvc.perform(get("/rooms/test")) // Simula GET a /rooms/test
                .andExpect(status().isOk()); // Debe devolver 200 OK
    }

    /**
     * Verifica que los endpoints bajo /bookings/** sean accesibles sin autenticación.
     */
    @Test
    @DisplayName("Should allow /bookings/** without authentication")
    void shouldAllowBookingsEndpoints() throws Exception {
        mockMvc.perform(get("/bookings/test")) // Simula GET a /bookings/test
                .andExpect(status().isOk()); // Debe devolver 200 OK
    }

    /**
     * Verifica que los endpoints protegidos requieran autenticación.
     * Según la configuración actual, devuelve 403 Forbidden si no hay autenticación.
     */
    @Test
    @DisplayName("Should block protected endpoints without authentication")
    void shouldBlockProtectedEndpoints() throws Exception {
        mockMvc.perform(get("/protected/test")) // Simula GET a /protected/test
                .andExpect(status().isForbidden()); // Se espera HTTP 403 Forbidden
    }
}
