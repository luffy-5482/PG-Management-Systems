package com.parent.tenant.service.impl;

import com.parent.tenant.dto.ChangePasswordRequest;
import com.parent.tenant.model.TenantCredentials;
import com.parent.tenant.repository.TenantCredentialsRepository;
import com.parent.tenant.service.PasswordService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PasswordServiceImpl implements PasswordService {

    private final TenantCredentialsRepository credsRepo;
    private final PasswordEncoder passwordEncoder;

    public PasswordServiceImpl(TenantCredentialsRepository credsRepo,
                               PasswordEncoder passwordEncoder) {
        this.credsRepo = credsRepo;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Note: this method name/signature MUST match the one in com.parent.tenant.service.PasswordService
     * (changeTenantPassword(...)). If your interface uses a different name, update this to match it.
     */
    @Override
    @Transactional
    public void changeTenantPassword(Long tenantId, ChangePasswordRequest req) {
        if (req == null || req.getOldPassword() == null || req.getNewPassword() == null) {
            throw new RuntimeException("Invalid password request");
        }

        TenantCredentials creds = credsRepo.findByTenant_Id(tenantId)
                .orElseThrow(() -> new RuntimeException("Credentials not found for tenant: " + tenantId));

        if (!passwordEncoder.matches(req.getOldPassword(), creds.getPasswordHash())) {
            throw new RuntimeException("Old password is incorrect");
        }

        creds.setPasswordHash(passwordEncoder.encode(req.getNewPassword()));
        credsRepo.save(creds);
    }
}
