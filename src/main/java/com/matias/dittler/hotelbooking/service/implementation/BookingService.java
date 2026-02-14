package com.matias.dittler.hotelbooking.service.implementation;

import com.matias.dittler.hotelbooking.dto.BookingDTO;
import com.matias.dittler.hotelbooking.dto.Response;
import com.matias.dittler.hotelbooking.dto.RoomDTO;
import com.matias.dittler.hotelbooking.entity.Booking;
import com.matias.dittler.hotelbooking.entity.Room;
import com.matias.dittler.hotelbooking.entity.User;
import com.matias.dittler.hotelbooking.exception.OurException;
import com.matias.dittler.hotelbooking.repository.BookingRepository;
import com.matias.dittler.hotelbooking.repository.RoomRepository;
import com.matias.dittler.hotelbooking.repository.UserRepository;
import com.matias.dittler.hotelbooking.service.interfac.InterfaceBookingService;
import com.matias.dittler.hotelbooking.service.interfac.InterfaceRoomService;
import com.matias.dittler.hotelbooking.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Servicio que implementa la lógica de negocio relacionada con las reservas (Booking).
 * Implementa la interfaz InterfaceBookingService.
 */
@Service
public class BookingService implements InterfaceBookingService {

    @Autowired
    private BookingRepository bookingRepository; // Repositorio de reservas
    @Autowired
    private InterfaceRoomService roomService; // Servicio de habitaciones
    @Autowired
    private RoomRepository roomRepository; // Repositorio de habitaciones
    @Autowired
    private UserRepository userRepository; // Repositorio de usuarios

    /**
     * Guarda una reserva para un usuario y una habitación determinada.
     * @param rooId ID de la habitación
     * @param userId ID del usuario
     * @param bookingRequest Objeto Booking con la información de la reserva
     * @return Response con estado y mensaje de la operación
     */
    @Override
    public Response saveBooking(Long rooId, Long userId, Booking bookingRequest) {
        Response response = new Response();

        try {
            // Validación: la fecha de check-out debe ser posterior a la de check-in
            if (bookingRequest.getCheckOutDate().isBefore(bookingRequest.getCheckInDate())) {
                throw new IllegalArgumentException("La fecha de entrada debe ser anterior a la fecha de salida.");
            }

            // Buscar habitación y usuario en la base de datos
            Room room = roomRepository.findById(rooId)
                    .orElseThrow(() -> new OurException("Habitación no encontrada"));
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new OurException("Usuario no encontrado"));

            // Verificar disponibilidad de la habitación
            List<Booking> existingBookings = room.getBookings();
            if (!roomIsAvailable(bookingRequest, existingBookings)) {
                throw new OurException("Habitación no disponible para el rango de fechas seleccionado");
            }

            // Asignar habitación y usuario a la reserva
            bookingRequest.setRoom(room);
            bookingRequest.setUser(user);

            // Generar código de confirmación aleatorio
            String bookingConfirmationCode = Utils.generateRandomConfirmationCode(10);
            bookingRequest.setBookingConfirmationCode(bookingConfirmationCode);

            // Guardar reserva
            bookingRepository.save(bookingRequest);

            // Configurar respuesta exitosa
            response.setStatusCode(200);
            response.setMessage("exitoso");
            response.setBookingConfirmationCode(bookingConfirmationCode);

        } catch (OurException e) {
            // Error conocido (habitacion o usuario no encontrados, no disponible)
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        } catch (Exception e) {
            // Otros errores inesperados
            response.setStatusCode(500);
            response.setMessage("Error al guardar una reserva: " + e.getMessage());
        }

        return response;
    }

    /**
     * Busca una reserva por su código de confirmación.
     * @param confirmationCode Código de confirmación de la reserva
     * @return Response con los datos de la reserva si se encuentra
     */
    @Override
    public Response findBookingByConfirmationCode(String confirmationCode) {
        Response response = new Response();

        try {
            Booking booking = bookingRepository.findByBookingConfirmationCode(confirmationCode)
                    .orElseThrow(() -> new OurException("Reserva no encontrada"));

            // Mapear entidad Booking a DTO incluyendo información de la habitación reservada
            BookingDTO bookingDTO = Utils.mapBookingEntityToBookingDTOPlusBookedRooms(booking, true);
            response.setBooking(bookingDTO);
            response.setMessage("exitoso");
            response.setStatusCode(200);

        } catch (OurException e) {
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error al obtener la reserva mediante el código de confirmación: " + e.getMessage());
        }

        return response;
    }

    /**
     * Obtiene todas las reservas registradas en la base de datos.
     * @return Response con la lista de reservas
     */
    @Override
    public Response getAllBookings() {
        Response response = new Response();

        try {
            // Traer todas las reservas ordenadas por ID descendente
            List<Booking> bookingList = bookingRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
            List<BookingDTO> bookingDTOList = Utils.mapBookingListEntityToBookingListDTO(bookingList);

            response.setBookingList(bookingDTOList);
            response.setMessage("exitoso");
            response.setStatusCode(200);

        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error al obtener todas las reservas: " + e.getMessage());
        }

        return response;
    }

    /**
     * Cancela una reserva específica por su ID.
     * @param bookingId ID de la reserva
     * @return Response indicando el resultado de la operación
     */
    @Override
    public Response cancelBooking(Long bookingId) {
        Response response = new Response();

        try {
            // Verificar que la reserva exista
            bookingRepository.findById(bookingId)
                    .orElseThrow(() -> new OurException("Reserva no encontrada"));

            // Eliminar reserva
            bookingRepository.deleteById(bookingId);

            response.setMessage("exitoso");
            response.setStatusCode(200);

        } catch (OurException e) {
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error al cancelar una reserva: " + e.getMessage());
        }

        return response;
    }

    /**
     * Valida si una habitación está disponible en el rango de fechas solicitado.
     * @param bookingRequest Reserva que se desea hacer
     * @param existingBookings Lista de reservas existentes de la habitación
     * @return true si está disponible, false si hay conflicto de fechas
     */
    private boolean roomIsAvailable(Booking bookingRequest, List<Booking> existingBookings) {
        return existingBookings.stream()
                .noneMatch(existingBooking ->
                        // Coinciden fechas de entrada
                        bookingRequest.getCheckInDate().equals(existingBooking.getCheckInDate())
                        // Fecha de salida anterior a una reserva existente
                        || bookingRequest.getCheckOutDate().isBefore(existingBooking.getCheckOutDate())
                        // Fecha de entrada dentro de una reserva existente
                        || (bookingRequest.getCheckInDate().isAfter(existingBooking.getCheckInDate())
                        && bookingRequest.getCheckInDate().isBefore(existingBooking.getCheckOutDate()))
                        // Fecha de entrada antes y salida coincide
                        || (bookingRequest.getCheckInDate().isBefore(existingBooking.getCheckInDate())
                        && bookingRequest.getCheckOutDate().equals(existingBooking.getCheckOutDate()))
                        // Fecha de reserva engloba una existente
                        || (bookingRequest.getCheckInDate().isBefore(existingBooking.getCheckInDate())
                        && bookingRequest.getCheckOutDate().isAfter(existingBooking.getCheckOutDate()))
                        // Fechas iguales inversas
                        || (bookingRequest.getCheckInDate().equals(existingBooking.getCheckOutDate())
                        && bookingRequest.getCheckOutDate().equals(existingBooking.getCheckInDate()))
                        || (bookingRequest.getCheckInDate().equals(existingBooking.getCheckOutDate())
                        && bookingRequest.getCheckOutDate().equals(bookingRequest.getCheckInDate()))
                );
    }
}
