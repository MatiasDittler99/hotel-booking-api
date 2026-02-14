package com.matias.dittler.hotelbooking.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import java.util.ArrayList;
import java.util.List;

/**
 * DTO (Data Transfer Object) que representa la información
 * de un usuario que será enviada al cliente.
 *
 * Se utiliza para evitar exponer directamente la entidad User
 * (por ejemplo, la contraseña) y para controlar qué datos
 * se incluyen en las respuestas de la API.
 */
@Data // Genera automáticamente getters, setters, equals, hashCode y toString
@JsonInclude(JsonInclude.Include.NON_NULL) // No incluye campos null en el JSON
public class UserDTO {

    /**
     * Identificador único del usuario.
     */
    private Long id;

    /**
     * Email del usuario.
     * Se usa como credencial principal para autenticación.
     */
    private String email;

    /**
     * Nombre completo del usuario.
     */
    private String name;

    /**
     * Número de teléfono del usuario.
     */
    private String phoneNumber;

    /**
     * Rol del usuario dentro del sistema.
     * Ej: ADMIN o USER.
     */
    private String role;

    /**
     * Lista de reservas asociadas al usuario.
     * 
     * Se inicializa como una lista vacía para evitar
     * NullPointerException cuando se agregan reservas.
     */
    private List<BookingDTO> bookings = new ArrayList<>();

}
