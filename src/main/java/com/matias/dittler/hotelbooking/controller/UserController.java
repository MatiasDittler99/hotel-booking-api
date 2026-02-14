package com.matias.dittler.hotelbooking.controller;

import com.matias.dittler.hotelbooking.dto.Response;
import com.matias.dittler.hotelbooking.service.interfac.InterfaceUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

/**
 * Controlador REST encargado de gestionar las operaciones relacionadas a los usuarios.
 * 
 * Expone endpoints para:
 * - Obtener todos los usuarios (solo ADMIN)
 * - Obtener usuario por ID
 * - Eliminar usuario (solo ADMIN)
 * - Obtener perfil del usuario autenticado
 * - Obtener historial de reservas de un usuario
 * 
 * La lógica de negocio es delegada completamente al servicio (InterfaceUserService).
 */
@RestController
@RequestMapping("/users") // Prefijo base para todos los endpoints del controlador
public class UserController {

    /**
     * Servicio de usuario inyectado por Spring.
     * Se encarga de contener la lógica de negocio.
     */
    @Autowired
    private InterfaceUserService userService;

    /**
     * Obtiene la lista completa de usuarios.
     * 
     * Solo accesible por usuarios con rol ADMIN.
     * 
     * @return ResponseEntity con la lista de usuarios y código HTTP dinámico.
     */
    @GetMapping("/all")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Response> getAllUsers(){
        Response response = userService.getAllUsers();
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    /**
     * Obtiene un usuario específico por su ID.
     * 
     * @param userId ID del usuario recibido por PathVariable
     * @return ResponseEntity con la información del usuario
     */
    @GetMapping("/get-by-id/{userId}")
    public ResponseEntity<Response> getUserById(@PathVariable("userId") String userId){
        Response response = userService.getUserById(userId);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    /**
     * Elimina un usuario por su ID.
     * 
     * Solo accesible por usuarios con rol ADMIN.
     * 
     * @param userId ID del usuario a eliminar
     * @return ResponseEntity con el resultado de la operación
     */
    @DeleteMapping("/delete/{userId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Response> deleteUser(@PathVariable("userId") String userId){
        Response response = userService.deleteUser(userId);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    /**
     * Obtiene la información del usuario actualmente autenticado.
     * 
     * Se obtiene el email desde el contexto de seguridad (SecurityContextHolder),
     * el cual es cargado previamente por el filtro JWT.
     * 
     * @return ResponseEntity con la información del usuario logueado
     */
    @GetMapping("/get-logged-in-profile-info")
    public ResponseEntity<Response> getLoggedInUSerProfile(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName(); // El email se usa como identificador único
        Response response = userService.getMyInfo(email);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    /**
     * Obtiene el historial de reservas de un usuario específico.
     * 
     * @param userId ID del usuario
     * @return ResponseEntity con la lista de reservas asociadas
     */
    @GetMapping("/get-user-bookings/{userId}")
    public ResponseEntity<Response> getUSerBookingHistory(@PathVariable("userId") String userId){
        Response response = userService.getUSerBookingHistory(userId);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }
}
