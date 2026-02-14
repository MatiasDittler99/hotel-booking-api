package com.matias.dittler.hotelbooking.controller;

import com.matias.dittler.hotelbooking.dto.Response;
import com.matias.dittler.hotelbooking.service.interfac.InterfaceBookingService;
import com.matias.dittler.hotelbooking.service.interfac.InterfaceRoomService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test unitario para RoomController usando Mockito.
 * Permite probar los endpoints del controlador sin depender del servicio real.
 */
@ExtendWith(MockitoExtension.class)
class RoomControllerTest {

    // Mock del servicio de habitaciones
    @Mock
    private InterfaceRoomService roomService;

    // Mock del servicio de reservas (por si algún endpoint lo usa)
    @Mock
    private InterfaceBookingService bookingService;

    // Inyecta los mocks en RoomController
    @InjectMocks
    private RoomController roomController;

    /**
     * Testea que se pueda agregar una nueva habitación correctamente.
     */
    @Test
    void shouldAddNewRoomSuccessfully() {

        MockMultipartFile photo =
                new MockMultipartFile("photo", "room.jpg",
                        "image/jpeg", "image".getBytes());

        Response mockResponse = new Response();
        mockResponse.setStatusCode(201);
        mockResponse.setMessage("Room added successfully");

        // Configura comportamiento simulado del servicio
        when(roomService.addNewRoom(any(), any(), any(), any()))
                .thenReturn(mockResponse);

        // Llamada al controlador
        ResponseEntity<Response> response =
                roomController.addNewRoom(photo, "DELUXE",
                        BigDecimal.valueOf(150), "Nice room");

        // Verificaciones
        assertEquals(201, response.getStatusCodeValue());
        assertEquals("Room added successfully",
                response.getBody().getMessage());

        verify(roomService, times(1))
                .addNewRoom(photo, "DELUXE", BigDecimal.valueOf(150), "Nice room");
    }

    /**
     * Testea obtener todas las habitaciones.
     */
    @Test
    void shouldReturnAllRooms() {
        Response mockResponse = new Response();
        mockResponse.setStatusCode(200);

        when(roomService.getAllRooms()).thenReturn(mockResponse);

        ResponseEntity<Response> response = roomController.getAllRooms();

        assertEquals(200, response.getStatusCodeValue());

        verify(roomService, times(1)).getAllRooms();
    }

    /**
     * Testea obtener los tipos de habitación.
     */
    @Test
    void shouldReturnRoomTypes() {
        when(roomService.getAllRoomTypes())
                .thenReturn(List.of("DELUXE", "STANDARD"));

        List<String> types = roomController.getRoomTypes();

        assertEquals(2, types.size());
        assertTrue(types.contains("DELUXE"));

        verify(roomService, times(1)).getAllRoomTypes();
    }

    /**
     * Testea obtener habitación por ID.
     */
    @Test
    void shouldReturnRoomById() {
        Response mockResponse = new Response();
        mockResponse.setStatusCode(200);

        when(roomService.getRoomById(1L)).thenReturn(mockResponse);

        ResponseEntity<Response> response = roomController.getRoomByID(1L);

        assertEquals(200, response.getStatusCodeValue());

        verify(roomService, times(1)).getRoomById(1L);
    }

    /**
     * Testea obtener habitaciones disponibles por fecha y tipo.
     */
    @Test
    void shouldReturnAvailableRoomsByDateAndType() {
        LocalDate checkIn = LocalDate.now();
        LocalDate checkOut = LocalDate.now().plusDays(2);

        Response mockResponse = new Response();
        mockResponse.setStatusCode(200);

        when(roomService.getAvailableRoomsByDateAndType(checkIn, checkOut, "DELUXE"))
                .thenReturn(mockResponse);

        ResponseEntity<Response> response =
                roomController.getAvailableRoomsByDateAndType(checkIn, checkOut, "DELUXE");

        assertEquals(200, response.getStatusCodeValue());

        verify(roomService, times(1))
                .getAvailableRoomsByDateAndType(checkIn, checkOut, "DELUXE");
    }

    /**
     * Testea eliminar habitación correctamente.
     */
    @Test
    void shouldDeleteRoom() {
        Response mockResponse = new Response();
        mockResponse.setStatusCode(200);

        when(roomService.deleteRoom(5L)).thenReturn(mockResponse);

        ResponseEntity<Response> response = roomController.deleteRoom(5L);

        assertEquals(200, response.getStatusCodeValue());

        verify(roomService, times(1)).deleteRoom(5L);
    }
}
