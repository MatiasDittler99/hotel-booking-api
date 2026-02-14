package com.matias.dittler.hotelbooking.service.interfac;

import com.matias.dittler.hotelbooking.dto.LoginRequest;
import com.matias.dittler.hotelbooking.dto.Response;
import com.matias.dittler.hotelbooking.entity.User;

/**
 * Interface que define los métodos del servicio de usuarios (UserService).
 * Cada método devuelve un objeto Response con el resultado de la operación.
 */
public interface InterfaceUserService {

    /**
     * Registra un nuevo usuario en el sistema.
     * @param user Objeto User con los datos del usuario a registrar
     * @return Response con estado, mensaje y datos del usuario creado
     */
    Response register(User user);

    /**
     * Autentica un usuario y genera un token JWT.
     * @param loginRequest Objeto LoginRequest con email y contraseña
     * @return Response con estado, mensaje, token y rol del usuario
     */
    Response login(LoginRequest loginRequest);

    /**
     * Obtiene todos los usuarios registrados en el sistema.
     * @return Response con estado, mensaje y lista de usuarios
     */
    Response getAllUsers();

    /**
     * Obtiene el historial de reservas de un usuario por su ID.
     * @param userId ID del usuario
     * @return Response con estado, mensaje y datos del usuario incluyendo reservas
     */
    Response getUSerBookingHistory(String userId);

    /**
     * Elimina un usuario por su ID.
     * @param userId ID del usuario a eliminar
     * @return Response con estado y mensaje de la operación
     */
    Response deleteUser(String userId);

    /**
     * Obtiene los datos de un usuario por su ID.
     * @param userId ID del usuario
     * @return Response con estado, mensaje y datos del usuario
     */
    Response getUserById(String userId);

    /**
     * Obtiene la información del usuario autenticado según su email.
     * @param email Email del usuario
     * @return Response con estado, mensaje y datos del usuario
     */
    Response getMyInfo(String email);
    
}
