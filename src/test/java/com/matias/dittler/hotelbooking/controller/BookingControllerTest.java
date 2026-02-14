package com.matias.dittler.hotelbooking.controller;

import com.matias.dittler.hotelbooking.dto.Response;
import com.matias.dittler.hotelbooking.entity.Booking;
import com.matias.dittler.hotelbooking.service.interfac.InterfaceBookingService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test unitario para BookingController usando Mockito.
 * Permite probar los endpoints del controlador sin depender de la base de datos ni de la lógica real del servicio.
 */
@ExtendWith(MockitoExtension.class)
class BookingControllerTest {

    // Simula el servicio de Booking
    @Mock
    private InterfaceBookingService bookingService;

    // Inyecta los mocks en el BookingController
    @InjectMocks
    private BookingController bookingController;

    /**
     * Testea que se pueda guardar una reserva correctamente.
     */
    @Test
    void shouldSaveBookingSuccessfully() {

        // Arrange: preparar datos de prueba
        Long roomId = 1L;
        Long userId = 2L;
        Booking bookingRequest = new Booking();

        Response mockResponse = new Response();
        mockResponse.setStatusCode(201);
        mockResponse.setMessage("Booking created successfully");

        // Configura el comportamiento simulado del servicio
        when(bookingService.saveBooking(roomId, userId, bookingRequest))
                .thenReturn(mockResponse);

        // Act: llamar al método del controlador
        ResponseEntity<Response> responseEntity =
                bookingController.saveBooking(roomId, userId, bookingRequest);

        // Assert: verificar resultados
        assertEquals(201, responseEntity.getStatusCodeValue());
        assertEquals("Booking created successfully",
                responseEntity.getBody().getMessage());

        // Verifica que el servicio se haya llamado exactamente una vez
        verify(bookingService, times(1))
                .saveBooking(roomId, userId, bookingRequest);
    }

    /**
     * Testea que se puedan obtener todas las reservas correctamente.
     */
    @Test
    void shouldReturnAllBookings() {

        // Arrange
        Response mockResponse = new Response();
        mockResponse.setStatusCode(200);
        mockResponse.setMessage("All bookings retrieved");

        when(bookingService.getAllBookings()).thenReturn(mockResponse);

        // Act
        ResponseEntity<Response> responseEntity =
                bookingController.getAllBookings();

        // Assert
        assertEquals(200, responseEntity.getStatusCodeValue());
        assertEquals("All bookings retrieved",
                responseEntity.getBody().getMessage());

        verify(bookingService, times(1)).getAllBookings();
    }

    /**
     * Testea que se pueda buscar una reserva por código de confirmación.
     */
    @Test
    void shouldFindBookingByConfirmationCode() {

        // Arrange
        String confirmationCode = "ABC123";

        Response mockResponse = new Response();
        mockResponse.setStatusCode(200);
        mockResponse.setMessage("Booking found");

        when(bookingService.findBookingByConfirmationCode(confirmationCode))
                .thenReturn(mockResponse);

        // Act
        ResponseEntity<Response> responseEntity =
                bookingController.getBookingsByConfirmationCode(confirmationCode);

        // Assert
        assertEquals(200, responseEntity.getStatusCodeValue());
        assertEquals("Booking found",
                responseEntity.getBody().getMessage());

        verify(bookingService, times(1))
                .findBookingByConfirmationCode(confirmationCode);
    }

    /**
     * Testea que se pueda cancelar una reserva correctamente.
     */
    @Test
    void shouldCancelBookingSuccessfully() {

        // Arrange
        Long bookingId = 10L;

        Response mockResponse = new Response();
        mockResponse.setStatusCode(200);
        mockResponse.setMessage("Booking cancelled");

        when(bookingService.cancelBooking(bookingId))
                .thenReturn(mockResponse);

        // Act
        ResponseEntity<Response> responseEntity =
                bookingController.cancelBooking(bookingId);

        // Assert
        assertEquals(200, responseEntity.getStatusCodeValue());
        assertEquals("Booking cancelled",
                responseEntity.getBody().getMessage());

        verify(bookingService, times(1))
                .cancelBooking(bookingId);
    }
}
