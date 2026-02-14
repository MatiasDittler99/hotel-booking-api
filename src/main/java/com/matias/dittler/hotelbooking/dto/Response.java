package com.matias.dittler.hotelbooking.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import java.util.List;

/**
 * Clase genérica de respuesta para toda la API.
 *
 * Se utiliza para estandarizar las respuestas del backend,
 * incluyendo información como código de estado, mensajes,
 * datos de autenticación y entidades del sistema.
 *
 * Gracias a @JsonInclude(JsonInclude.Include.NON_NULL),
 * solo se enviarán en el JSON los campos que no sean null.
 */
@Data // Lombok genera getters, setters, toString, equals y hashCode automáticamente
@JsonInclude(JsonInclude.Include.NON_NULL) // Excluye campos null en la respuesta JSON
public class Response {

    /**
     * Código de estado HTTP personalizado.
     * Ej: 200, 201, 400, 404, 500, etc.
     */
    private int statusCode;

    /**
     * Mensaje descriptivo del resultado de la operación.
     * Ej: "Usuario creado correctamente", "Credenciales inválidas", etc.
     */
    private String message;

    /**
     * Token JWT generado al hacer login.
     * Solo se devuelve en procesos de autenticación exitosos.
     */
    private String token;

    /**
     * Rol del usuario autenticado (ADMIN o USER).
     */
    private String role;

    /**
     * Tiempo de expiración del token JWT.
     */
    private String expirationTime;

    /**
     * Código de confirmación generado al crear una reserva.
     */
    private String bookingConfirmationCode;

    /**
     * Información de un usuario específico.
     * Se usa cuando se devuelve un solo usuario.
     */
    private UserDTO user;

    /**
     * Información de una habitación específica.
     */
    private RoomDTO room;

    /**
     * Información de una reserva específica.
     */
    private BookingDTO booking;

    /**
     * Lista de usuarios.
     * Se usa en endpoints que retornan múltiples usuarios.
     */
    private List<UserDTO> userList;

    /**
     * Lista de habitaciones.
     */
    private List<RoomDTO> roomList;

    /**
     * Lista de reservas.
     */
    private List<BookingDTO> bookingList;

}
