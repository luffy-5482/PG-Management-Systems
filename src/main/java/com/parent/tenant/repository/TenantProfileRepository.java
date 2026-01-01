package com.parent.tenant.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import com.parent.tenant.model.TenantProfile;

public interface TenantProfileRepository
        extends JpaRepository<TenantProfile, Long> {

    Optional<TenantProfile> findByTenantId(Long tenantId);

    Optional<TenantProfile> findByApplicationId(Long applicationId);
}
