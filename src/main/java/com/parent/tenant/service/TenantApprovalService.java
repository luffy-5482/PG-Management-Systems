package com.parent.tenant.service;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.parent.config.JwtService;
import com.parent.pg.model.PgEntity;
import com.parent.pg.repository.PgRepository;
import com.parent.tenant.enums.TenantStatus;
import com.parent.tenant.model.TenantAccount;
import com.parent.tenant.model.TenantApplication;
import com.parent.tenant.model.TenantProfile;
import com.parent.tenant.repository.TenantAccountRepository;
import com.parent.tenant.repository.TenantApplicationRepository;
import com.parent.tenant.repository.TenantProfileRepository;

import jakarta.servlet.http.HttpServletRequest;

@Service
public class TenantApprovalService {

    private final TenantApplicationRepository appRepo;
    private final TenantAccountRepository accountRepo;
    private final TenantProfileRepository profileRepo;
    private final PgRepository pgRepo;
    private final JwtService jwtService;
    private final TenantActivityLogService logService;
    private final PasswordEncoder passwordEncoder;

    public TenantApprovalService(
            TenantApplicationRepository appRepo,
            TenantAccountRepository accountRepo,
            TenantProfileRepository profileRepo,
            PgRepository pgRepo,
            JwtService jwtService,
            TenantActivityLogService logService,
            PasswordEncoder passwordEncoder
    ) {
        this.appRepo = appRepo;
        this.accountRepo = accountRepo;
        this.profileRepo = profileRepo;
        this.pgRepo = pgRepo;
        this.jwtService = jwtService;
        this.logService = logService;
        this.passwordEncoder = passwordEncoder;
    }

    // ----------------------------------------
    // APPROVE TENANT (SECURED)
    // ----------------------------------------
    @Transactional
    public void approve(Long applicationId, String tempPassword, HttpServletRequest request) {

        TenantApplication app = appRepo.findById(applicationId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid application"));

        if (app.getStatus() != TenantStatus.UNDER_REVIEW) {
            throw new IllegalStateException("Application not eligible for approval");
        }

        PgEntity pg = pgRepo.findById(Long.valueOf(app.getPgId()))
                .orElseThrow(() -> new IllegalStateException("PG not found"));

        enforceAuthorization(pg, request);

        // ---- CREATE TENANT ACCOUNT ----
        TenantAccount account = new TenantAccount();
        account.setApplicationId(app.getId());
        account.setEmail(app.getEmail());
        account.setPassword(passwordEncoder.encode(tempPassword));
        account.setStatus(TenantStatus.ACTIVE);
        account.setTempPassword(true);

        account = accountRepo.save(account);

        // ---- LINK PROFILE ----
        TenantProfile profile = profileRepo.findByApplicationId(app.getId())
                .orElseThrow(() -> new IllegalStateException("Tenant profile missing"));

        profile.setTenantId(account.getId());
        profileRepo.save(profile);

        // ---- UPDATE APPLICATION STATUS ----
        app.setStatus(TenantStatus.APPROVED);
        appRepo.save(app);

        // ---- ACTIVITY LOG ----
        logService.log(
                applicationId,
                "APPROVED",
                "Tenant approved for PG " + pg.getId()
        );
    }

    // ----------------------------------------
    // REJECT TENANT (SECURED)
    // ----------------------------------------
    @Transactional
    public void reject(Long applicationId, String reason, HttpServletRequest request) {

        TenantApplication app = appRepo.findById(applicationId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid application"));

        PgEntity pg = pgRepo.findById(Long.valueOf(app.getPgId()))
                .orElseThrow(() -> new IllegalStateException("PG not found"));

        enforceAuthorization(pg, request);

        app.setStatus(TenantStatus.REJECTED);
        appRepo.save(app);

        logService.log(applicationId, "REJECTED", reason);
    }

    // ----------------------------------------
    // AUTHORIZATION CORE
    // ----------------------------------------
    private void enforceAuthorization(PgEntity pg, HttpServletRequest request) {

        String role = jwtService.extractRoleFromRequest(request);

        if ("ADMIN".equals(role)) {
            return;
        }

        if ("OWNER".equals(role)) {
            Long ownerId = jwtService.extractOwnerIdFromRequest(request);
            if (!pg.getOwner().getId().equals(ownerId)) {
                throw new AccessDeniedException("You do not own this PG");
            }
            return;
        }

        if ("MANAGER".equals(role)) {
            var allowedPgIds = jwtService.extractAllowedPgIdsFromRequest(request);
            if (!allowedPgIds.contains(pg.getId())) {
                throw new AccessDeniedException("Manager not assigned to this PG");
            }
            return;
        }

        throw new AccessDeniedException("Unauthorized role");
    }
}
