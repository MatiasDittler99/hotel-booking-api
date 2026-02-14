package com.matias.dittler.hotelbooking.service.implementation;

import com.matias.dittler.hotelbooking.dto.LoginRequest;
import com.matias.dittler.hotelbooking.dto.Response;
import com.matias.dittler.hotelbooking.dto.UserDTO;
import com.matias.dittler.hotelbooking.entity.User;
import com.matias.dittler.hotelbooking.exception.OurException;
import com.matias.dittler.hotelbooking.repository.UserRepository;
import com.matias.dittler.hotelbooking.service.interfac.InterfaceUserService;
import com.matias.dittler.hotelbooking.utils.JWTUtils;
import com.matias.dittler.hotelbooking.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * Servicio que implementa la lógica de negocio relacionada con los usuarios.
 * Implementa InterfaceUserService.
 */
@Service
public class UserService implements InterfaceUserService {

    @Autowired
    private UserRepository userRepository; // Repositorio para interactuar con la tabla de usuarios

    @Autowired
    private PasswordEncoder passwordEncoder; // Para encriptar contraseñas

    @Autowired
    private JWTUtils jwtUtils; // Para generar y validar tokens JWT

    @Autowired
    private AuthenticationManager authenticationManager; // Para autenticar usuarios

    /**
     * Registra un nuevo usuario en el sistema.
     * @param user Usuario con datos a registrar
     * @return Response con el estado y datos del usuario creado
     */
    @Override
    public Response register(User user) {
        Response response = new Response();

        try {
            // Si no tiene rol, se asigna por defecto "USER"
            if (user.getRole() == null || user.getRole().isBlank()) {
                user.setRole("USER");
            }

            // Verifica si ya existe un usuario con el mismo email
            if (userRepository.existsByEmail(user.getEmail())) {
                throw new OurException(user.getEmail() + " Ya existe");
            }

            // Encriptar contraseña antes de guardar
            user.setPassword(passwordEncoder.encode(user.getPassword()));

            // Guardar usuario en base de datos
            User savedUser = userRepository.save(user);

            // Mapear a DTO para devolver información segura (sin password)
            UserDTO userDTO = Utils.mapUserEntityToUserDTO(savedUser);

            response.setStatusCode(200);
            response.setUser(userDTO);
            response.setMessage("exitoso");

        } catch (OurException e) {
            response.setStatusCode(400); // Error de validación
            response.setMessage(e.getMessage());

        } catch (Exception e) {
            response.setStatusCode(500); // Error interno
            response.setMessage("Error al guardar un usuario: " + e.getMessage());
        }

        return response;
    }

    /**
     * Realiza el login de un usuario y devuelve token JWT.
     * @param loginRequest Objeto con email y password
     * @return Response con token, rol y expiración
     */
    @Override
    public Response login(LoginRequest loginRequest) {
        Response response = new Response();

        try {
            // Autenticar con Spring Security
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getEmail(), loginRequest.getPassword())
            );

            // Obtener usuario de la base de datos
            var user = userRepository.findByEmail(loginRequest.getEmail())
                    .orElseThrow(() -> new OurException("Usuario no encontrado"));

            // Generar token JWT
            var token = jwtUtils.generateToken(user);

            response.setToken(token);
            response.setExpirationTime("7 dias"); // Tiempo de expiración visible
            response.setRole(user.getRole());
            response.setMessage("exitoso");
            response.setStatusCode(200);

        } catch (OurException e) {
            response.setStatusCode(404); // Usuario no encontrado
            response.setMessage(e.getMessage());

        } catch (Exception e) {
            response.setStatusCode(500); // Error interno
            response.setMessage("Error al iniciar sesión: " + e.getMessage());
        }

        return response;
    }

    /**
     * Obtiene todos los usuarios registrados.
     * @return Response con lista de usuarios
     */
    @Override
    public Response getAllUsers() {
        Response response = new Response();

        try {
            List<User> userList = userRepository.findAll();
            List<UserDTO> userDTOList = Utils.mapUserListEntityToUserListDTO(userList);

            response.setUserList(userDTOList);
            response.setMessage("exitoso");
            response.setStatusCode(200);

        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error al obtener todos los usuarios: " + e.getMessage());
        }

        return response;
    }

    /**
     * Obtiene el historial de reservas de un usuario.
     * @param userId ID del usuario
     * @return Response con usuario y sus reservas
     */
    @Override
    public Response getUSerBookingHistory(String userId) {
        Response response = new Response();

        try {
            User user = userRepository.findById(Long.valueOf(userId))
                    .orElseThrow(() -> new OurException("Usuario no encontrado"));

            UserDTO userDTO = Utils.mapUserEntityToUserDTOPlusUserBookingsAndRoom(user);

            response.setUser(userDTO);
            response.setMessage("exitoso");
            response.setStatusCode(200);

        } catch (OurException e) {
            response.setStatusCode(404);
            response.setMessage(e.getMessage());

        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error al obtener reservas de usuarios: " + e.getMessage());
        }

        return response;
    }

    /**
     * Elimina un usuario por su ID.
     * @param userId ID del usuario a eliminar
     * @return Response con estado de operación
     */
    @Override
    public Response deleteUser(String userId) {
        Response response = new Response();

        try {
            // Verifica existencia del usuario
            userRepository.findById(Long.valueOf(userId))
                    .orElseThrow(() -> new OurException("Usuario no encontrado"));

            // Elimina usuario
            userRepository.deleteById(Long.valueOf(userId));

            response.setMessage("exitoso");
            response.setStatusCode(200);

        } catch (OurException e) {
            response.setStatusCode(404);
            response.setMessage(e.getMessage());

        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error al eliminar un usuario: " + e.getMessage());
        }

        return response;
    }

    /**
     * Obtiene un usuario por su ID.
     * @param userId ID del usuario
     * @return Response con datos del usuario
     */
    @Override
    public Response getUserById(String userId) {
        Response response = new Response();

        try {
            User user = userRepository.findById(Long.valueOf(userId))
                    .orElseThrow(() -> new OurException("Usuario no encontrado"));

            UserDTO userDTO = Utils.mapUserEntityToUserDTO(user);

            response.setUser(userDTO);
            response.setMessage("exitoso");
            response.setStatusCode(200);

        } catch (OurException e) {
            response.setStatusCode(404);
            response.setMessage(e.getMessage());

        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error al obtener un usuario por id: " + e.getMessage());
        }

        return response;
    }

    /**
     * Obtiene información del usuario logueado por su email.
     * @param email Email del usuario
     * @return Response con datos del usuario
     */
    @Override
    public Response getMyInfo(String email) {
        Response response = new Response();

        try {
            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new OurException("Usuario no encontrado"));

            UserDTO userDTO = Utils.mapUserEntityToUserDTO(user);

            response.setUser(userDTO);
            response.setMessage("exitoso");
            response.setStatusCode(200);

        } catch (OurException e) {
            response.setStatusCode(404);
            response.setMessage(e.getMessage());

        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error al obtener información de usuario: " + e.getMessage());
        }

        return response;
    }
}
