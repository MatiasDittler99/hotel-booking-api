package com.matias.dittler.hotelbooking.utils;

import com.matias.dittler.hotelbooking.dto.BookingDTO;
import com.matias.dittler.hotelbooking.dto.RoomDTO;
import com.matias.dittler.hotelbooking.dto.UserDTO;
import com.matias.dittler.hotelbooking.entity.Booking;
import com.matias.dittler.hotelbooking.entity.Room;
import com.matias.dittler.hotelbooking.entity.User;

import java.security.SecureRandom;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Clase utilitaria con métodos estáticos para:
 *  - Generación de códigos de confirmación
 *  - Mapeo de entidades a DTOs
 */
public class Utils {

    // Caracteres permitidos para generar códigos aleatorios
    private static final String ALPHANUMERIC_STRING = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

    // Generador seguro de números aleatorios
    private static final SecureRandom secureRandom = new SecureRandom();

    /**
     * Genera un código aleatorio alfanumérico de longitud 'length'
     * Se utiliza para códigos de confirmación de reservas
     */
    public static String generateRandomConfirmationCode(int length) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int randomIndex = secureRandom.nextInt(ALPHANUMERIC_STRING.length());
            char randomChar = ALPHANUMERIC_STRING.charAt(randomIndex);
            stringBuilder.append(randomChar);
        }
        return stringBuilder.toString();
    }

    // ===================== Mapeos Usuario =====================

    /**
     * Mapea una entidad User a UserDTO
     */
    public static UserDTO mapUserEntityToUserDTO(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setName(user.getName());
        userDTO.setEmail(user.getEmail());
        userDTO.setPhoneNumber(user.getPhoneNumber());
        userDTO.setRole(user.getRole());
        return userDTO;
    }

    /**
     * Mapea una entidad User a UserDTO incluyendo reservas y habitaciones
     */
    public static UserDTO mapUserEntityToUserDTOPlusUserBookingsAndRoom(User user){
        UserDTO userDTO = mapUserEntityToUserDTO(user); // mapeo base

        if (!user.getBookings().isEmpty()){
            userDTO.setBookings(user.getBookings().stream()
                    .map(booking -> mapBookingEntityToBookingDTOPlusBookedRooms(booking, false))
                    .collect(Collectors.toList()));
        }
        return userDTO;
    }

    /**
     * Mapea lista de usuarios a lista de DTOs
     */
    public static List<UserDTO> mapUserListEntityToUserListDTO(List<User> userList){
        return userList.stream().map(Utils::mapUserEntityToUserDTO).collect(Collectors.toList());
    }

    // ===================== Mapeos Habitación =====================

    /**
     * Mapea una entidad Room a RoomDTO
     */
    public static RoomDTO mapRoomEntityToRoomDTO(Room room) {
        RoomDTO roomDTO = new RoomDTO();
        roomDTO.setId(room.getId());
        roomDTO.setRoomType(room.getRoomType());
        roomDTO.setRoomPrice(room.getRoomPrice());
        roomDTO.setRoomPhotoUrl(room.getRoomPhotoUrl());
        roomDTO.setRoomDescription(room.getRoomDescription());
        return roomDTO;
    }

    /**
     * Mapea una entidad Room a RoomDTO incluyendo reservas de esa habitación
     */
    public static RoomDTO mapRoomEntityToRoomDTOPlusBookings(Room room) {
        RoomDTO roomDTO = mapRoomEntityToRoomDTO(room);

        if (room.getBookings() != null) {
            roomDTO.setBookings(room.getBookings().stream()
                    .map(Utils::mapBookingEntityToBookingDTO)
                    .collect(Collectors.toList()));
        }

        return roomDTO;
    }

    /**
     * Mapea lista de habitaciones a lista de DTOs
     */
    public static List<RoomDTO> mapRoomListEntityToRoomListDTO(List<Room> roomList){
        return roomList.stream().map(Utils::mapRoomEntityToRoomDTO).collect(Collectors.toList());
    }

    // ===================== Mapeos Reserva =====================

    /**
     * Mapea una entidad Booking a BookingDTO
     */
    public static BookingDTO mapBookingEntityToBookingDTO(Booking booking) {
        BookingDTO bookingDTO = new BookingDTO();
        bookingDTO.setId(booking.getId());
        bookingDTO.setCheckInDate(booking.getCheckInDate());
        bookingDTO.setCheckOutDate(booking.getCheckOutDate());
        bookingDTO.setNumOfChildren(booking.getNumOfChildren());
        bookingDTO.setNumOfAdults(booking.getNumOfAdults());
        bookingDTO.setTotalNumOfGuest(booking.getTotalNumOfGuest());
        bookingDTO.setBookingConfirmationCode(booking.getBookingConfirmationCode());
        return bookingDTO;
    }

    /**
     * Mapea una reserva incluyendo usuario y habitación
     * @param mapUser determina si se mapea el usuario dentro de la reserva
     */
    public static BookingDTO mapBookingEntityToBookingDTOPlusBookedRooms(Booking booking, boolean mapUser) {
        BookingDTO bookingDTO = mapBookingEntityToBookingDTO(booking);

        if (mapUser) {
            bookingDTO.setUser(mapUserEntityToUserDTO(booking.getUser()));
        }

        if (booking.getRoom() != null) {
            RoomDTO roomDTO = mapRoomEntityToRoomDTO(booking.getRoom());
            bookingDTO.setRoom(roomDTO);
        }

        return bookingDTO;
    }

    /**
     * Mapea lista de reservas a lista de DTOs
     */
    public static List<BookingDTO> mapBookingListEntityToBookingListDTO(List<Booking> bookingList){
        return bookingList.stream().map(Utils::mapBookingEntityToBookingDTO).collect(Collectors.toList());
    }
}
