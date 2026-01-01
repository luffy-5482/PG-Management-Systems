package com.parent.tenant.service;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.parent.tenant.enums.TenantStatus;
import com.parent.tenant.model.TenantAccount;
import com.parent.tenant.repository.TenantAccountRepository;

import jakarta.transaction.Transactional;

@Service
public class TenantAuthService {

    private final TenantAccountRepository accountRepo;
    private final PasswordEncoder passwordEncoder;

    public TenantAuthService(
            TenantAccountRepository accountRepo,
            PasswordEncoder passwordEncoder
    ) {
        this.accountRepo = accountRepo;
        this.passwordEncoder = passwordEncoder;
    }

    public TenantAccount login(String email, String rawPassword) {

        TenantAccount account = accountRepo.findByEmail(email)
                .orElseThrow(() -> new BadCredentialsException("Invalid credentials"));

        if (account.getStatus() != TenantStatus.ACTIVE)
            throw new IllegalStateException("Account not active");

        if (!passwordEncoder.matches(rawPassword, account.getPassword()))
            throw new BadCredentialsException("Invalid credentials");

        return account;
    }
    @Transactional
    public void changePassword(Long tenantId, String currentPassword, String newPassword) {
        TenantAccount account = accountRepo.findById(tenantId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid tenant"));

        if (!passwordEncoder.matches(currentPassword, account.getPassword())) {
            throw new IllegalArgumentException("Current password is incorrect");
        }

        account.setPassword(passwordEncoder.encode(newPassword));
        accountRepo.save(account);

        // Optional: log activity
    }
    
}
