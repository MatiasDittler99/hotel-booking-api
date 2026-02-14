package com.matias.dittler.hotelbooking.service.implementation;

import com.matias.dittler.hotelbooking.entity.Room;
import com.matias.dittler.hotelbooking.repository.BookingRepository;
import com.matias.dittler.hotelbooking.repository.RoomRepository;
import com.matias.dittler.hotelbooking.service.R2StorageService;
import com.matias.dittler.hotelbooking.dto.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Tests unitarios para RoomService usando Mockito para simular dependencias externas.
 * 
 * Objetivos:
 * - Probar la lógica de RoomService sin depender de la base de datos real.
 * - Simular el comportamiento de R2StorageService y los repositorios.
 */
@ExtendWith(MockitoExtension.class) // Habilita la extensión de Mockito para JUnit 5
class RoomServiceTest {

    // Simulamos RoomRepository para no tocar la base de datos real
    @Mock
    private RoomRepository roomRepository;

    // Simulamos BookingRepository en caso de que RoomService dependa de él
    @Mock
    private BookingRepository bookingRepository;

    // Simulamos R2StorageService para no subir imágenes reales
    @Mock
    private R2StorageService rStorageService;

    // Inyectamos los mocks dentro de RoomService
    @InjectMocks
    private RoomService roomService;

    // Objeto Room de prueba
    private Room room;

    /**
     * Se ejecuta antes de cada test para inicializar objetos.
     */
    @BeforeEach
    void setUp() {
        room = new Room();
        room.setId(1L);
        room.setRoomType("DELUXE");
        room.setRoomPrice(BigDecimal.valueOf(200));
    }

    /**
     * Test que verifica la creación de una nueva habitación.
     * - Se simula la subida de la imagen y la persistencia en la base de datos.
     * - Se verifica que se llame al método save del repositorio exactamente 1 vez.
     */
    @Test
    void shouldAddNewRoomSuccessfully() {
        MultipartFile file = new MockMultipartFile(
                "photo", "room.jpg", "image/jpeg", "test".getBytes()
        );

        // Simulamos la subida de la imagen
        when(rStorageService.uploadImage(file)).thenReturn("https://fake-bucket.test/images/room.jpg");
        // Simulamos que la habitación se guarda y devuelve el objeto
        when(roomRepository.save(any(Room.class))).thenReturn(room);

        Response response = roomService.addNewRoom(file, "DELUXE", BigDecimal.valueOf(200), "Nice room");

        assertEquals(200, response.getStatusCode());
        verify(roomRepository, times(1)).save(any(Room.class));
    }

    /**
     * Test que verifica que se devuelve 404 al intentar borrar una habitación inexistente.
     */
    @Test
    void shouldReturn404WhenDeletingNonExistingRoom() {
        when(roomRepository.findById(1L)).thenReturn(Optional.empty());

        Response response = roomService.deleteRoom(1L);

        assertEquals(404, response.getStatusCode());
    }

    /**
     * Test que verifica la eliminación exitosa de una habitación existente.
     * - Se asegura que el método deleteById sea llamado exactamente 1 vez.
     */
    @Test
    void shouldDeleteRoomSuccessfully() {
        when(roomRepository.findById(1L)).thenReturn(Optional.of(room));

        Response response = roomService.deleteRoom(1L);

        assertEquals(200, response.getStatusCode());
        verify(roomRepository, times(1)).deleteById(1L);
    }

    /**
     * Test que verifica la actualización de una habitación existente.
     * - Se simula la subida de una nueva imagen y la persistencia de cambios.
     */
    @Test
    void shouldUpdateRoomSuccessfully() {
        MultipartFile file = new MockMultipartFile(
                "photo", "room.jpg", "image/jpeg", "test".getBytes()
        );

        when(roomRepository.findById(1L)).thenReturn(Optional.of(room));
        when(rStorageService.uploadImage(file)).thenReturn("https://fake-bucket.test/images/newroom.jpg");
        when(roomRepository.save(any(Room.class))).thenReturn(room);

        Response response = roomService.updateRoom(1L, "Updated description", "UPDATED",
                BigDecimal.valueOf(300), file);

        assertEquals(200, response.getStatusCode());
        verify(roomRepository, times(1)).save(any(Room.class));
    }

    /**
     * Test que verifica que se devuelve 404 al intentar actualizar una habitación inexistente.
     */
    @Test
    void shouldReturn404WhenUpdatingNonExistingRoom() {
        when(roomRepository.findById(1L)).thenReturn(Optional.empty());

        Response response = roomService.updateRoom(1L, "Desc", "Type", BigDecimal.valueOf(100), null);

        assertEquals(404, response.getStatusCode());
    }

    /**
     * Test que verifica que se devuelve 404 al buscar una habitación inexistente por ID.
     */
    @Test
    void shouldReturn404WhenRoomNotFoundById() {
        when(roomRepository.findById(1L)).thenReturn(Optional.empty());

        Response response = roomService.getRoomById(1L);

        assertEquals(404, response.getStatusCode());
    }
}
