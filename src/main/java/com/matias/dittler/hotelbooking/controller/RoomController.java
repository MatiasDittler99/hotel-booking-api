package com.matias.dittler.hotelbooking.controller;

import com.matias.dittler.hotelbooking.dto.Response;
import com.matias.dittler.hotelbooking.service.interfac.InterfaceBookingService;
import com.matias.dittler.hotelbooking.service.interfac.InterfaceRoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * Controlador encargado de la gestión de habitaciones del hotel.
 *
 * Permite:
 * - Crear nuevas habitaciones (ADMIN)
 * - Actualizar o eliminar habitaciones (ADMIN)
 * - Consultar habitaciones
 * - Buscar disponibilidad por fecha y tipo
 *
 * Todas las rutas están bajo el prefijo: /rooms
 */
@RestController
@RequestMapping("/rooms")
public class RoomController {

    /**
     * Servicio que contiene la lógica de negocio relacionada a habitaciones.
     */
    @Autowired
    private InterfaceRoomService roomService;

    /**
     * Servicio de reservas, utilizado indirectamente para validaciones
     * relacionadas con disponibilidad.
     */
    @Autowired
    private InterfaceBookingService bookingService;

    /**
     * Crea una nueva habitación en el sistema.
     *
     * Solo accesible por usuarios con rol ADMIN.
     *
     * Permite subir una imagen opcional junto con los datos de la habitación.
     *
     * URL: POST /rooms/add
     *
     * @param photo Imagen de la habitación (opcional)
     * @param roomType Tipo de habitación (ej: Suite, Single, Double)
     * @param roomPrice Precio por noche
     * @param roomDescription Descripción opcional
     * @return Resultado de la operación
     */
    @PostMapping("/add")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Response> addNewRoom(
            @RequestParam(value = "photo", required = false) MultipartFile photo,
            @RequestParam(value = "roomType", required = false) String roomType,
            @RequestParam(value = "roomPrice", required = false) BigDecimal roomPrice,
            @RequestParam(value = "roomDescription", required = false) String roomDescription
    ) {

        // Validación básica de campos obligatorios
        if (photo == null || photo.isEmpty() || roomType == null || roomType.isBlank() || roomPrice == null) {
            Response response = new Response();
            response.setStatusCode(400);
            response.setMessage("Proporcione valores para todos los campos (foto, tipo de habitación, precio de la habitación)");
        }

        Response response = roomService.addNewRoom(photo, roomType, roomPrice, roomDescription);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    /**
     * Obtiene todas las habitaciones registradas.
     *
     * URL: GET /rooms/all
     *
     * @return Lista completa de habitaciones
     */
    @GetMapping("/all")
    public ResponseEntity<Response> getAllRooms() {
        Response response = roomService.getAllRooms();
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    /**
     * Obtiene la lista de todos los tipos de habitación disponibles.
     *
     * URL: GET /rooms/types
     *
     * @return Lista de tipos de habitación
     */
    @GetMapping("/types")
    public List<String> getRoomTypes() {
        return roomService.getAllRoomTypes();
    }

    /**
     * Obtiene la información de una habitación específica por su ID.
     *
     * URL: GET /rooms/room-by-id/{roomId}
     *
     * @param roomId ID de la habitación
     * @return Datos detallados de la habitación
     */
    @GetMapping("/room-by-id/{roomId}")
    public ResponseEntity<Response> getRoomByID(@PathVariable("roomId") Long roomId) {
        Response response = roomService.getRoomById(roomId);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    /**
     * Obtiene todas las habitaciones actualmente disponibles.
     *
     * URL: GET /rooms/all-available-rooms
     *
     * @return Lista de habitaciones disponibles
     */
    @GetMapping("/all-available-rooms")
    public ResponseEntity<Response> getAvailableRooms() {
        Response response = roomService.getAllAvailableRooms();
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    /**
     * Busca habitaciones disponibles según:
     * - Fecha de entrada
     * - Fecha de salida
     * - Tipo de habitación
     *
     * URL: GET /rooms/available-rooms-by-date-and-type
     *
     * Ejemplo:
     * /rooms/available-rooms-by-date-and-type?checkInDate=2026-06-07&checkOutDate=2026-06-09&roomType=Suite
     *
     * @param checkInDate Fecha de entrada
     * @param checkOutDate Fecha de salida
     * @param roomType Tipo de habitación
     * @return Lista de habitaciones que cumplen los criterios
     */
    @GetMapping("/available-rooms-by-date-and-type")
    public ResponseEntity<Response> getAvailableRoomsByDateAndType(
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate checkInDate,

            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate checkOutDate,

            @RequestParam(required = false)
            String roomType
    ) {

        // Validación básica de parámetros obligatorios
        if (checkInDate == null || checkOutDate == null || roomType == null || roomType.isBlank()) {
            Response response = new Response();
            response.setStatusCode(400);
            response.setMessage("Todos los campos son obligatorios (fecha de entrada, fecha de salida, tipo de habitación)");
        }

        Response response = roomService.getAvailableRoomsByDateAndType(checkInDate, checkOutDate, roomType);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    /**
     * Actualiza los datos de una habitación existente.
     *
     * Solo accesible por ADMIN.
     *
     * URL: PUT /rooms/update/{roomId}
     *
     * @param roomId ID de la habitación
     * @param photo Nueva imagen (opcional)
     * @param roomType Nuevo tipo (opcional)
     * @param roomPrice Nuevo precio (opcional)
     * @param roomDescription Nueva descripción (opcional)
     * @return Resultado de la actualización
     */
    @PutMapping("/update/{roomId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Response> updateRoom(
            @PathVariable Long roomId,
            @RequestParam(value = "photo", required = false) MultipartFile photo,
            @RequestParam(value = "roomType", required = false) String roomType,
            @RequestParam(value = "roomPrice", required = false) BigDecimal roomPrice,
            @RequestParam(value = "roomDescription", required = false) String roomDescription) {

        Response response = roomService.updateRoom(roomId, roomDescription, roomType, roomPrice, photo);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    /**
     * Elimina una habitación del sistema.
     *
     * Solo accesible por ADMIN.
     *
     * URL: DELETE /rooms/delete/{roomId}
     *
     * @param roomId ID de la habitación a eliminar
     * @return Resultado de la operación
     */
    @DeleteMapping("/delete/{roomId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Response> deleteRoom(@PathVariable Long roomId) {
        Response response = roomService.deleteRoom(roomId);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }
}
