package com.parent.tenant.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.parent.tenant.model.TenantAccount;

import java.util.Optional;

public interface TenantAccountRepository
        extends JpaRepository<TenantAccount, Long> {

    Optional<TenantAccount> findByEmail(String email);
}
