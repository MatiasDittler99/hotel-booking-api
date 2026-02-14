package com.matias.dittler.hotelbooking.dto;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test unitario para la clase LoginRequest.
 * Se valida que las restricciones de validación (Jakarta Bean Validation) funcionen correctamente.
 */
class LoginRequestTest {

    // Validator de Bean Validation
    private Validator validator;

    /**
     * Inicializa el Validator antes de cada test.
     */
    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    /**
     * Testea que falle la validación si el email está vacío.
     */
    @Test
    void shouldFailWhenEmailIsBlank() {

        // Arrange: crear LoginRequest con email vacío
        LoginRequest request = new LoginRequest();
        request.setEmail("");
        request.setPassword("1234");

        // Act: validar objeto
        Set<ConstraintViolation<LoginRequest>> violations = validator.validate(request);

        // Assert: debe haber al menos una violación
        assertFalse(violations.isEmpty());
    }

    /**
     * Testea que falle la validación si la contraseña está vacía.
     */
    @Test
    void shouldFailWhenPasswordIsBlank() {

        LoginRequest request = new LoginRequest();
        request.setEmail("test@test.com");
        request.setPassword("");

        Set<ConstraintViolation<LoginRequest>> violations = validator.validate(request);

        assertFalse(violations.isEmpty());
    }

    /**
     * Testea que pase la validación si los campos son válidos.
     */
    @Test
    void shouldPassWhenFieldsAreValid() {

        LoginRequest request = new LoginRequest();
        request.setEmail("test@test.com");
        request.setPassword("1234");

        Set<ConstraintViolation<LoginRequest>> violations = validator.validate(request);

        // Assert: no debe haber violaciones
        assertTrue(violations.isEmpty());
    }
}
