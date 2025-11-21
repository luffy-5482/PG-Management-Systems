package com.parent.payment.controller;

import com.parent.payment.model.Booking;
import com.parent.payment.service.BookingService;
import com.parent.config.SecurityUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;
import java.util.List;

@RestController
@RequestMapping("/api/bookings")
@CrossOrigin(origins = "*")
public class BookingController {

    @Autowired private BookingService bookingService;

    @PostMapping("/create")
    public ResponseEntity<?> createBooking(@RequestBody Map<String, Object> request) {
        try {
            Map<String,Object> resp = bookingService.createBookingAndOrder(request);
            return ResponseEntity.ok(resp);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    @PostMapping("/verify")
    public ResponseEntity<?> verify(@RequestBody Map<String,String> payload) {
        try {
            return bookingService.verifyPayment(payload);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error verifying payment: " + e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getBooking(@PathVariable Long id) {
        try {
            Booking b = bookingService.getBooking(id);
            return ResponseEntity.ok(b);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    @GetMapping("/tenant")
    public ResponseEntity<?> getByTenant(@RequestParam String email) {
        return ResponseEntity.ok(bookingService.getBookingsByTenantEmail(email));
    }

    @GetMapping("/owner")
    public ResponseEntity<?> getByOwner() {
        Long ownerId = SecurityUtils.getLoggedInOwnerId();
        return ResponseEntity.ok(bookingService.getBookingsByOwner(ownerId));
    }
}
