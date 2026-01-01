package com.parent.tenant.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.parent.tenant.model.TenantApplication;

public interface TenantApplicationRepository
        extends JpaRepository<TenantApplication, Long> {

    Optional<TenantApplication> findByEmail(String email);
    Optional<TenantApplication> findByPhone(String phone);
}
