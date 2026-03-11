package com.matias.dittler.hotelbooking.security;

// Imports de dependencias necesarias para pruebas unitarias y mocks
import com.matias.dittler.hotelbooking.service.CustomUserDetailsService;
import com.matias.dittler.hotelbooking.utils.JWTUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Pruebas unitarias para JWTAuthFilter.
 * 
 * Propósito:
 * - Validar la lógica de autenticación basada en JWT.
 * - Comprobar el comportamiento del filtro en distintos escenarios:
 *      1. Sin header de autorización.
 *      2. Token válido.
 *      3. Token inválido.
 * 
 * Buenas prácticas aplicadas:
 * - Uso de Mockito para simular dependencias externas (JWTUtils, CustomUserDetailsService, HttpServletRequest/Response, FilterChain).
 * - Limpieza del SecurityContext antes de cada prueba para evitar contaminación entre tests.
 * - Tests claros, cortos y con un solo objetivo por método.
 */
class JWTAuthFilterTest {

    // Dependencias simuladas (mocks) para controlar el comportamiento del filtro
    @Mock
    private JWTUtils jwtUtils;

    @Mock
    private CustomUserDetailsService customUserDetailsService;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private FilterChain filterChain;

    // Objeto bajo prueba: JWTAuthFilter con las dependencias inyectadas
    @InjectMocks
    private JWTAuthFilter jwtAuthFilter;

    /**
     * Inicialización antes de cada test.
     * - Abre los mocks de Mockito.
     * - Limpia el contexto de seguridad de Spring para que cada test sea independiente.
     */
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        SecurityContextHolder.clearContext();
    }

    /**
     * Test: Cuando no hay header de Authorization.
     * 
     * Objetivo:
     * - Verificar que el filtro simplemente pasa al siguiente elemento de la cadena.
     * - No se establece autenticación en SecurityContext.
     */
    @Test
    void shouldPassFilterChainWhenNoAuthorizationHeader() throws Exception {
        when(request.getHeader("Authorization")).thenReturn(null);

        jwtAuthFilter.doFilterInternal(request, response, filterChain);

        // Verifica que filterChain.doFilter se haya llamado una vez
        verify(filterChain, times(1)).doFilter(request, response);

        // Asegura que no se haya creado ninguna autenticación
        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }

    /**
     * Test: Cuando se recibe un token válido.
     * 
     * Objetivo:
     * - Validar que el filtro extrae el usuario del token y lo autentica correctamente.
     * - Se verifica que el SecurityContext contenga la autenticación con el usuario correcto.
     */
    @Test
    void shouldAuthenticateWhenValidToken() throws Exception {
        String token = "valid.token.here";
        String email = "user@example.com";

        // Simula header válido con "Bearer "
        when(request.getHeader("Authorization")).thenReturn("Bearer " + token);

        // Simula datos del usuario
        UserDetails userDetails = new User(email, "password", Collections.emptyList());

        // Mockea comportamiento del JWTUtils y UserDetailsService
        when(jwtUtils.extractUsername(token)).thenReturn(email);
        when(customUserDetailsService.loadUserByUsername(email)).thenReturn(userDetails);
        when(jwtUtils.isValidToken(token, userDetails)).thenReturn(true);

        jwtAuthFilter.doFilterInternal(request, response, filterChain);

        // Verifica que la cadena de filtros se ejecute
        verify(filterChain, times(1)).doFilter(request, response);

        // Asegura que el SecurityContext tenga la autenticación establecida
        assertNotNull(SecurityContextHolder.getContext().getAuthentication());
        assertEquals(email, SecurityContextHolder.getContext().getAuthentication().getName());
    }

    /**
     * Test: Cuando el token es inválido.
     * 
     * Objetivo:
     * - Comprobar que un token no válido no establece autenticación.
     * - La cadena de filtros continúa normalmente sin interrumpir la petición.
     */
    @Test
    void shouldPassFilterChainWhenInvalidToken() throws Exception {
        String token = "invalid.token";

        when(request.getHeader("Authorization")).thenReturn("Bearer " + token);
        when(jwtUtils.extractUsername(token)).thenReturn("user@example.com");
        when(customUserDetailsService.loadUserByUsername("user@example.com"))
                .thenReturn(new User("user@example.com", "password", Collections.emptyList()));
        when(jwtUtils.isValidToken(anyString(), any(UserDetails.class))).thenReturn(false);

        jwtAuthFilter.doFilterInternal(request, response, filterChain);

        // Verifica que la cadena de filtros continúe
        verify(filterChain, times(1)).doFilter(request, response);

        // Asegura que no se establezca autenticación
        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }
}