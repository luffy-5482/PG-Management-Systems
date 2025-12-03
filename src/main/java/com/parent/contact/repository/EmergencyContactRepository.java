package com.parent.contact.repository;

import com.parent.contact.model.EmergencyContact;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmergencyContactRepository extends JpaRepository<EmergencyContact, Long> {
    List<EmergencyContact> findByTenantId(Long tenantId);

    // If your EmergencyContact stores a Tenant object:
    // List<EmergencyContact> findByTenant_Id(Long tenantId);
}
