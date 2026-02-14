package com.matias.dittler.hotelbooking.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import java.math.BigDecimal;
import java.util.List;

/**
 * DTO (Data Transfer Object) que representa la información
 * de una habitación que se enviará al cliente.
 *
 * Se utiliza para evitar exponer directamente la entidad Room
 * y para controlar exactamente qué datos se devuelven en la API.
 */
@Data // Genera automáticamente getters, setters, equals, hashCode y toString
@JsonInclude(JsonInclude.Include.NON_NULL) // No incluye campos null en el JSON
public class RoomDTO {

    /**
     * Identificador único de la habitación.
     */
    private Long id;

    /**
     * Tipo de habitación.
     * Ej: "Single", "Double", "Suite", etc.
     */
    private String roomType;

    /**
     * Precio de la habitación por noche.
     * Se usa BigDecimal para evitar problemas de precisión con valores monetarios.
     */
    private BigDecimal roomPrice;

    /**
     * URL pública de la imagen de la habitación.
     * Generalmente almacenada en Cloudflare R2 u otro servicio de almacenamiento.
     */
    private String roomPhotoUrl;

    /**
     * Descripción detallada de la habitación.
     */
    private String roomDescription;

    /**
     * Lista de reservas asociadas a esta habitación.
     * Se incluye cuando se necesita mostrar información
     * relacionada con las reservas.
     */
    private List<BookingDTO> bookings;

}
