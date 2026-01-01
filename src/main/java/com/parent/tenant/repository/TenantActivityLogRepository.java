package com.parent.tenant.repository;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.parent.tenant.model.TenantActivityLog;

public interface TenantActivityLogRepository
        extends JpaRepository<TenantActivityLog, Long> {

    List<TenantActivityLog> findByApplicationIdOrderByCreatedAtDesc(Long applicationId);
}
