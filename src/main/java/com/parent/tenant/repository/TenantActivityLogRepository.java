package com.parent.tenant.repository;

import com.parent.tenant.model.TenantActivityLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TenantActivityLogRepository extends JpaRepository<TenantActivityLog, Long> {

    // list latest first for a tenant
    List<TenantActivityLog> findByTenant_IdOrderByCreatedAtDesc(Long tenantId);
}
