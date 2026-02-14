package com.matias.dittler.hotelbooking.entity;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test de validación para la entidad Booking usando Bean Validation (jakarta.validation).
 * Se enfoca en validar que los campos obligatorios y las reglas de negocio estén correctamente implementadas.
 */
class BookingValidationTest {

    private Validator validator;

    /**
     * Inicializa el validador antes de cada test.
     */
    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    /**
     * Verifica que falle la validación cuando la fecha de check-in es nula.
     */
    @Test
    void shouldFailWhenCheckInDateIsNull() {

        Booking booking = new Booking();
        booking.setCheckOutDate(LocalDate.now().plusDays(1)); // check-out válido
        booking.setNumOfAdults(1);

        // Act: validar la entidad
        Set<ConstraintViolation<Booking>> violations = validator.validate(booking);

        // Assert: debe haber violaciones porque checkInDate es obligatorio
        assertFalse(violations.isEmpty());
    }

    /**
     * Verifica que falle la validación cuando la fecha de check-out no es futura respecto a check-in.
     */
    @Test
    void shouldFailWhenCheckOutDateIsNotFuture() {

        Booking booking = new Booking();
        booking.setCheckInDate(LocalDate.now());
        booking.setCheckOutDate(LocalDate.now()); // check-out no es futura
        booking.setNumOfAdults(1);

        // Act: validar la entidad
        Set<ConstraintViolation<Booking>> violations = validator.validate(booking);

        // Assert: debe haber violaciones porque check-out debe ser posterior a check-in
        assertFalse(violations.isEmpty());
    }

    /**
     * Verifica que la validación pase correctamente con un booking válido.
     */
    @Test
    void shouldPassWhenBookingIsValid() {

        Booking booking = new Booking();
        booking.setCheckInDate(LocalDate.now());
        booking.setCheckOutDate(LocalDate.now().plusDays(2));
        booking.setNumOfAdults(2);
        booking.setNumOfChildren(1);

        // Act: validar la entidad
        Set<ConstraintViolation<Booking>> violations = validator.validate(booking);

        // Assert: no debe haber violaciones
        assertTrue(violations.isEmpty());
    }
}
