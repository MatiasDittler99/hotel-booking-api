package com.matias.dittler.hotelbooking.service.implementation;

import com.matias.dittler.hotelbooking.entity.Booking;
import com.matias.dittler.hotelbooking.entity.Room;
import com.matias.dittler.hotelbooking.entity.User;
import com.matias.dittler.hotelbooking.repository.BookingRepository;
import com.matias.dittler.hotelbooking.repository.RoomRepository;
import com.matias.dittler.hotelbooking.repository.UserRepository;
import com.matias.dittler.hotelbooking.dto.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Tests unitarios para BookingService
 * 
 * Se verifican los escenarios principales de creación de reservas (Bookings):
 * - Manejo de room/user inexistentes
 * - Guardado exitoso
 * - Validación de fechas de check-in/check-out
 */
@ExtendWith(MockitoExtension.class)
class BookingServiceTest {

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private RoomRepository roomRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private BookingService bookingService;

    private Booking booking;
    private Room room;
    private User user;

    /**
     * Inicializa objetos comunes antes de cada test.
     */
    @BeforeEach
    void setUp() {
        booking = new Booking();
        booking.setCheckInDate(LocalDate.now().plusDays(1));
        booking.setCheckOutDate(LocalDate.now().plusDays(3));

        room = new Room();
        room.setBookings(new ArrayList<>());

        user = new User();
    }

    /**
     * Testea que se devuelva 404 si la habitación no existe.
     */
    @Test
    void shouldReturn404WhenRoomNotFound() {
        when(roomRepository.findById(1L)).thenReturn(Optional.empty());

        Response response = bookingService.saveBooking(1L, 1L, booking);

        assertEquals(404, response.getStatusCode());
    }

    /**
     * Testea que se devuelva 404 si el usuario no existe.
     */
    @Test
    void shouldReturn404WhenUserNotFound() {
        when(roomRepository.findById(1L)).thenReturn(Optional.of(room));
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        Response response = bookingService.saveBooking(1L, 1L, booking);

        assertEquals(404, response.getStatusCode());
    }

    /**
     * Testea el flujo exitoso de creación de reserva.
     */
    @Test
    void shouldSaveBookingSuccessfully() {
        when(roomRepository.findById(1L)).thenReturn(Optional.of(room));
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(bookingRepository.save(any(Booking.class))).thenReturn(booking);

        Response response = bookingService.saveBooking(1L, 1L, booking);

        assertEquals(200, response.getStatusCode());
        verify(bookingRepository, times(1)).save(any(Booking.class));
    }

    /**
     * Testea que se devuelva 500 si la fecha de check-out es anterior al check-in.
     */
    @Test
    void shouldReturn500WhenCheckoutBeforeCheckin() {
        booking.setCheckOutDate(LocalDate.now().minusDays(1));

        Response response = bookingService.saveBooking(1L, 1L, booking);

        assertEquals(500, response.getStatusCode());
    }
}
