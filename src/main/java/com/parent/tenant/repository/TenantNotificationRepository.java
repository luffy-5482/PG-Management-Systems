package com.parent.tenant.repository;

import com.parent.tenant.model.TenantNotification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TenantNotificationRepository extends JpaRepository<TenantNotification, Long> {
    List<TenantNotification> findByTenantIdOrderByCreatedAtDesc(Long tenantId);
    List<TenantNotification> findByTenantIdAndIsReadFalseOrderByCreatedAtDesc(Long tenantId);
    long countByTenantIdAndIsReadFalse(Long tenantId);
}
