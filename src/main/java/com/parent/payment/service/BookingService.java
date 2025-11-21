package com.parent.payment.service;

import com.parent.payment.model.Booking;
import org.springframework.http.ResponseEntity;
import java.util.List;
import java.util.Map;

public interface BookingService {
    Map<String,Object> createBookingAndOrder(Map<String,Object> request) throws Exception;
    ResponseEntity<?> verifyPayment(Map<String,String> payload) throws Exception;
    Booking getBooking(Long id);
    List<Booking> getBookingsByTenantEmail(String email);
    List<Booking> getBookingsByOwner(Long ownerId);
}
