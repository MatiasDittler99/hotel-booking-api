package com.matias.dittler.hotelbooking.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginRequest {

    @NotBlank(message = "El e-mail es requerido")
    private String email;
    @NotBlank(message = "La contraseña es requireda")
    private String password;
}

