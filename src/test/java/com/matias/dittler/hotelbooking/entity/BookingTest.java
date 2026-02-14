package com.matias.dittler.hotelbooking.entity;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Test unitario para la entidad Booking.
 * Se centra en verificar que el cálculo del total de huéspedes funcione correctamente
 * al modificar el número de adultos o niños.
 */
class BookingTest {

    /**
     * Verifica que el total de huéspedes se calcule correctamente al establecer adultos y niños.
     */
    @Test
    void shouldCalculateTotalGuestsCorrectly() {

        // Arrange: crear una reserva con 2 adultos y 3 niños
        Booking booking = new Booking();
        booking.setNumOfAdults(2);
        booking.setNumOfChildren(3);

        // Act & Assert: total de huéspedes debe ser 5
        assertEquals(5, booking.getTotalNumOfGuest());
    }

    /**
     * Verifica que el total de huéspedes se actualice correctamente al cambiar el número de adultos.
     */
    @Test
    void shouldUpdateTotalGuestsWhenAdultsChange() {

        Booking booking = new Booking();
        booking.setNumOfAdults(1);
        booking.setNumOfChildren(1);

        // Cambiar número de adultos
        booking.setNumOfAdults(4);

        // Total de huéspedes ahora debe ser 4 adultos + 1 niño = 5
        assertEquals(5, booking.getTotalNumOfGuest());
    }

    /**
     * Verifica que el total de huéspedes se actualice correctamente al cambiar el número de niños.
     */
    @Test
    void shouldUpdateTotalGuestsWhenChildrenChange() {

        Booking booking = new Booking();
        booking.setNumOfAdults(2);
        booking.setNumOfChildren(2);

        // Cambiar número de niños
        booking.setNumOfChildren(5);

        // Total de huéspedes ahora debe ser 2 adultos + 5 niños = 7
        assertEquals(7, booking.getTotalNumOfGuest());
    }
}
