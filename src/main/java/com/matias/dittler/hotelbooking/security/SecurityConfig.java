package com.matias.dittler.hotelbooking.security;

import com.matias.dittler.hotelbooking.service.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Configuración principal de seguridad de Spring Security para la aplicación.
 *
 * Funciones principales:
 *  - Configura CORS y desactiva CSRF (no necesario con JWT).
 *  - Define endpoints públicos y protegidos según roles (ADMIN, USER).
 *  - Integra autenticación basada en JWT mediante JWTAuthFilter.
 *  - Gestiona contraseñas usando BCryptPasswordEncoder.
 *  - Configura la política de sesión como stateless para JWT.
 *
 * Anotaciones:
 *  - @Configuration: Marca la clase como bean de configuración de Spring.
 *  - @EnableWebSecurity: Activa la seguridad web de Spring.
 *  - @EnableMethodSecurity: Permite usar anotaciones como @PreAuthorize en métodos.
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Autowired
    private CustomUserDetailsService customUserDetailsService; // Servicio para cargar datos de usuario

    @Autowired
    private JWTAuthFilter jwtAuthFilter; // Filtro que valida tokens JWT

    /**
     * Configura la cadena de filtros de Spring Security.
     *
     * Flujo principal:
     *  1. Desactiva CSRF y habilita CORS.
     *  2. Define autorización de endpoints según roles y permisos.
     *  3. Configura sesión sin estado (stateless).
     *  4. Integra AuthenticationProvider personalizado y filtro JWT.
     *
     * @param httpSecurity Objeto HttpSecurity de Spring Security
     * @return SecurityFilterChain configurado
     * @throws Exception en caso de error en la configuración de seguridad
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .csrf(csrf -> csrf.disable()) // CSRF no necesario con JWT
                .cors(Customizer.withDefaults()) // Habilita CORS con configuración por defecto
                .authorizeHttpRequests(request -> request
                        // Endpoints públicos
                        .requestMatchers("/", "/auth/**").permitAll()
                        .requestMatchers(
                                "/rooms/all-available-rooms",
                                "/rooms/available-rooms-by-date-and-type",
                                "/rooms/types",
                                "/rooms/all",
                                "/rooms/room-by-id/**").permitAll()
                        .requestMatchers(
                                "/v3/api-docs/**",
                                "/swagger-ui/**",
                                "/swagger-ui.html",
                                "/swagger-ui/index.html",
                                "/webjars/**").permitAll()
                        // Endpoints de habitaciones (ADMIN)
                        .requestMatchers("/rooms/add").hasAuthority("ADMIN")
                        .requestMatchers("/rooms/update/**").hasAuthority("ADMIN")
                        .requestMatchers("/rooms/delete/**").hasAuthority("ADMIN")
                        // Endpoints de reservas
                        .requestMatchers("/bookings/book-room/**").hasAnyAuthority("USER", "ADMIN")
                        .requestMatchers("/bookings/cancel/**").hasAnyAuthority("USER", "ADMIN")
                        .requestMatchers("/bookings/all").hasAuthority("ADMIN")
                        .requestMatchers("/bookings/get-by-confirmation-code/**").authenticated()
                        // Endpoints de usuarios
                        .requestMatchers("/users/all").hasAuthority("ADMIN")
                        .requestMatchers("/users/get-logged-in-profile-info").authenticated()
                        .requestMatchers("/users/get-by-id/**").authenticated()
                        .requestMatchers("/users/get-user-bookings/**").authenticated()
                        .requestMatchers("/users/delete/**").hasAuthority("ADMIN")
                        .anyRequest().authenticated() // Todos los demás requieren autenticación
                )
                // Configura la política de sesión como stateless (JWT)
                .sessionManagement(manager -> manager.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // Configura proveedor de autenticación con UserDetailsService y PasswordEncoder
                .authenticationProvider(authenticationProvider())
                // Agrega filtro JWT antes del UsernamePasswordAuthenticationFilter
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return httpSecurity.build();
    }

    /**
     * Proveedor de autenticación DAO personalizado.
     *
     * Permite:
     *  - Cargar usuarios desde CustomUserDetailsService.
     *  - Validar contraseñas usando BCryptPasswordEncoder.
     *
     * @return AuthenticationProvider configurado
     */
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(customUserDetailsService);
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        return daoAuthenticationProvider;
    }

    /**
     * Bean para codificación de contraseñas usando BCrypt.
     *
     * @return PasswordEncoder que aplica hash BCrypt
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Bean que expone el AuthenticationManager de Spring Security.
     *
     * Útil para autenticaciones manuales, por ejemplo en endpoints de login.
     *
     * @param authenticationConfiguration Configuración de autenticación de Spring
     * @return AuthenticationManager configurado
     * @throws Exception en caso de error al obtener el AuthenticationManager
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}