package com.matias.dittler.hotelbooking.service.implementation;

import com.matias.dittler.hotelbooking.dto.Response;
import com.matias.dittler.hotelbooking.dto.RoomDTO;
import com.matias.dittler.hotelbooking.entity.Room;
import com.matias.dittler.hotelbooking.exception.OurException;
import com.matias.dittler.hotelbooking.repository.BookingRepository;
import com.matias.dittler.hotelbooking.repository.RoomRepository;
import com.matias.dittler.hotelbooking.service.R2StorageService;
import com.matias.dittler.hotelbooking.service.interfac.InterfaceRoomService;
import com.matias.dittler.hotelbooking.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * Servicio que implementa la lógica de negocio relacionada con las habitaciones (Room).
 * Implementa la interfaz InterfaceRoomService.
 */
@Service
public class RoomService implements InterfaceRoomService {

    @Autowired
    private RoomRepository roomRepository; // Repositorio de habitaciones

    @Autowired
    private BookingRepository bookingRepository; // Repositorio de reservas

    @Autowired
    private R2StorageService rStorageService; // Servicio para manejar subida de imágenes

    /**
     * Agrega una nueva habitación.
     * @param photo Imagen de la habitación
     * @param roomType Tipo de habitación (Ej: Suite, Doble)
     * @param roomPrice Precio de la habitación
     * @param description Descripción de la habitación
     * @return Response con estado y datos de la habitación guardada
     */
    @Override
    public Response addNewRoom(MultipartFile photo, String roomType, BigDecimal roomPrice, String description) {
        Response response = new Response();

        try {
            // Subir imagen a R2Storage y obtener URL
            String imageUrl = rStorageService.uploadImage(photo);

            // Crear nueva entidad Room
            Room room = new Room();
            room.setRoomPhotoUrl(imageUrl);
            room.setRoomType(roomType);
            room.setRoomPrice(roomPrice);
            room.setRoomDescription(description);

            // Guardar en base de datos
            Room savedRoom = roomRepository.save(room);

            // Mapear a DTO para devolver
            RoomDTO roomDTO = Utils.mapRoomEntityToRoomDTO(savedRoom);

            // Configurar respuesta exitosa
            response.setRoom(roomDTO);
            response.setMessage("exitoso");
            response.setStatusCode(200);

        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error al guardar una habitación: " + e.getMessage());
        }

        return response;
    }

    /**
     * Obtiene todos los tipos de habitación distintos.
     * @return Lista de tipos de habitaciones
     */
    @Override
    public List<String> getAllRoomTypes() {
        return roomRepository.findDistinctRoomTypes();
    }

    /**
     * Obtiene todas las habitaciones registradas en la base de datos.
     * @return Response con lista de habitaciones
     */
    @Override
    public Response getAllRooms() {
        Response response = new Response();

        try {
            List<Room> roomList = roomRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
            List<RoomDTO> roomDTOList = Utils.mapRoomListEntityToRoomListDTO(roomList);

            response.setRoomList(roomDTOList);
            response.setMessage("exitoso");
            response.setStatusCode(200);

        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error al obtener todas las habitaciones: " + e.getMessage());
        }

        return response;
    }

    /**
     * Elimina una habitación por su ID.
     * @param roomId ID de la habitación
     * @return Response indicando el resultado
     */
    @Override
    public Response deleteRoom(Long roomId) {
        Response response = new Response();

        try {
            // Verificar que la habitación exista
            roomRepository.findById(roomId).orElseThrow(() -> new OurException("Habitación no encontrada"));

            // Eliminar habitación
            roomRepository.deleteById(roomId);

            response.setMessage("exitoso");
            response.setStatusCode(200);

        } catch (OurException e) {
            response.setStatusCode(404);
            response.setMessage(e.getMessage());

        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error al eliminar una habitación: " + e.getMessage());
        }

        return response;
    }

    /**
     * Actualiza los datos de una habitación.
     * @param roomId ID de la habitación a actualizar
     * @param description Nueva descripción (opcional)
     * @param roomType Nuevo tipo (opcional)
     * @param roomPrice Nuevo precio (opcional)
     * @param photo Nueva foto (opcional)
     * @return Response con la habitación actualizada
     */
    @Override
    public Response updateRoom(Long roomId, String description, String roomType, BigDecimal roomPrice, MultipartFile photo) {
        Response response = new Response();

        try {
            String imageUrl = null;

            // Si hay nueva foto, subirla y obtener URL
            if (photo != null && !photo.isEmpty()) {
                imageUrl = rStorageService.uploadImage(photo);
            }

            // Obtener habitación existente
            Room room = roomRepository.findById(roomId).orElseThrow(() -> new OurException("Habitación no encontrada"));

            // Actualizar campos si fueron proporcionados
            if (roomType != null) room.setRoomType(roomType);
            if (roomPrice != null) room.setRoomPrice(roomPrice);
            if (description != null) room.setRoomDescription(description);
            if (imageUrl != null) room.setRoomPhotoUrl(imageUrl);

            // Guardar cambios
            Room updatedRoom = roomRepository.save(room);

            // Mapear a DTO para devolver
            RoomDTO roomDTO = Utils.mapRoomEntityToRoomDTO(updatedRoom);

            response.setRoom(roomDTO);
            response.setMessage("exitoso");
            response.setStatusCode(200);

        } catch (OurException e) {
            response.setStatusCode(404);
            response.setMessage(e.getMessage());

        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error al actualizar una habitación: " + e.getMessage());
        }

        return response;
    }

    /**
     * Obtiene una habitación por su ID incluyendo sus reservas.
     * @param roomId ID de la habitación
     * @return Response con la habitación encontrada
     */
    @Override
    public Response getRoomById(Long roomId) {
        Response response = new Response();

        try {
            Room room = roomRepository.findById(roomId).orElseThrow(() -> new OurException("Habitación no encontrada"));
            RoomDTO roomDTO = Utils.mapRoomEntityToRoomDTOPlusBookings(room);

            response.setRoom(roomDTO);
            response.setMessage("exitoso");
            response.setStatusCode(200);

        } catch (OurException e) {
            response.setStatusCode(404);
            response.setMessage(e.getMessage());

        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error al obtener una habitación por Id: " + e.getMessage());
        }

        return response;
    }

    /**
     * Obtiene habitaciones disponibles según rango de fechas y tipo de habitación.
     * @param checkInDate Fecha de entrada
     * @param checkOutDate Fecha de salida
     * @param roomType Tipo de habitación
     * @return Response con lista de habitaciones disponibles
     */
    @Override
    public Response getAvailableRoomsByDateAndType(LocalDate checkInDate, LocalDate checkOutDate, String roomType) {
        Response response = new Response();

        try {
            List<Room> availableRooms = roomRepository.findAvailableRoomsByDateAndTypes(checkInDate, checkOutDate, roomType);
            List<RoomDTO> roomDTOList = Utils.mapRoomListEntityToRoomListDTO(availableRooms);

            response.setRoomList(roomDTOList);
            response.setMessage("exitoso");
            response.setStatusCode(200);

        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error al obtener habitaciones disponibles: " + e.getMessage());
        }

        return response;
    }

    /**
     * Obtiene todas las habitaciones que actualmente no tienen reservas.
     * @return Response con lista de habitaciones disponibles
     */
    @Override
    public Response getAllAvailableRooms() {
        Response response = new Response();

        try {
            List<Room> roomList = roomRepository.getAllAvailableRooms();
            List<RoomDTO> roomDTOList = Utils.mapRoomListEntityToRoomListDTO(roomList);

            response.setRoomList(roomDTOList);
            response.setMessage("exitoso");
            response.setStatusCode(200);

        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error al obtener habitaciones disponibles: " + e.getMessage());
        }

        return response;
    }
}
