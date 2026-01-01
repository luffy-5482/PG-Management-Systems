package com.parent.tenant.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.parent.tenant.model.TenantDocument;
import com.parent.tenant.model.TenantProfile;

public interface TenantDocumentRepository
        extends JpaRepository<TenantDocument, Long> {

	 Optional<TenantDocument> findByApplicationId(Long applicationId);
}
