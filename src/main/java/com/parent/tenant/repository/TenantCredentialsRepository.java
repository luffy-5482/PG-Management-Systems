package com.parent.tenant.repository;

import com.parent.tenant.model.TenantCredentials;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface TenantCredentialsRepository extends JpaRepository<TenantCredentials, Long> {
    Optional<TenantCredentials> findByTenant_Id(Long tenantId);
    Optional<TenantCredentials> findByUsername(String username);
}
