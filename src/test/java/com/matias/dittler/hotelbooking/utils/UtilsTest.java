package com.matias.dittler.hotelbooking.utils;

import com.matias.dittler.hotelbooking.dto.*;
import com.matias.dittler.hotelbooking.entity.*;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests unitarios para la clase Utils.
 *
 * Verifica la generación de códigos y el mapeo de entidades a DTOs.
 */
class UtilsTest {

    /**
     * Verifica que el código de confirmación generado tenga la longitud correcta.
     */
    @Test
    void shouldGenerateRandomConfirmationCodeWithCorrectLength() {
        String code = Utils.generateRandomConfirmationCode(10);

        assertNotNull(code);       // Código no nulo
        assertEquals(10, code.length()); // Longitud correcta
    }

    /**
     * Verifica el mapeo correcto de User a UserDTO.
     */
    @Test
    void shouldMapUserEntityToUserDTO() {
        User user = new User();
        user.setId(1L);
        user.setName("Matias");
        user.setEmail("matias@test.com");
        user.setPhoneNumber("123456");
        user.setRole("ADMIN");

        UserDTO dto = Utils.mapUserEntityToUserDTO(user);

        assertEquals(user.getId(), dto.getId());
        assertEquals(user.getName(), dto.getName());
        assertEquals(user.getEmail(), dto.getEmail());
        assertEquals(user.getPhoneNumber(), dto.getPhoneNumber());
        assertEquals(user.getRole(), dto.getRole());
    }

    /**
     * Verifica el mapeo correcto de Room a RoomDTO.
     */
    @Test
    void shouldMapRoomEntityToRoomDTO() {
        Room room = new Room();
        room.setId(1L);
        room.setRoomType("DELUXE");
        room.setRoomPrice(BigDecimal.valueOf(200.0));
        room.setRoomPhotoUrl("photo.jpg");
        room.setRoomDescription("Nice room");

        RoomDTO dto = Utils.mapRoomEntityToRoomDTO(room);

        assertEquals(room.getId(), dto.getId());
        assertEquals(room.getRoomType(), dto.getRoomType());
        assertEquals(room.getRoomPrice(), dto.getRoomPrice());
        assertEquals(room.getRoomPhotoUrl(), dto.getRoomPhotoUrl());
        assertEquals(room.getRoomDescription(), dto.getRoomDescription());
    }

    /**
     * Verifica el mapeo correcto de Booking a BookingDTO.
     */
    @Test
    void shouldMapBookingEntityToBookingDTO() {
        Booking booking = new Booking();
        booking.setId(1L);
        booking.setCheckInDate(LocalDate.now());
        booking.setCheckOutDate(LocalDate.now().plusDays(2));
        booking.setNumOfAdults(2);
        booking.setNumOfChildren(1);
        booking.setTotalNumOfGuest(3);
        booking.setBookingConfirmationCode("ABC123");

        BookingDTO dto = Utils.mapBookingEntityToBookingDTO(booking);

        assertEquals(booking.getId(), dto.getId());
        assertEquals(booking.getCheckInDate(), dto.getCheckInDate());
        assertEquals(booking.getCheckOutDate(), dto.getCheckOutDate());
        assertEquals(booking.getNumOfAdults(), dto.getNumOfAdults());
        assertEquals(booking.getNumOfChildren(), dto.getNumOfChildren());
        assertEquals(booking.getTotalNumOfGuest(), dto.getTotalNumOfGuest());
        assertEquals(booking.getBookingConfirmationCode(), dto.getBookingConfirmationCode());
    }

    /**
     * Verifica que una lista de usuarios se mapee correctamente a DTOs.
     */
    @Test
    void shouldMapUserListToUserDTOList() {
        User user = new User();
        user.setId(1L);
        user.setName("Test");

        List<UserDTO> result = Utils.mapUserListEntityToUserListDTO(List.of(user));

        assertEquals(1, result.size());
        assertEquals(user.getId(), result.get(0).getId());
    }

    /**
     * Verifica que una lista de habitaciones se mapee correctamente a DTOs.
     */
    @Test
    void shouldMapRoomListToRoomDTOList() {
        Room room = new Room();
        room.setId(1L);

        List<RoomDTO> result = Utils.mapRoomListEntityToRoomListDTO(List.of(room));

        assertEquals(1, result.size());
        assertEquals(room.getId(), result.get(0).getId());
    }

    /**
     * Verifica que una lista de reservas se mapee correctamente a DTOs.
     */
    @Test
    void shouldMapBookingListToBookingDTOList() {
        Booking booking = new Booking();
        booking.setId(1L);

        List<BookingDTO> result = Utils.mapBookingListEntityToBookingListDTO(List.of(booking));

        assertEquals(1, result.size());
        assertEquals(booking.getId(), result.get(0).getId());
    }
}
