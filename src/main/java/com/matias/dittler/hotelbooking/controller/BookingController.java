package com.matias.dittler.hotelbooking.controller;

import com.matias.dittler.hotelbooking.dto.Response;
import com.matias.dittler.hotelbooking.entity.Booking;
import com.matias.dittler.hotelbooking.service.interfac.InterfaceBookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * Controlador encargado de gestionar las reservas (Bookings) del sistema.
 *
 * Permite:
 * - Crear una nueva reserva
 * - Obtener todas las reservas (solo ADMIN)
 * - Buscar reserva por código de confirmación
 * - Cancelar una reserva
 *
 * Todas las rutas están bajo el prefijo: /bookings
 */
@RestController
@RequestMapping("/bookings")
public class BookingController {

    /**
     * Servicio que contiene la lógica de negocio relacionada a reservas.
     * Se encarga de validaciones, disponibilidad de habitaciones,
     * generación de códigos de confirmación y persistencia.
     */
    @Autowired
    private InterfaceBookingService bookingService;

    /**
     * Crea una nueva reserva para una habitación específica y un usuario determinado.
     *
     * Requiere rol ADMIN o USER.
     *
     * URL: POST /bookings/book-room/{roomId}/{userId}
     *
     * @param roomId ID de la habitación a reservar
     * @param userId ID del usuario que realiza la reserva
     * @param bookingRequest Datos de la reserva (fechas, huéspedes, etc.)
     * @return ResponseEntity con el resultado de la operación y código HTTP correspondiente
     */
    @PostMapping("/book-room/{roomId}/{userId}")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('USER')")
    public ResponseEntity<Response> saveBooking(
            @PathVariable Long roomId,
            @PathVariable Long userId,
            @RequestBody Booking bookingRequest) {

        Response response = bookingService.saveBooking(roomId, userId, bookingRequest);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    /**
     * Obtiene todas las reservas registradas en el sistema.
     *
     * Solo accesible por usuarios con rol ADMIN.
     *
     * URL: GET /bookings/all
     *
     * @return Lista completa de reservas envuelta en un objeto Response
     */
    @GetMapping("/all")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Response> getAllBookings() {

        Response response = bookingService.getAllBookings();
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    /**
     * Busca una reserva utilizando su código de confirmación único.
     *
     * Este endpoint puede utilizarse para que un cliente consulte
     * el estado de su reserva.
     *
     * URL: GET /bookings/get-by-confirmation-code/{confirmationCode}
     *
     * @param confirmationCode Código único generado al crear la reserva
     * @return Información de la reserva correspondiente
     */
    @GetMapping("/get-by-confirmation-code/{confirmationCode}")
    public ResponseEntity<Response> getBookingsByConfirmationCode(
            @PathVariable String confirmationCode) {

        Response response = bookingService.findBookingByConfirmationCode(confirmationCode);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    /**
     * Cancela una reserva existente.
     *
     * Solo accesible por usuarios con rol ADMIN.
     * Puede implicar liberación de la habitación y actualización del estado.
     *
     * URL: DELETE /bookings/cancel/{bookingId}
     *
     * @param bookingId ID de la reserva a cancelar
     * @return Resultado de la operación
     */
    @DeleteMapping("/cancel/{bookingId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Response> cancelBooking(@PathVariable Long bookingId) {

        Response response = bookingService.cancelBooking(bookingId);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }
}