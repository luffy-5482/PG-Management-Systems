package com.parent.tenant.repository;

import com.parent.tenant.model.TenantNotificationPrefs;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface TenantNotificationPrefsRepository extends JpaRepository<TenantNotificationPrefs, Long> {
    Optional<TenantNotificationPrefs> findByTenantId(Long tenantId);
}
