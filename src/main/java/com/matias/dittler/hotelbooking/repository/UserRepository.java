package com.matias.dittler.hotelbooking.repository;

import com.matias.dittler.hotelbooking.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

/**
 * Repositorio para la entidad User.
 *
 * Extiende JpaRepository para heredar operaciones CRUD básicas:
 * - save()
 * - findById()
 * - findAll()
 * - deleteById()
 *
 * Incluye métodos personalizados útiles para la autenticación y validación de usuarios.
 */
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Verifica si un usuario con el email dado ya existe en la base de datos.
     *
     * Útil para evitar registros duplicados durante el registro de usuarios.
     *
     * @param email Email del usuario
     * @return true si el email ya existe, false en caso contrario
     */
    boolean existsByEmail(String email);

    /**
     * Busca un usuario por su email.
     *
     * Este método es utilizado principalmente en procesos de login
     * o validación de credenciales.
     *
     * @param email Email del usuario
     * @return Optional<User> que contiene el usuario si se encuentra
     */
    Optional<User> findByEmail(String email);
    
}
