package com.parent.payment.repository;

import com.parent.payment.model.Tenant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TenantRepository extends JpaRepository<Tenant, Long> {

    Optional<Tenant> findById(Long id);

    // 👇 ADD THIS back in
    Optional<Tenant> findByEmail(String email);
}
