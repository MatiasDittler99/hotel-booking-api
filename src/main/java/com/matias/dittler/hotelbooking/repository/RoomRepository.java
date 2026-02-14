package com.matias.dittler.hotelbooking.repository;

import com.matias.dittler.hotelbooking.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.time.LocalDate;
import java.util.List;

/**
 * Repositorio para la entidad Room.
 *
 * Extiende JpaRepository para obtener métodos CRUD básicos:
 * - save()
 * - findById()
 * - findAll()
 * - deleteById()
 *
 * Además, incluye consultas personalizadas mediante JPQL.
 */
public interface RoomRepository extends JpaRepository<Room, Long> {

    /**
     * Obtiene todos los tipos de habitación distintos disponibles en la base de datos.
     *
     * @return Lista de nombres de tipos de habitación únicos
     */
    @Query("SELECT DISTINCT r.roomType FROM Room r")
    List<String> findDistinctRoomTypes();

    /**
     * Obtiene todas las habitaciones que actualmente no están reservadas.
     *
     * @return Lista de habitaciones disponibles
     */
    @Query("SELECT r FROM Room r WHERE r.id NOT IN (SELECT b.room.id FROM Booking b)")
    List<Room> getAllAvailableRooms();

    /**
     * Obtiene habitaciones disponibles por rango de fechas y tipo de habitación.
     *
     * La consulta verifica que la habitación no tenga reservas que
     * se solapen con las fechas indicadas.
     *
     * @param checkInDate Fecha de entrada deseada
     * @param checkOutDate Fecha de salida deseada
     * @param roomType Tipo de habitación buscada (puede ser parcial)
     * @return Lista de habitaciones disponibles que cumplen con los criterios
     */
    @Query("SELECT r FROM Room r WHERE r.roomType LIKE %:roomType% AND r.id NOT IN " +
           "(SELECT bk.room.id FROM Booking bk WHERE " +
           "(bk.checkInDate <= :checkOutDate) AND (bk.checkOutDate >= :checkInDate))")
    List<Room> findAvailableRoomsByDateAndTypes(LocalDate checkInDate, LocalDate checkOutDate, String roomType);

}
