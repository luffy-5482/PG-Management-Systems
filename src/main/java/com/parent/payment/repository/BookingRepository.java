package com.parent.payment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.parent.payment.model.Booking;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findByTenant_Email(String email);
    List<Booking> findByOwnerId(Long ownerId);
    List<Booking> findByRoomId(Long roomId);
    Optional<Booking> findByPayment_Id(Long paymentId);

    // 🔥 New method to get tenant's current booking
    Optional<Booking> findTopByTenant_IdAndStatusOrderByIdDesc(Long tenantId, String status);
}
