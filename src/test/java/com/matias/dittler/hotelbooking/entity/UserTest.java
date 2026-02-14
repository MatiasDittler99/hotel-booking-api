package com.matias.dittler.hotelbooking.entity;

import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;
import java.util.Collection;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test de la entidad User centrado en la integración con Spring Security.
 * Se valida que los métodos requeridos por UserDetails funcionen correctamente.
 */
class UserTest {

    /**
     * Verifica que getAuthorities() devuelva correctamente el rol como autoridad.
     */
    @Test
    void shouldReturnCorrectAuthority() {

        User user = new User();
        user.setRole("ADMIN");

        // Act: obtener autoridades según UserDetails
        Collection<? extends GrantedAuthority> authorities = user.getAuthorities();

        // Assert: debe tener exactamente una autoridad y coincidir con el rol asignado
        assertEquals(1, authorities.size());
        assertEquals("ADMIN", authorities.iterator().next().getAuthority());
    }

    /**
     * Verifica que getUsername() devuelva correctamente el email del usuario.
     */
    @Test
    void shouldReturnEmailAsUsername() {

        User user = new User();
        user.setEmail("test@email.com");

        // Act & Assert: username coincide con el email
        assertEquals("test@email.com", user.getUsername());
    }
}
