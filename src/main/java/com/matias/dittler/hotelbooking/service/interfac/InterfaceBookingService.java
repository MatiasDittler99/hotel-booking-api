package com.matias.dittler.hotelbooking.service.interfac;

import com.matias.dittler.hotelbooking.dto.Response;
import com.matias.dittler.hotelbooking.entity.Booking;

/**
 * Interface que define los métodos del servicio de reservas (BookingService).
 * Cada método devuelve un objeto Response con el resultado de la operación.
 */
public interface InterfaceBookingService {

    /**
     * Guarda una nueva reserva en el sistema.
     * @param rooId ID de la habitación a reservar
     * @param userId ID del usuario que realiza la reserva
     * @param bookingRequest Objeto Booking con los datos de la reserva
     * @return Response con estado, mensaje y código de confirmación
     */
    Response saveBooking(Long rooId, Long userId, Booking bookingRequest);

    /**
     * Obtiene una reserva por su código de confirmación.
     * @param confirmationCode Código único de la reserva
     * @return Response con estado, mensaje y datos de la reserva
     */
    Response findBookingByConfirmationCode(String confirmationCode);

    /**
     * Obtiene todas las reservas existentes en el sistema.
     * @return Response con lista de reservas
     */
    Response getAllBookings();

    /**
     * Cancela una reserva por su ID.
     * @param bookingId ID de la reserva a cancelar
     * @return Response con estado y mensaje de la operación
     */
    Response cancelBooking(Long bookingId);

}
