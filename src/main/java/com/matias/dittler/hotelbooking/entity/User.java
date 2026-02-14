package com.matias.dittler.hotelbooking.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Entidad que representa un usuario del sistema.
 *
 * Está mapeada a la tabla "users" en la base de datos.
 * Implementa UserDetails para integrarse con Spring Security.
 */
@Data // Genera getters, setters, equals, hashCode y toString automáticamente
@Entity
@Table(name = "users")
public class User implements UserDetails {

    /**
     * Identificador único del usuario.
     * Se genera automáticamente con auto-incremento.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Correo electrónico del usuario.
     * Es obligatorio y único en la base de datos.
     */
    @NotBlank(message = "El e-mail es requerido")
    @Column(unique = true)
    private String email;

    /**
     * Nombre completo del usuario.
     * Es obligatorio.
     */
    @NotBlank(message = "El nombre es requerido")
    private String name;

    /**
     * Número de teléfono del usuario.
     * Es opcional.
     */
    private String phoneNumber;

    /**
     * Contraseña del usuario.
     * Es obligatoria.
     */
    @NotBlank(message = "La contraseña es requerida")
    private String password;

    /**
     * Rol del usuario.
     * Ej: "USER", "ADMIN".
     */
    private String role;

    /**
     * Relación uno-a-muchos con Booking.
     *
     * - mappedBy = "user" indica que la relación está definida
     *   en la entidad Booking.
     * - FetchType.LAZY: las reservas se cargan solo cuando se necesitan.
     * - CascadeType.ALL: operaciones sobre User se propagan a las reservas asociadas.
     */
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Booking> bookings = new ArrayList<>();


    // ================================
    // Implementación de UserDetails
    // ================================

    /**
     * Devuelve los roles del usuario como GrantedAuthority
     * para Spring Security.
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role));
    }

    /**
     * Devuelve el username utilizado por Spring Security
     * (en este caso, el email).
     */
    @Override
    public String getUsername() {
        return email;
    }

    /**
     * Indica si la cuenta está expirada.
     * Retorna true porque no implementamos expiración de cuentas.
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * Indica si la cuenta está bloqueada.
     * Retorna true porque no implementamos bloqueo de cuentas.
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     * Indica si las credenciales han expirado.
     * Retorna true porque no implementamos expiración de credenciales.
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * Indica si el usuario está habilitado.
     * Retorna true porque todos los usuarios creados están habilitados.
     */
    @Override
    public boolean isEnabled() {
        return true;
    }


    // ================================
    // toString personalizado
    // ================================
    /**
     * toString personalizado.
     * No incluye la lista de bookings para evitar:
     * - Recursión infinita
     * - Problemas de rendimiento
     */
    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", name='" + name + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", password='" + password + '\'' +
                ", role='" + role + '\'' +
                '}';
    }
}

