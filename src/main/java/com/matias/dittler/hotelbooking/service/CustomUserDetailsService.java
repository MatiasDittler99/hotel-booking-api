package com.matias.dittler.hotelbooking.service;

import com.matias.dittler.hotelbooking.entity.User;
import com.matias.dittler.hotelbooking.repository.UserRepository;
import com.matias.dittler.hotelbooking.service.implementation.CustomUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Servicio de integración con Spring Security para la carga de detalles de usuario.
 * 
 * Esta clase implementa UserDetailsService, permitiendo que Spring Security pueda:
 *  - Autenticar usuarios mediante su email.
 *  - Proveer roles y permisos asociados al usuario.
 * 
 * Buenas prácticas aplicadas:
 *  - Se delega la búsqueda de usuarios al UserRepository.
 *  - Se lanza UsernameNotFoundException si el usuario no existe, cumpliendo con el contrato de Spring Security.
 *  - Se encapsula la entidad User en CustomUserDetails para exponer solo la información necesaria a Spring Security.
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {

    /**
     * Repositorio de usuarios para acceder a la base de datos.
     * Inyección automática por Spring.
     */
    @Autowired
    private UserRepository userRepository;

    /**
     * Carga los detalles de un usuario dado su username (en este caso, email).
     *
     * Flujo:
     *  1. Busca el usuario en la base de datos mediante userRepository.
     *  2. Si no se encuentra, lanza UsernameNotFoundException.
     *  3. Si se encuentra, devuelve una instancia de CustomUserDetails que implementa UserDetails.
     *
     * @param username Email del usuario a buscar.
     * @return UserDetails con la información necesaria para Spring Security.
     * @throws UsernameNotFoundException si el usuario no existe en la base de datos.
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));

        return new CustomUserDetails(user);
    }
}