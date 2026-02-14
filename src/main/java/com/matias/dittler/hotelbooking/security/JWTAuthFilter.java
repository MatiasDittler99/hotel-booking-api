package com.matias.dittler.hotelbooking.security;

import com.matias.dittler.hotelbooking.service.CustomUserDetailsService;
import com.matias.dittler.hotelbooking.utils.JWTUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * Filtro de autenticación JWT que se ejecuta una vez por cada petición HTTP.
 *
 * Responsabilidades principales:
 *  - Interceptar solicitudes entrantes.
 *  - Extraer el token JWT del header "Authorization".
 *  - Validar el token.
 *  - Si es válido, establecer la autenticación en el contexto de seguridad.
 *
 * Este filtro se integra en la cadena de filtros de Spring Security
 * antes del UsernamePasswordAuthenticationFilter.
 */
@Component
public class JWTAuthFilter extends OncePerRequestFilter {

    @Autowired
    private JWTUtils jwtUtils; // Utilidades para generar, validar y extraer información del JWT

    @Autowired
    private CustomUserDetailsService customUserDetailsService; // Servicio que obtiene los datos del usuario desde la base de datos

    /**
     * Determina qué rutas NO deben pasar por este filtro JWT.
     *
     * Se excluyen:
     *  - Endpoints públicos (/auth, /rooms, /bookings).
     *  - Documentación OpenAPI y Swagger.
     *  - Recursos estáticos necesarios para Swagger UI.
     *
     * Esto evita validaciones innecesarias y posibles errores 401/403
     * en endpoints que deben ser accesibles sin autenticación.
     *
     * @param request solicitud HTTP entrante
     * @return true si la ruta no debe ser filtrada (se omite JWT)
     */
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();

        return path.startsWith("/v3/api-docs")
                || path.startsWith("/rooms")
                || path.startsWith("/bookings")
                || path.startsWith("/auth")
                || path.startsWith("/swagger-ui")
                || path.startsWith("/swagger-ui.html")
                || path.startsWith("/swagger-ui/index.html")
                || path.startsWith("/webjars");
    }

    /**
     * Método principal del filtro.
     *
     * Flujo:
     *  1. Obtiene el header Authorization.
     *  2. Verifica que tenga formato "Bearer <token>".
     *  3. Extrae el email del usuario desde el token.
     *  4. Valida el token contra los datos del usuario.
     *  5. Si es válido, establece la autenticación en el SecurityContext.
     *
     * @param request  solicitud HTTP
     * @param response respuesta HTTP
     * @param filterChain cadena de filtros de Spring Security
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        // Obtiene el encabezado Authorization
        final String authHeader = request.getHeader("Authorization");
        final String jwtToken;
        final String userEmail;

        // Si no hay header o no comienza con "Bearer ", continúa sin autenticar
        if (authHeader == null || !authHeader.startsWith("Bearer ") || authHeader.isBlank()) {
            filterChain.doFilter(request, response);
            return;
        }

        // Extrae el token JWT removiendo el prefijo "Bearer "
        jwtToken = authHeader.substring(7);

        // Extrae el username (email) desde el token
        userEmail = jwtUtils.extractUsername(jwtToken);

        // Si el token contiene un usuario válido y no existe autenticación previa
        if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            // Carga los detalles del usuario desde la base de datos
            UserDetails userDetails = customUserDetailsService.loadUserByUsername(userEmail);

            // Verifica que el token sea válido y no esté expirado
            if (jwtUtils.isValidToken(jwtToken, userDetails)) {

                // Crea un nuevo contexto de seguridad vacío
                SecurityContext securityContext = SecurityContextHolder.createEmptyContext();

                // Crea el objeto de autenticación con los roles del usuario
                UsernamePasswordAuthenticationToken authenticationToken =
                        new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                userDetails.getAuthorities()
                        );

                // Agrega detalles adicionales de la petición (IP, sesión, etc.)
                authenticationToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );

                // Establece la autenticación en el contexto
                securityContext.setAuthentication(authenticationToken);
                SecurityContextHolder.setContext(securityContext);
            }
        }

        // Continúa con la cadena de filtros
        filterChain.doFilter(request, response);
    }
}
