package com.matias.dittler.hotelbooking.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

/**
 * Entidad que representa una reserva (Booking) en el sistema.
 * 
 * Está mapeada a la tabla "bookings" en la base de datos.
 * Contiene la información relacionada con fechas, cantidad de huéspedes
 * y las relaciones con el usuario y la habitación.
 */
@Data // Genera getters, setters, equals, hashCode y toString automáticamente
@Entity // Indica que esta clase es una entidad JPA
@Table(name = "bookings") // Nombre de la tabla en la base de datos
public class Booking {

    /**
     * Identificador único de la reserva.
     * Se genera automáticamente con auto-incremento.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Fecha de entrada (check-in).
     * No puede ser nula.
     */
    @NotNull(message = "La fecha de entrada es requerida")
    private LocalDate checkInDate;

    /**
     * Fecha de salida (check-out).
     * Debe ser una fecha futura.
     */
    @Future(message = "La fecha de salida debe ser futura")
    private LocalDate checkOutDate;

    /**
     * Cantidad de adultos en la reserva.
     * Debe ser al menos 1.
     */
    @Min(value = 1, message = "El número de adultos no debe ser inferior a uno")
    private int numOfAdults;

    /**
     * Cantidad de niños en la reserva.
     * Puede ser 0 como mínimo.
     */
    @Min(value = 0, message = "El número de hijos no debe ser inferior a cero")
    private int numOfChildren;

    /**
     * Total de huéspedes (adultos + niños).
     * Se calcula automáticamente.
     */
    private int totalNumOfGuest;

    /**
     * Código único de confirmación de la reserva.
     * Se genera al crear la reserva.
     */
    private String bookingConfirmationCode;

    /**
     * Relación muchos-a-uno con el usuario.
     * 
     * FetchType.EAGER: el usuario se carga inmediatamente
     * junto con la reserva.
     */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id") // Clave foránea en la tabla bookings
    private User user;

    /**
     * Relación muchos-a-uno con la habitación.
     * 
     * FetchType.LAZY: la habitación se carga solo cuando se necesita.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id") // Clave foránea en la tabla bookings
    private Room room;

    /**
     * Método que calcula automáticamente el total de huéspedes.
     */
    public void calculateTotalNumberOfGuests(){
        this.totalNumOfGuest = this.numOfAdults + this.numOfChildren;
    }

    /**
     * Setter personalizado para adultos.
     * Recalcula automáticamente el total de huéspedes.
     */
    public void setNumOfAdults(int numOfAdults) {
        this.numOfAdults = numOfAdults;
        calculateTotalNumberOfGuests();
    }

    /**
     * Setter personalizado para niños.
     * Recalcula automáticamente el total de huéspedes.
     */
    public void setNumOfChildren(int numOfChildren) {
        this.numOfChildren = numOfChildren;
        calculateTotalNumberOfGuests();
    }

    /**
     * toString personalizado para evitar recursión infinita
     * (no incluye user ni room).
     */
    @Override
    public String toString() {
        return "Booking{" +
                "id=" + id +
                ", checkInDate=" + checkInDate +
                ", checkOutDate=" + checkOutDate +
                ", numOfAdults=" + numOfAdults +
                ", numOfChildren=" + numOfChildren +
                ", totalNumOfGuest=" + totalNumOfGuest +
                ", bookingConfirmationCode='" + bookingConfirmationCode + '\'' +
                '}';
    }
}
