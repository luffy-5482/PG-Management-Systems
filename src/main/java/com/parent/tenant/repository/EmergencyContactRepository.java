package com.parent.tenant.repository;

import com.parent.tenant.model.EmergencyContact;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface EmergencyContactRepository extends JpaRepository<EmergencyContact, Long> {

    // convenient method expected by existing service
    List<EmergencyContact> findByTenantId(Long tenantId);

    // keep the other existing method (if present) too
    List<EmergencyContact> findByTenantIdOrderByCreatedAtDesc(Long tenantId);
}
