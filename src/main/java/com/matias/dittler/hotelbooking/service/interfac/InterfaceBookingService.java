package com.matias.dittler.hotelbooking.service.interfac;

import com.matias.dittler.hotelbooking.dto.Response;
import com.matias.dittler.hotelbooking.entity.Booking;

public interface InterfaceBookingService {

    Response saveBooking(Long rooId, Long userId, Booking bookingRequest);
    Response findBookingByConfirmationCode(String confirmationCode);
    Response getAllBookings();
    Response cancelBooking(Long bookingId);
    
}
