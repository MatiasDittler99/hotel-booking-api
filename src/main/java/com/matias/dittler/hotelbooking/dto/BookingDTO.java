package com.matias.dittler.hotelbooking.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import java.time.LocalDate;

/**
 * Data Transfer Object (DTO) utilizado para transportar información
 * relacionada a una reserva (Booking) entre las distintas capas de la aplicación.
 *
 * Este DTO evita exponer directamente la entidad JPA al cliente,
 * permitiendo mayor control sobre los datos enviados en la respuesta.
 *
 * Se utiliza tanto para respuestas como para intercambio de datos en la API.
 */
@Data // Genera automáticamente getters, setters, toString, equals y hashCode
@JsonInclude(JsonInclude.Include.NON_NULL) 
// Indica que solo se incluirán en el JSON los campos que no sean null.
// Mejora la limpieza de las respuestas y evita enviar datos innecesarios.
public class BookingDTO {

    /**
     * Identificador único de la reserva.
     */
    private Long id;

    /**
     * Fecha de check-in (ingreso del huésped).
     */
    private LocalDate checkInDate;

    /**
     * Fecha de check-out (egreso del huésped).
     */
    private LocalDate checkOutDate;

    /**
     * Número de adultos incluidos en la reserva.
     */
    private int numOfAdults;

    /**
     * Número de niños incluidos en la reserva.
     */
    private int numOfChildren;

    /**
     * Número total de huéspedes (adultos + niños).
     * Puede calcularse automáticamente en la lógica de negocio.
     */
    private int totalNumOfGuest;

    /**
     * Código único de confirmación de la reserva.
     * Se genera automáticamente al crear la reserva.
     */
    private String bookingConfirmationCode;

    /**
     * Información del usuario que realizó la reserva.
     * Se representa mediante UserDTO para evitar exponer la entidad completa.
     */
    private UserDTO user;

    /**
     * Información de la habitación reservada.
     * Se representa mediante RoomDTO.
     */
    private RoomDTO room;

}
