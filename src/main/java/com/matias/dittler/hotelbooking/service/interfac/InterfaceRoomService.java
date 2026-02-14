package com.matias.dittler.hotelbooking.service.interfac;

import com.matias.dittler.hotelbooking.dto.Response;
import org.springframework.web.multipart.MultipartFile;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * Interface que define los métodos del servicio de habitaciones (RoomService).
 * Cada método devuelve un objeto Response con el resultado de la operación.
 */
public interface InterfaceRoomService {

    /**
     * Agrega una nueva habitación al sistema.
     * @param photo Imagen de la habitación (opcional)
     * @param roomType Tipo de habitación (ej. Suite, Doble)
     * @param roomPrice Precio de la habitación
     * @param description Descripción de la habitación
     * @return Response con estado, mensaje y datos de la habitación creada
     */
    Response addNewRoom(MultipartFile photo, String roomType, BigDecimal roomPrice, String description);

    /**
     * Obtiene todos los tipos de habitación disponibles.
     * @return Lista de strings con los nombres de los tipos de habitación
     */
    List<String> getAllRoomTypes();

    /**
     * Obtiene todas las habitaciones registradas en el sistema.
     * @return Response con estado, mensaje y lista de habitaciones
     */
    Response getAllRooms();

    /**
     * Elimina una habitación por su ID.
     * @param roomId ID de la habitación a eliminar
     * @return Response con estado y mensaje de la operación
     */
    Response deleteRoom(Long roomId);

    /**
     * Actualiza los datos de una habitación existente.
     * @param roomId ID de la habitación a actualizar
     * @param description Nueva descripción (opcional)
     * @param roomType Nuevo tipo de habitación (opcional)
     * @param roomPrice Nuevo precio (opcional)
     * @param photo Nueva imagen (opcional)
     * @return Response con estado, mensaje y datos de la habitación actualizada
     */
    Response updateRoom(Long roomId, String description, String roomType, BigDecimal roomPrice, MultipartFile photo);

    /**
     * Obtiene los datos de una habitación por su ID.
     * @param roomId ID de la habitación
     * @return Response con estado, mensaje y datos de la habitación
     */
    Response getRoomById(Long roomId);

    /**
     * Obtiene las habitaciones disponibles según fechas y tipo.
     * @param checkInDate Fecha de entrada
     * @param checkOutDate Fecha de salida
     * @param roomType Tipo de habitación
     * @return Response con estado, mensaje y lista de habitaciones disponibles
     */
    Response getAvailableRoomsByDateAndType(LocalDate checkInDate, LocalDate checkOutDate, String roomType);

    /**
     * Obtiene todas las habitaciones disponibles sin filtrar por fecha.
     * @return Response con estado, mensaje y lista de habitaciones disponibles
     */
    Response getAllAvailableRooms();
    
}
