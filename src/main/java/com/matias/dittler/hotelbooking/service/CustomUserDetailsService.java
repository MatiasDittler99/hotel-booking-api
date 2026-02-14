package com.matias.dittler.hotelbooking.service;

import com.matias.dittler.hotelbooking.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Servicio personalizado para cargar detalles de usuario para Spring Security.
 * Implementa UserDetailsService para que Spring Security pueda autenticar usuarios.
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    /**
     * Método que busca un usuario por su email (username) para autenticación.
     * @param username Email del usuario que intenta iniciar sesión
     * @return UserDetails del usuario si se encuentra
     * @throws UsernameNotFoundException Si no se encuentra el usuario con ese email
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Busca el usuario en la base de datos usando UserRepository
        return userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("Nombre de usuario no encontrado"));
    }

}
