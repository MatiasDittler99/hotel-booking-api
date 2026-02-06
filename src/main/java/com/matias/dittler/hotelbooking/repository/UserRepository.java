package com.matias.dittler.hotelbooking.repository;

import com.matias.dittler.hotelbooking.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByEmail(String email);
    Optional<User> findByEmail(String email);
    
}