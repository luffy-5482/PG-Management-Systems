package com.parent.payment.repository;

import com.parent.payment.model.TenantPayment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TenantPaymentRepository extends JpaRepository<TenantPayment, Long> {

    // history for a tenant, latest first
    List<TenantPayment> findByTenant_IdOrderByPaidAtDesc(Long tenantId);
}
