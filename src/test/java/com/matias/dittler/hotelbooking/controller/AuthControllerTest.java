package com.matias.dittler.hotelbooking.controller;

import com.matias.dittler.hotelbooking.dto.LoginRequest;
import com.matias.dittler.hotelbooking.dto.Response;
import com.matias.dittler.hotelbooking.entity.User;
import com.matias.dittler.hotelbooking.service.interfac.InterfaceUserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Clase de test unitario para AuthController.
 * Usa Mockito para simular el servicio de usuarios (InterfaceUserService) y probar el comportamiento del controlador.
 */
@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    // Simula el servicio de usuario sin levantar toda la aplicación
    @Mock
    private InterfaceUserService userService;

    // Inyecta los mocks dentro del AuthController
    @InjectMocks
    private AuthController authController;

    /**
     * Prueba que el registro de un usuario funcione correctamente.
     */
    @Test
    void shouldRegisterUserSuccessfully() {

        // Arrange: preparar los datos de prueba
        User user = new User();
        user.setEmail("test@test.com");
        user.setPassword("1234");

        Response mockResponse = new Response();
        mockResponse.setStatusCode(201);
        mockResponse.setMessage("User registered successfully");

        // Configura Mockito para que devuelva mockResponse cuando se llame a userService.register
        when(userService.register(user)).thenReturn(mockResponse);

        // Act: ejecutar el método del controlador
        ResponseEntity<Response> responseEntity = authController.register(user);

        // Assert: verificar que el controlador devuelva lo esperado
        assertEquals(201, responseEntity.getStatusCodeValue());
        assertEquals("User registered successfully", responseEntity.getBody().getMessage());

        // Verifica que el método del servicio se haya llamado exactamente una vez
        verify(userService, times(1)).register(user);
    }

    /**
     * Prueba que el login de un usuario funcione correctamente.
     */
    @Test
    void shouldLoginUserSuccessfully() {

        // Arrange
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("test@test.com");
        loginRequest.setPassword("1234");

        Response mockResponse = new Response();
        mockResponse.setStatusCode(200);
        mockResponse.setMessage("Login successful");

        // Configura el mock
        when(userService.login(loginRequest)).thenReturn(mockResponse);

        // Act
        ResponseEntity<Response> responseEntity = authController.login(loginRequest);

        // Assert
        assertEquals(200, responseEntity.getStatusCodeValue());
        assertEquals("Login successful", responseEntity.getBody().getMessage());

        // Verifica que se haya llamado al servicio de login exactamente una vez
        verify(userService, times(1)).login(loginRequest);
    }
}
