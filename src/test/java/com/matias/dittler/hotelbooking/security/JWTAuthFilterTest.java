package com.matias.dittler.hotelbooking.security;

import com.matias.dittler.hotelbooking.service.CustomUserDetailsService;
import com.matias.dittler.hotelbooking.utils.JWTUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Tests unitarios para JWTAuthFilter, el filtro de autenticación basado en JWT.
 *
 * Objetivos:
 * - Asegurar que el filtro no afecte el SecurityContext si no hay header Authorization.
 * - Validar que un token válido autentique correctamente al usuario.
 * - Verificar que un token inválido no autentique a nadie.
 */
@ExtendWith(MockitoExtension.class) // Habilita Mockito para JUnit 5
class JWTAuthFilterTest {

    // Simulamos la utilidad de JWT para no depender de la lógica real de tokens
    @Mock
    private JWTUtils jwtUtils;

    // Simulamos el servicio de carga de usuarios
    @Mock
    private CustomUserDetailsService customUserDetailsService;

    // Simulamos la request HTTP
    @Mock
    private HttpServletRequest request;

    // Simulamos la response HTTP
    @Mock
    private HttpServletResponse response;

    // Simulamos la cadena de filtros de Spring Security
    @Mock
    private FilterChain filterChain;

    // Inyectamos los mocks en JWTAuthFilter
    @InjectMocks
    private JWTAuthFilter jwtAuthFilter;

    /**
     * Se ejecuta antes de cada test para limpiar el SecurityContext
     * y asegurar que los tests sean independientes.
     */
    @BeforeEach
    void setUp() {
        SecurityContextHolder.clearContext();
    }

    /**
     * Verifica que el método shouldNotFilter() retorne true
     * para todas las rutas que deben estar excluidas del filtro JWT.
     *
     * Estas rutas incluyen:
     * - Documentación OpenAPI (/v3/api-docs)
     * - Swagger UI
     * - Endpoints públicos (auth, rooms, bookings)
     * - Recursos estáticos necesarios para Swagger (/webjars)
     *
     * Si el método devuelve true, significa que el filtro
     * NO intentará validar token JWT en esas rutas.
     */
    @Test
    void shouldNotFilterSwaggerAndPublicEndpoints() {

        // Endpoint de documentación OpenAPI
        when(request.getRequestURI()).thenReturn("/v3/api-docs");
        assertTrue(jwtAuthFilter.shouldNotFilter(request));

        // Recursos de Swagger UI
        when(request.getRequestURI()).thenReturn("/swagger-ui/index.html");
        assertTrue(jwtAuthFilter.shouldNotFilter(request));

        // Endpoint público de autenticación
        when(request.getRequestURI()).thenReturn("/auth/login");
        assertTrue(jwtAuthFilter.shouldNotFilter(request));

        // Endpoint público de rooms
        when(request.getRequestURI()).thenReturn("/rooms");
        assertTrue(jwtAuthFilter.shouldNotFilter(request));

        // Endpoint público de bookings
        when(request.getRequestURI()).thenReturn("/bookings");
        assertTrue(jwtAuthFilter.shouldNotFilter(request));

        // Recursos estáticos utilizados por Swagger
        when(request.getRequestURI()).thenReturn("/webjars/some-file.js");
        assertTrue(jwtAuthFilter.shouldNotFilter(request));
    }


    /**
     * Verifica que el método shouldNotFilter() retorne false
     * para rutas que NO están excluidas.
     *
     * En estos casos el filtro JWT debe ejecutarse,
     * lo que implica que se intentará validar el token
     * y establecer autenticación si corresponde.
     */
    @Test
    void shouldFilterProtectedEndpoints() {

        // Endpoint protegido típico de usuario
        when(request.getRequestURI()).thenReturn("/users/profile");
        assertFalse(jwtAuthFilter.shouldNotFilter(request));

        // Endpoint protegido de administración
        when(request.getRequestURI()).thenReturn("/admin/dashboard");
        assertFalse(jwtAuthFilter.shouldNotFilter(request));
    }


    /**
     * Verifica que si no hay header Authorization:
     * - El SecurityContext permanezca vacío.
     * - La cadena de filtros continúe normalmente.
     */
    @Test
    void shouldDoNothingWhenNoAuthorizationHeader() throws Exception {
        when(request.getHeader("Authorization")).thenReturn(null);

        jwtAuthFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }

    /**
     * Verifica la autenticación correcta cuando:
     * - El token es válido.
     * - El usuario existe.
     * 
     * Pasos:
     * 1. Simula el header Authorization con token válido.
     * 2. Simula extracción de username y carga del usuario.
     * 3. Valida que el token es correcto.
     * 4. Verifica que el SecurityContext tenga la autenticación.
     * 5. La cadena de filtros continúa.
     */
    @Test
    void shouldAuthenticateWhenTokenIsValid() throws Exception {
        String token = "valid.jwt.token";
        String email = "test@email.com";

        UserDetails userDetails = User
                .withUsername(email)
                .password("password")
                .authorities("ROLE_USER")
                .build();

        when(request.getHeader("Authorization")).thenReturn("Bearer " + token);
        when(jwtUtils.extractUsername(token)).thenReturn(email);
        when(customUserDetailsService.loadUserByUsername(email)).thenReturn(userDetails);
        when(jwtUtils.isValidToken(token, userDetails)).thenReturn(true);

        jwtAuthFilter.doFilterInternal(request, response, filterChain);

        // Se verifica que la autenticación se haya seteado correctamente
        assertNotNull(SecurityContextHolder.getContext().getAuthentication());
        assertEquals(email,
                SecurityContextHolder.getContext().getAuthentication().getName());

        verify(filterChain).doFilter(request, response);
    }

    /**
     * Verifica que si el token es inválido:
     * - No se autentique ningún usuario.
     * - El SecurityContext permanezca vacío.
     * - La cadena de filtros continúe normalmente.
     */
    @Test
    void shouldNotAuthenticateWhenTokenIsInvalid() throws Exception {
        String token = "invalid.jwt.token";
        String email = "test@email.com";

        UserDetails userDetails = User
                .withUsername(email)
                .password("password")
                .authorities("ROLE_USER")
                .build();

        when(request.getHeader("Authorization")).thenReturn("Bearer " + token);
        when(jwtUtils.extractUsername(token)).thenReturn(email);
        when(customUserDetailsService.loadUserByUsername(email)).thenReturn(userDetails);
        when(jwtUtils.isValidToken(token, userDetails)).thenReturn(false);

        jwtAuthFilter.doFilterInternal(request, response, filterChain);

        assertNull(SecurityContextHolder.getContext().getAuthentication());
        verify(filterChain).doFilter(request, response);
    }
}
