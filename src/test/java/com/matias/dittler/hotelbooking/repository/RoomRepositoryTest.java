package com.matias.dittler.hotelbooking.repository;

import com.matias.dittler.hotelbooking.entity.Room;
import com.matias.dittler.hotelbooking.entity.Booking;
import com.matias.dittler.hotelbooking.entity.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test de repositorios JPA centrado en RoomRepository y su relación con Booking y User.
 * Se valida la lógica de consultas personalizadas como obtener tipos de habitaciones
 * y habitaciones disponibles por fecha y tipo.
 */
@DataJpaTest
class RoomRepositoryTest {

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private UserRepository userRepository;

    /**
     * Valida que findDistinctRoomTypes() devuelva solo los tipos distintos de habitaciones.
     */
    @Test
    @DisplayName("Should return distinct room types")
    void shouldReturnDistinctRoomTypes() {

        // Arrange: crear dos habitaciones de tipos diferentes
        Room room1 = new Room();
        room1.setRoomType("SINGLE");
        room1.setRoomPrice(BigDecimal.valueOf(100));

        Room room2 = new Room();
        room2.setRoomType("DOUBLE");
        room2.setRoomPrice(BigDecimal.valueOf(200));

        roomRepository.save(room1);
        roomRepository.save(room2);

        // Act: obtener tipos distintos
        List<String> types = roomRepository.findDistinctRoomTypes();

        // Assert: debe contener exactamente los dos tipos creados
        assertEquals(2, types.size());
        assertTrue(types.contains("SINGLE"));
        assertTrue(types.contains("DOUBLE"));
    }

    /**
     * Valida que findAvailableRoomsByDateAndTypes() devuelva las habitaciones disponibles
     * excluyendo las que estén reservadas en el rango de fechas indicado.
     */
    @Test
    @DisplayName("Should return available rooms by date and type")
    void shouldFindAvailableRoomsByDateAndType() {

        // Arrange: crear habitación
        Room room = new Room();
        room.setRoomType("DELUXE");
        room.setRoomPrice(BigDecimal.valueOf(300));
        room = roomRepository.save(room);

        // Crear usuario necesario para la reserva
        User user = new User();
        user.setEmail("test@test.com");
        user.setName("Test User");
        user.setPassword("1234");
        user.setRole("USER");
        user = userRepository.save(user);

        // Crear reserva que bloquea la habitación del día 1 al 3
        Booking booking = new Booking();
        booking.setRoom(room);
        booking.setUser(user);
        booking.setCheckInDate(LocalDate.now().plusDays(1));
        booking.setCheckOutDate(LocalDate.now().plusDays(3));
        booking.setNumOfAdults(2);
        booking.setNumOfChildren(0);
        booking.setBookingConfirmationCode("CONF123");
        bookingRepository.save(booking);

        // Act: buscar disponibilidad fuera del rango ocupado (días 5 a 7)
        List<Room> availableRooms =
                roomRepository.findAvailableRoomsByDateAndTypes(
                        LocalDate.now().plusDays(5),
                        LocalDate.now().plusDays(7),
                        "DELUXE"
                );

        // Assert: la habitación creada debería estar disponible
        assertEquals(1, availableRooms.size());
    }
}
