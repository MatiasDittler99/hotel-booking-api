package com.matias.dittler.hotelbooking.controller;

import com.matias.dittler.hotelbooking.dto.Response;
import com.matias.dittler.hotelbooking.service.interfac.InterfaceUserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test unitario para UserController usando Mockito.
 * Se prueban los endpoints de usuario simulando el servicio subyacente.
 */
@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    // Mock del servicio de usuario
    @Mock
    private InterfaceUserService userService;

    // Inyecta los mocks en UserController
    @InjectMocks
    private UserController userController;

    /**
     * Antes de cada test, limpia el SecurityContext.
     * Esto evita que la autenticación de tests anteriores afecte al siguiente.
     */
    @BeforeEach
    void clearSecurityContext() {
        SecurityContextHolder.clearContext();
    }

    /**
     * Testea que se puedan obtener todos los usuarios correctamente.
     */
    @Test
    void shouldReturnAllUsers() {

        // Arrange: Configura respuesta simulada del servicio
        Response mockResponse = new Response();
        mockResponse.setStatusCode(200);
        when(userService.getAllUsers()).thenReturn(mockResponse);

        // Act: Llamada al endpoint del controlador
        ResponseEntity<Response> response = userController.getAllUsers();

        // Assert: Verifica código de respuesta y que el servicio fue llamado una vez
        assertEquals(200, response.getStatusCodeValue());
        verify(userService, times(1)).getAllUsers();
    }

    /**
     * Testea que se pueda obtener un usuario por ID correctamente.
     */
    @Test
    void shouldReturnUserById() {

        Response mockResponse = new Response();
        mockResponse.setStatusCode(200);

        when(userService.getUserById("1")).thenReturn(mockResponse);

        ResponseEntity<Response> response = userController.getUserById("1");

        assertEquals(200, response.getStatusCodeValue());
        verify(userService, times(1)).getUserById("1");
    }

    /**
     * Testea que se pueda eliminar un usuario correctamente.
     */
    @Test
    void shouldDeleteUser() {

        Response mockResponse = new Response();
        mockResponse.setStatusCode(200);

        when(userService.deleteUser("1")).thenReturn(mockResponse);

        ResponseEntity<Response> response = userController.deleteUser("1");

        assertEquals(200, response.getStatusCodeValue());
        verify(userService, times(1)).deleteUser("1");
    }

    /**
     * Testea que se pueda obtener el perfil del usuario logueado.
     * Simula el SecurityContext y Authentication de Spring Security.
     */
    @Test
    void shouldReturnLoggedInUserProfile() {

        // Mock de Authentication
        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn("test@test.com");

        // Se establece la autenticación en el SecurityContext
        SecurityContextHolder.getContext().setAuthentication(authentication);

        Response mockResponse = new Response();
        mockResponse.setStatusCode(200);

        // Configura comportamiento simulado del servicio
        when(userService.getMyInfo("test@test.com")).thenReturn(mockResponse);

        // Llamada al endpoint
        ResponseEntity<Response> response = userController.getLoggedInUSerProfile();

        assertEquals(200, response.getStatusCodeValue());
        verify(userService, times(1)).getMyInfo("test@test.com");
    }

    /**
     * Testea que se pueda obtener el historial de reservas de un usuario.
     */
    @Test
    void shouldReturnUserBookingHistory() {

        Response mockResponse = new Response();
        mockResponse.setStatusCode(200);

        when(userService.getUSerBookingHistory("1")).thenReturn(mockResponse);

        ResponseEntity<Response> response = userController.getUSerBookingHistory("1");

        assertEquals(200, response.getStatusCodeValue());
        verify(userService, times(1)).getUSerBookingHistory("1");
    }
}
