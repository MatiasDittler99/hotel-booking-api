package com.matias.dittler.hotelbooking.service.implementation;

import com.matias.dittler.hotelbooking.entity.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

/**
 * Implementación personalizada de UserDetails para Spring Security.
 *
 * Propósito:
 * - Encapsular la entidad User para exponer solo la información necesaria
 *   a Spring Security (username, password, roles, estado de la cuenta).
 * - Proveer las autoridades (roles) del usuario para la autorización.
 *
 * Buenas prácticas aplicadas:
 * - Se mantiene la inmutabilidad del usuario (atributo final).
 * - Se implementa solo lo necesario para Spring Security.
 * - Roles se exponen mediante GrantedAuthority para integrarse con mecanismos de autorización.
 */
public class CustomUserDetails implements UserDetails {

    /**
     * Entidad de usuario subyacente.
     * Se mantiene como final para asegurar inmutabilidad.
     */
    private final User user;

    /**
     * Constructor.
     *
     * @param user Entidad User a encapsular.
     */
    public CustomUserDetails(User user) {
        this.user = user;
    }

    /**
     * Obtiene las autoridades (roles) del usuario.
     *
     * @return Lista de GrantedAuthority basada en el rol del usuario.
     *         Por ejemplo: "ADMIN" o "USER".
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(user.getRole()));
    }

    /**
     * Obtiene la contraseña del usuario.
     *
     * @return contraseña cifrada del usuario.
     */
    @Override
    public String getPassword() {
        return user.getPassword();
    }

    /**
     * Obtiene el nombre de usuario utilizado para autenticación.
     *
     * @return email del usuario.
     */
    @Override
    public String getUsername() {
        return user.getEmail();
    }

    /**
     * Indica si la cuenta del usuario no ha expirado.
     *
     * @return true si la cuenta sigue activa.
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * Indica si la cuenta del usuario no está bloqueada.
     *
     * @return true si la cuenta no está bloqueada.
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     * Indica si las credenciales del usuario no han expirado.
     *
     * @return true si las credenciales son válidas.
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * Indica si el usuario está habilitado.
     *
     * @return true si el usuario está activo y habilitado.
     */
    @Override
    public boolean isEnabled() {
        return true;
    }
}