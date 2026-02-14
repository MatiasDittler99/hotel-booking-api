package com.matias.dittler.hotelbooking.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * DTO (Data Transfer Object) utilizado para recibir
 * los datos de autenticación (login) desde el cliente.
 * 
 * Esta clase se usa principalmente en el endpoint /auth/login
 * para capturar el email y la contraseña enviados en el body.
 */
@Data // Lombok genera automáticamente getters, setters, toString, equals y hashCode
public class LoginRequest {

    /**
     * Email del usuario.
     * 
     * @NotBlank valida que el campo no sea null,
     * vacío o solo espacios en blanco.
     */
    @NotBlank(message = "El e-mail es requerido")
    private String email;

    /**
     * Contraseña del usuario.
     * 
     * @NotBlank asegura que el campo no esté vacío
     * antes de procesar la autenticación.
     */
    @NotBlank(message = "La contraseña es requireda")
    private String password;
}

