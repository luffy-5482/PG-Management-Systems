package com.parent.tenant.repository;

import com.parent.tenant.model.TenantDocument;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TenantDocumentRepository extends JpaRepository<TenantDocument, Long> {

    // All docs for a given tenant
    List<TenantDocument> findByTenant_Id(Long tenantId);
    
    Optional<TenantDocument> findByIdAndTenantId(Long id, Long tenantId);
}
