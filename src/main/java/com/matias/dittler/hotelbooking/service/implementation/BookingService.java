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

@Service
public class BookingService implements InterfaceBookingService {
    @Autowired
    private BookingRepository bookingRepository;
    @Autowired
    private InterfaceRoomService roomService;
    @Autowired
    private RoomRepository roomRepository;
    @Autowired
    private UserRepository userRepository;


    @Override
    public Response saveBooking(Long rooId, Long userId, Booking bookingRequest) {
        Response response = new Response();

        try {
            if(bookingRequest.getCheckOutDate().isBefore(bookingRequest.getCheckInDate())){
                throw new IllegalArgumentException("La fecha de entrada debe ser anterior a la fecha de salida.");
            }
            Room room = roomRepository.findById(rooId).orElseThrow(()-> new OurException("Habitación no encontrada"));
            User user = userRepository.findById(userId).orElseThrow(()-> new OurException("Usuario no encontrado"));

            List<Booking> existingBookings = room.getBookings();
            if (!roomIsAvailable(bookingRequest, existingBookings)){
                throw new OurException("Habitación no disponible para el rango de fechas seleccionado");
            }
            bookingRequest.setRoom(room);
            bookingRequest.setUser(user);
            String bookingConfirmationCode = Utils.generateRandomConfirmationCode(10);
            bookingRequest.setBookingConfirmationCode(bookingConfirmationCode);
            bookingRepository.save(bookingRequest);

            response.setStatusCode(200);
            response.setMessage("existoso");
            response.setBookingConfirmationCode(bookingConfirmationCode);


            response.setMessage("exitoso");
            response.setStatusCode(200);

        }catch (OurException e) {
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        }catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error al guardar una reserva " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response findBookingByConfirmationCode(String confirmationCode) {
        Response response = new Response();

        try {
            Booking booking = bookingRepository.findByBookingConfirmationCode(confirmationCode).orElseThrow(()-> new OurException("Reserva no encontrada"));
            BookingDTO bookingDTO = Utils.mapBookingEntityToBookingDTOPlusBookedRooms(booking, true);
            response.setMessage("exitoso");
            response.setStatusCode(200);
            response.setBooking(bookingDTO);

        }catch (OurException e) {
            response.setStatusCode(404);
            response.setMessage(e.getMessage());

        }catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error al obtener la reserva mediante el código de confirmación " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response getAllBookings() {
        Response response = new Response();

        try {
            List<Booking> bookingList = bookingRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
            List<BookingDTO> bookingDTOList = Utils.mapBookingListEntityToBookingListDTO(bookingList);
            response.setMessage("exitoso");
            response.setStatusCode(200);
            response.setBookingList(bookingDTOList);

        }catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error al obtener todas las reservas " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response cancelBooking(Long bookingId) {
        Response response = new Response();

        try {
            bookingRepository.findById(bookingId).orElseThrow(()-> new OurException("Reserva no encontrada"));
            bookingRepository.deleteById(bookingId);
            response.setMessage("exitoso");
            response.setStatusCode(200);

        }catch (OurException e) {
            response.setStatusCode(404);
            response.setMessage(e.getMessage());

        }catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error al cancelar una reserva " + e.getMessage());
        }
        return response;
    }

    private boolean roomIsAvailable(Booking bookingRequest, List<Booking> existingBookings){
        return existingBookings.stream()
                .noneMatch(existingBooking ->
                        bookingRequest.getCheckInDate().equals(existingBooking.getCheckInDate())
                                || bookingRequest.getCheckOutDate().isBefore(existingBooking.getCheckOutDate())
                                || (bookingRequest.getCheckInDate().isAfter(existingBooking.getCheckInDate())
                                && bookingRequest.getCheckInDate().isBefore(existingBooking.getCheckOutDate()))
                                || (bookingRequest.getCheckInDate().isBefore(existingBooking.getCheckInDate())

                                && bookingRequest.getCheckOutDate().equals(existingBooking.getCheckOutDate()))
                                || (bookingRequest.getCheckInDate().isBefore(existingBooking.getCheckInDate())

                                && bookingRequest.getCheckOutDate().isAfter(existingBooking.getCheckOutDate()))

                                || (bookingRequest.getCheckInDate().equals(existingBooking.getCheckOutDate())
                                && bookingRequest.getCheckOutDate().equals(existingBooking.getCheckInDate()))

                                || (bookingRequest.getCheckInDate().equals(existingBooking.getCheckOutDate())
                                && bookingRequest.getCheckOutDate().equals(bookingRequest.getCheckInDate()))
                );

    }
}
