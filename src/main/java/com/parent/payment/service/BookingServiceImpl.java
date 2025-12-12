package com.parent.payment.service;

import com.parent.payment.model.*;
import com.parent.payment.repository.*;
import com.parent.pg.repository.RoomRepository;
import com.parent.pg.repository.PgRepository;
import com.parent.pg.model.RoomEntity;
import com.parent.payment.util.RazorpaySignatureVerifier;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.format.DateTimeParseException;
import java.util.*;

@Service
public class BookingServiceImpl implements BookingService {

    @Autowired private RoomRepository roomRepository;
    @Autowired private PgRepository pgRepository;
    @Autowired private TenantRepository tenantRepository;
    @Autowired private BookingRepository bookingRepository;
    @Autowired private PaymentRepository paymentRepository;
    @Autowired private RazorpayOrderService razorpayOrderService;

    @Value("${razorpay.secret}")
    private String razorpaySecret;

    @Override
    @Transactional
    public Map<String, Object> createBookingAndOrder(Map<String, Object> request) throws Exception {
        Number roomIdNum = (Number) request.get("roomId");
        if (roomIdNum == null) throw new IllegalArgumentException("roomId is required");
        Long roomId = roomIdNum.longValue();

        RoomEntity room = roomRepository.findById(roomId)
                .orElseThrow(() -> new RuntimeException("Room not found"));

        if (room.getAvailable() == null || !room.getAvailable()) {
            throw new RuntimeException("Room is not available for booking");
        }

        String tenantName = (String)request.get("tenantName");
        String tenantEmail = (String)request.get("tenantEmail");
        String tenantContact = (String)request.get("tenantContact");

        if (tenantName == null || tenantEmail == null) throw new IllegalArgumentException("tenantName & tenantEmail required");

        // parse optional dates
        Instant start = null, end = null;
        try {
            if (request.get("startDate") != null) start = Instant.parse((String) request.get("startDate"));
            if (request.get("endDate") != null) end = Instant.parse((String) request.get("endDate"));
        } catch (DateTimeParseException ex) { /* ignore */ }

        Double price = room.getPricePerBed();
        if (price == null) {
            Number amt = (Number) request.get("amount");
            if (amt == null) throw new IllegalArgumentException("Price not set for room; provide amount");
            price = amt.doubleValue();
        }
        long amountInPaise = Math.round(price * 100);

        String receipt = "rcpt_" + UUID.randomUUID().toString().replace("-", "").substring(0,12);
        JSONObject order = razorpayOrderService.createOrder(amountInPaise, receipt);

        Tenant tenant = tenantRepository.findByEmail(tenantEmail)
                .orElseGet(() -> tenantRepository.save(new Tenant(tenantName, tenantEmail, tenantContact)));

        Payment payment = new Payment();
        payment.setRoomId(room.getId());
        payment.setPgId(room.getPg() != null ? room.getPg().getId() : null);
        payment.setAmount(order.getLong("amount"));
        payment.setCurrency(order.getString("currency"));
        payment.setOrderId(order.getString("id"));
        payment.setStatus("CREATED");
        payment.setTenantName(tenantName);
        payment.setTenantEmail(tenantEmail);
        payment.setTenantContact(tenantContact);
        payment = paymentRepository.save(payment);

        Booking booking = new Booking();
        booking.setRoomId(room.getId());
        booking.setPgId(room.getPg() != null ? room.getPg().getId() : null);
        booking.setOwnerId(room.getPg() != null && room.getPg().getOwner() != null ? room.getPg().getOwner().getId() : null);
        booking.setTenant(tenant);
        booking.setStartDate(start);
        booking.setEndDate(end);
        booking.setPayment(payment);
        booking.setStatus("PENDING");
        booking = bookingRepository.save(booking);

        Map<String,Object> resp = new HashMap<>();
        resp.put("orderId", order.getString("id"));
        resp.put("amount", order.getLong("amount"));
        resp.put("currency", order.getString("currency"));
        resp.put("key", razorpayOrderService.getKeyId());
        resp.put("bookingId", booking.getId());
        resp.put("roomId", room.getId());
        return resp;
    }

    @Override
    @Transactional
    public ResponseEntity<?> verifyPayment(Map<String, String> payload) throws Exception {
        String orderId = payload.get("razorpay_order_id");
        String paymentId = payload.get("razorpay_payment_id");
        String signature = payload.get("razorpay_signature");
        String bookingIdStr = payload.get("bookingId");

        if (orderId == null || paymentId == null || signature == null) {
            return ResponseEntity.badRequest().body("Missing fields");
        }

        Payment payment = paymentRepository.findByOrderId(orderId);
        if (payment == null) return ResponseEntity.badRequest().body("Payment record not found");

        String payloadStr = orderId + "|" + paymentId;
        boolean valid = RazorpaySignatureVerifier.verifySignature(payloadStr, signature, razorpaySecret);

        if (!valid) {
            payment.setStatus("FAILED");
            paymentRepository.save(payment);
            return ResponseEntity.badRequest().body("Invalid signature");
        }

        payment.setPaymentId(paymentId);
        payment.setStatus("SUCCESS");
        paymentRepository.save(payment);

        Booking booking = null;
        if (bookingIdStr != null) {
            Long bookingId = Long.valueOf(bookingIdStr);
            booking = bookingRepository.findById(bookingId).orElse(null);
        }
        if (booking == null) {
            booking = bookingRepository.findByPayment_Id(payment.getId()).orElse(null);
        }

        if (booking != null) {
            booking.setStatus("CONFIRMED");
            bookingRepository.save(booking);
        }

        RoomEntity room = roomRepository.findById(payment.getRoomId())
                .orElseThrow(() -> new RuntimeException("Room not found"));
        room.setAvailable(false);
        roomRepository.save(room);

        Map<String,Object> resp = new HashMap<>();
        resp.put("message","Payment verified and booking confirmed");
        resp.put("bookingId", booking != null ? booking.getId() : null);
        return ResponseEntity.ok(resp);
    }

    @Override
    public Booking getBooking(Long id) {
        return bookingRepository.findById(id).orElseThrow(() -> new RuntimeException("Booking not found"));
    }

    @Override
    public List<Booking> getBookingsByTenantEmail(String email) {
        return bookingRepository.findByTenant_Email(email);
    }

    @Override
    public List<Booking> getBookingsByOwner(Long ownerId) {
        return bookingRepository.findByOwnerId(ownerId);
    }
}
