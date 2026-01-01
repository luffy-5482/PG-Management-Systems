package com.parent.tenant.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.parent.tenant.model.TenantSupportTicket;

public interface TenantSupportTicketRepository
        extends JpaRepository<TenantSupportTicket, Long> {

    List<TenantSupportTicket> findByTenantId(Long tenantId);
}
