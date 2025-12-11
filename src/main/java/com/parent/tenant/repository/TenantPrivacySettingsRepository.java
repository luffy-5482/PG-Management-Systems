package com.parent.tenant.repository;

import com.parent.tenant.model.TenantPrivacySettings;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TenantPrivacySettingsRepository extends JpaRepository<TenantPrivacySettings, Long> {
}
