package com.matias.dittler.hotelbooking.service.implementation;

import com.matias.dittler.hotelbooking.dto.LoginRequest;
import com.matias.dittler.hotelbooking.dto.Response;
import com.matias.dittler.hotelbooking.entity.User;
import com.matias.dittler.hotelbooking.repository.UserRepository;
import com.matias.dittler.hotelbooking.service.implementation.UserService;
import com.matias.dittler.hotelbooking.utils.JWTUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Tests unitarios para UserService
 *
 * Verifica los escenarios principales relacionados con usuarios:
 * - Registro de usuarios
 * - Login y generación de JWT
 * - Eliminación de usuarios
 */
@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JWTUtils jwtUtils;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private UserService userService;

    private User user;

    /**
     * Inicializa un usuario de ejemplo antes de cada test.
     */
    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setEmail("test@mail.com");
        user.setPassword("1234");
        user.setRole("USER");
    }

    /**
     * Testea registro exitoso de un usuario nuevo.
     * Verifica:
     * - Que no exista previamente el usuario
     * - Que la contraseña se encode correctamente
     * - Que se guarde en el repositorio
     */
    @Test
    void register_success() {
        when(userRepository.existsByEmail(user.getEmail())).thenReturn(false);
        when(passwordEncoder.encode(any())).thenReturn("encoded");
        when(userRepository.save(any(User.class))).thenReturn(user);

        Response response = userService.register(user);

        assertEquals(200, response.getStatusCode());
        assertEquals("exitoso", response.getMessage());
        verify(userRepository, times(1)).save(any(User.class));
    }

    /**
     * Testea intento de registro cuando el usuario ya existe.
     * Devuelve código 400.
     */
    @Test
    void register_userAlreadyExists() {
        when(userRepository.existsByEmail(user.getEmail())).thenReturn(true);

        Response response = userService.register(user);

        assertEquals(400, response.getStatusCode());
    }

    /**
     * Testea login exitoso de usuario existente.
     * Verifica:
     * - Generación del token JWT
     * - Código de respuesta 200
     */
    @Test
    void login_success() {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("test@mail.com");
        loginRequest.setPassword("1234");

        when(userRepository.findByEmail("test@mail.com")).thenReturn(Optional.of(user));
        when(jwtUtils.generateToken(user)).thenReturn("jwt-token");

        Response response = userService.login(loginRequest);

        assertEquals(200, response.getStatusCode());
        assertEquals("jwt-token", response.getToken());
    }

    /**
     * Testea login de usuario no encontrado.
     * Devuelve código 404.
     */
    @Test
    void login_userNotFound() {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("test@mail.com");
        loginRequest.setPassword("1234");

        when(userRepository.findByEmail("test@mail.com")).thenReturn(Optional.empty());

        Response response = userService.login(loginRequest);

        assertEquals(404, response.getStatusCode());
    }

    /**
     * Testea intento de eliminar usuario inexistente.
     * Devuelve código 404.
     */
    @Test
    void deleteUser_notFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        Response response = userService.deleteUser("1");

        assertEquals(404, response.getStatusCode());
    }

    /**
     * Testea eliminación exitosa de usuario existente.
     */
    @Test
    void deleteUser_success() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        Response response = userService.deleteUser("1");

        assertEquals(200, response.getStatusCode());
        verify(userRepository, times(1)).deleteById(1L);
    }
}
