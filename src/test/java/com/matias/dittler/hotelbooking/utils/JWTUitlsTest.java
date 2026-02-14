package com.matias.dittler.hotelbooking.utils;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.Collections;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests unitarios para JWTUtils.
 *
 * Verifica la generación, extracción y validación de tokens JWT.
 */
class JWTUtilsTest {

    private JWTUtils jwtUtils;
    private UserDetails userDetails;

    @BeforeEach
    void setUp() {
        jwtUtils = new JWTUtils();

        // Usuario simulado para generar y validar tokens
        userDetails = new User(
                "testuser",
                "password",
                Collections.emptyList()
        );
    }

    /**
     * Verifica que se pueda generar un token válido.
     */
    @Test
    void shouldGenerateToken() {
        String token = jwtUtils.generateToken(userDetails);

        assertNotNull(token);      // Token no nulo
        assertFalse(token.isEmpty()); // Token no vacío
    }

    /**
     * Verifica que se pueda extraer correctamente el nombre de usuario de un token.
     */
    @Test
    void shouldExtractUsernameFromToken() {
        String token = jwtUtils.generateToken(userDetails);

        String username = jwtUtils.extractUsername(token);

        assertEquals("testuser", username); // El username extraído coincide
    }

    /**
     * Verifica que un token generado sea válido para el mismo usuario.
     */
    @Test
    void shouldValidateCorrectToken() {
        String token = jwtUtils.generateToken(userDetails);

        boolean isValid = jwtUtils.isValidToken(token, userDetails);

        assertTrue(isValid); // Token válido
    }

    /**
     * Verifica que un token no sea válido para otro usuario distinto.
     */
    @Test
    void shouldInvalidateTokenWithDifferentUser() {
        String token = jwtUtils.generateToken(userDetails);

        UserDetails otherUser = new User(
                "otheruser",
                "password",
                Collections.emptyList()
        );

        boolean isValid = jwtUtils.isValidToken(token, otherUser);

        assertFalse(isValid); // Token inválido para usuario diferente
    }
}
