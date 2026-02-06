package com.matias.dittler.hotelbooking.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
@Entity
@Table(name = "bookings")
public class Booking {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "La fecha de entrada es requerida")
    private LocalDate checkInDate;

    @Future(message = "La fecha de salida debe ser futura")
    private LocalDate checkOutDate;

    @Min(value = 1, message = "El número de adultos no debe ser inferior a uno")
    private int numOfAdults;

    @Min(value = 0, message = "El número de hijos no debe ser inferior a cero")
    private int numOfChildren;

    private int totalNumOfGuest;

    private String bookingConfirmationCode;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id")
    private Room room;


    public void calculateTotalNumberOfGuests(){
        this.totalNumOfGuest = this.numOfAdults + this.numOfChildren;
    }

    public void setNumOfAdults(int numOfAdults) {
        this.numOfAdults = numOfAdults;
        calculateTotalNumberOfGuests();
    }

    public void setNumOfChildren(int numOfChildren) {
        this.numOfChildren = numOfChildren;
        calculateTotalNumberOfGuests();
    }

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
