package com.matias.dittler.hotelbooking.repository;

import com.matias.dittler.hotelbooking.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

/**
 * Repositorio para la entidad Booking.
 *
 * Extiende JpaRepository, por lo que hereda métodos CRUD básicos:
 * - save()
 * - findById()
 * - findAll()
 * - deleteById()
 * - etc.
 *
 * JpaRepository maneja automáticamente la conexión con la base de datos
 * y las transacciones por defecto.
 */
public interface BookingRepository extends JpaRepository<Booking, Long> {

    /**
     * Método personalizado para buscar una reserva por su código de confirmación.
     *
     * @param confirmationCode Código de confirmación de la reserva
     * @return Optional<Booking> que contendrá la reserva si se encuentra
     */
    Optional<Booking> findByBookingConfirmationCode(String confirmationCode);

}
