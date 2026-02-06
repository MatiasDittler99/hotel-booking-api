package com.matias.dittler.hotelbooking.repository;

import com.matias.dittler.hotelbooking.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    Optional<Booking> findByBookingConfirmationCode(String confirmationCode);
    
}
