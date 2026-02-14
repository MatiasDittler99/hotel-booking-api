package com.matias.dittler.hotelbooking.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Entidad que representa una habitación dentro del sistema.
 *
 * Está mapeada a la tabla "rooms" en la base de datos.
 * Contiene la información principal de cada habitación
 * y su relación con las reservas.
 */
@Data // Genera getters, setters, equals, hashCode y toString automáticamente
@Entity // Indica que es una entidad JPA
@Table(name = "rooms") // Nombre de la tabla en la base de datos
public class Room {

    /**
     * Identificador único de la habitación.
     * Se genera automáticamente (auto-incremental).
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Tipo de habitación.
     * Ej: Single, Double, Suite, etc.
     */
    private String roomType;

    /**
     * Precio de la habitación.
     * Se utiliza BigDecimal para evitar problemas de precisión
     * con valores monetarios.
     */
    private BigDecimal roomPrice;

    /**
     * URL pública de la imagen de la habitación.
     * Generalmente almacenada en Cloudflare R2 o similar.
     */
    private String roomPhotoUrl;

    /**
     * Descripción detallada de la habitación.
     */
    private String roomDescription;

    /**
     * Relación uno-a-muchos con Booking.
     *
     * - mappedBy = "room" indica que la relación está definida
     *   en la entidad Booking.
     * - FetchType.LAZY: las reservas se cargan solo cuando se necesitan.
     * - CascadeType.ALL: cualquier operación sobre Room (persist, delete, etc.)
     *   se propaga a las reservas asociadas.
     */
    @OneToMany(mappedBy = "room", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Booking> bookings = new ArrayList<>();

    /**
     * toString personalizado.
     * 
     * No incluye la lista de bookings para evitar:
     * - Recursividad infinita
     * - Problemas de rendimiento
     */
    @Override
    public String toString() {
        return "Room{" +
                "id=" + id +
                ", roomType='" + roomType + '\'' +
                ", roomPrice=" + roomPrice +
                ", roomPhotoUrl='" + roomPhotoUrl + '\'' +
                ", description='" + roomDescription + '\'' +
                '}';
    }
}
