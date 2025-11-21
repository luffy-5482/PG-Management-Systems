package com.parent.payment.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.parent.payment.service.BookingService;
import com.parent.payment.service.BookingServiceImpl;

import java.util.Map;

@RestController
@RequestMapping("/api/payments")
@CrossOrigin(origins = "*")
public class PaymentController {

    @Autowired
    private BookingService bookingService;

    // CREATE RAZORPAY ORDER + CREATE BOOKING
    @PostMapping("/create-booking-order")
    public Map<String, Object> createBookingOrder(@RequestBody Map<String, Object> request) throws Exception {
        return bookingService.createBookingAndOrder(request);
    }

    // VERIFY PAYMENT
    @PostMapping("/verify")
    public Object verifyPayment(@RequestBody Map<String, String> payload) throws Exception {
        return bookingService.verifyPayment(payload);
    }
}
