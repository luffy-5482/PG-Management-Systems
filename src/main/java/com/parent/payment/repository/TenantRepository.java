package com.parent.payment.repository;

import com.parent.payment.model.Tenant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TenantRepository extends JpaRepository<Tenant, Long> {
    // JpaRepository already provides findById(Long). This is shown to make intent explicit.
    Optional<Tenant> findById(Long id);

    // If you need to look up by email (common), add:
    // Optional<Tenant> findByEmail(String email);
}
