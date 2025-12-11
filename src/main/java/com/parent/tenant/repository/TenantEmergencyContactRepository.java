package com.parent.tenant.repository;

import com.parent.tenant.model.TenantEmergencyContact;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TenantEmergencyContactRepository extends JpaRepository<TenantEmergencyContact, Long> {

    // Preferred: query by tenantId column and order by created_at desc
    @Query("SELECT c FROM TenantEmergencyContact c WHERE c.tenantId = :tenantId ORDER BY c.createdAt DESC")
    List<TenantEmergencyContact> findByTenantIdOrderByCreatedAtDesc(@Param("tenantId") Long tenantId);

    // Also keep the method name your service may call (underscore form). It maps to same query.
    @Query("SELECT c FROM TenantEmergencyContact c WHERE c.tenantId = :tenantId ORDER BY c.createdAt DESC")
    List<TenantEmergencyContact> findByTenant_IdOrderByCreatedAtDesc(@Param("tenantId") Long tenantId);
}
