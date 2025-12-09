package com.parent.payment.repository;

import com.parent.payment.model.TenantSupportTicket;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TenantSupportTicketRepository extends JpaRepository<TenantSupportTicket, Long> {

    // for activity & ticket list
    List<TenantSupportTicket> findByTenant_IdOrderByCreatedAtDesc(Long tenantId);
}
