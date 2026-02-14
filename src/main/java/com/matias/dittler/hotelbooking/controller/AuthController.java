package com.matias.dittler.hotelbooking.controller;

import com.matias.dittler.hotelbooking.dto.LoginRequest;
import com.matias.dittler.hotelbooking.dto.Response;
import com.matias.dittler.hotelbooking.entity.User;
import com.matias.dittler.hotelbooking.service.interfac.InterfaceUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controlador encargado de manejar la autenticación de usuarios.
 * 
 * Expone endpoints para:
 * - Registro de nuevos usuarios
 * - Inicio de sesión (login)
 * 
 * Todas las rutas están bajo el prefijo: /auth
 */
@RestController
@RequestMapping("/auth")
public class AuthController {

    /**
     * Servicio que contiene la lógica de negocio relacionada a usuarios
     * (registro, autenticación, validaciones, generación de tokens, etc.).
     */
    @Autowired
    private InterfaceUserService userService;

    /**
     * Endpoint para registrar un nuevo usuario en el sistema.
     *
     * Recibe un objeto User en el body de la petición,
     * lo envía al servicio para su validación y persistencia,
     * y devuelve una respuesta estandarizada.
     *
     * URL: POST /auth/register
     *
     * @param user Datos del usuario a registrar
     * @return ResponseEntity con el código de estado y el cuerpo de la respuesta
     */
    @PostMapping("/register")
    public ResponseEntity<Response> register(@RequestBody User user){
        Response response = userService.register(user);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    /**
     * Endpoint para autenticar un usuario existente.
     *
     * Recibe las credenciales (email/username y password)
     * dentro de un objeto LoginRequest.
     *
     * Si las credenciales son válidas, el servicio devuelve
     * una respuesta que puede incluir un token JWT u otra
     * información de autenticación.
     *
     * URL: POST /auth/login
     *
     * @param loginRequest Objeto que contiene las credenciales del usuario
     * @return ResponseEntity con el código HTTP correspondiente y la respuesta
     */
    @PostMapping("/login")
    public ResponseEntity<Response> login(@RequestBody LoginRequest loginRequest){
        Response response = userService.login(loginRequest);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }
}
