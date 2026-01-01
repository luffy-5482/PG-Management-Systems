package com.parent.tenant.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.parent.tenant.model.TenantNotification;

public interface TenantNotificationRepository
        extends JpaRepository<TenantNotification, Long> {

    List<TenantNotification> findByTenantIdOrderByCreatedAtDesc(Long tenantId);

    int countByTenantIdAndReadFalse(Long tenantId);
}
