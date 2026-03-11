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
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Configuración de seguridad de Spring Security para la aplicación.
 * Incluye:
 * - CORS
 * - Desactivación de CSRF
 * - Configuración de endpoints públicos y protegidos
 * - Autenticación basada en JWT
 * - Gestión de contraseñas con BCrypt
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity // Permite usar anotaciones como @PreAuthorize en métodos
public class SecurityConfig {

    @Autowired
    private CustomUserDetailsService customUserDetailsService; // Servicio que carga datos de usuario

    @Autowired
    private JWTAuthFilter jwtAuthFilter; // Filtro que valida tokens JWT

    /**
     * Configuración de la cadena de filtros de seguridad.
     * @param httpSecurity Objeto HttpSecurity de Spring Security
     * @return SecurityFilterChain configurado
     * @throws Exception
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                // Desactiva CSRF (no es necesario con JWT)
                .csrf(csrf-> csrf.disable())
                // Habilita CORS con la configuración por defecto
                .cors(Customizer.withDefaults())
                // Configuración de autorización de endpoints
                .authorizeHttpRequests(request -> request
                        .requestMatchers("/").permitAll()
                        .requestMatchers("/auth/**").permitAll()
                        .requestMatchers("/rooms/all-available-rooms").permitAll()
                        .requestMatchers("/rooms/available-rooms-by-date-and-type").permitAll()
                        .requestMatchers("/rooms/types").permitAll()
                        .requestMatchers("/rooms/all").permitAll()
                        .requestMatchers("/rooms/room-by-id/**").permitAll()
                        .requestMatchers("/v3/api-docs/**","/swagger-ui/**","/swagger-ui.html", "/swagger-ui/index.html", "/webjars/**").permitAll() // Endpoints públicos
                        // .requestMatchers("/auth/**", "/rooms/**", "/bookings/**").permitAll() // Endpoint publicos
                        // Rooms - ADMIN only
                        .requestMatchers("/rooms/add").hasAuthority("ADMIN")
                        .requestMatchers("/rooms/update/**").hasAuthority("ADMIN")
                        .requestMatchers("/rooms/delete/**").hasAuthority("ADMIN")
                         // Bookings
                        .requestMatchers("/bookings/book-room/**").hasAnyAuthority("USER","ADMIN")
                        .requestMatchers("/bookings/cancel/**").hasAnyAuthority("USER","ADMIN")
                        .requestMatchers("/bookings/all").hasAuthority("ADMIN")
                        .requestMatchers("/bookings/get-by-confirmation-code/**").authenticated()

                        // Users
                        .requestMatchers("/users/all").hasAuthority("ADMIN")
                        .requestMatchers("/users/get-logged-in-profile-info").authenticated()
                        .requestMatchers("/users/get-by-id/**").authenticated()
                        .requestMatchers("/users/get-user-bookings/**").authenticated()
                        .requestMatchers("/users/delete/**").hasAuthority("ADMIN")
                        .anyRequest().authenticated() // Todos los demás requieren autenticación
                )
                // Configuración de sesiones: sin estado, ya que usamos JWT
                .sessionManagement(manager -> manager.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // Configura el proveedor de autenticación
                .authenticationProvider(authenticationProvider())
                // Agrega nuestro filtro JWT antes del filtro de login de Spring Security
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return httpSecurity.build();
    }

    /**
     * Configura el proveedor de autenticación DAO con nuestro UserDetailsService y codificador de contraseñas.
     * @return AuthenticationProvider
     */
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        // Se indica cómo se cargan los usuarios
        daoAuthenticationProvider.setUserDetailsService(customUserDetailsService);
        // Se indica cómo se codifican y verifican las contraseñas
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        return daoAuthenticationProvider;
    }

    /**
     * Bean para codificar las contraseñas usando BCrypt.
     * @return PasswordEncoder
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Bean que expone el AuthenticationManager de Spring Security.
     * Es útil para realizar autenticaciones manuales, por ejemplo en el login.
     * @param authenticationConfiguration Configuración de autenticación de Spring
     * @return AuthenticationManager
     * @throws Exception
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

}
