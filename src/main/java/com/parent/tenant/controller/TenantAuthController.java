package com.parent.tenant.controller;

import java.util.Map;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.parent.config.JwtService;
import com.parent.tenant.dto.TenantLoginRequest;
import com.parent.tenant.model.TenantAccount;
import com.parent.tenant.model.TenantProfile;
import com.parent.tenant.repository.TenantProfileRepository;
import com.parent.tenant.service.TenantAuthService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/tenant/auth")
public class TenantAuthController {

    private final TenantAuthService authService;
    private final JwtService jwtService;
    private final TenantProfileRepository profileRepo;

    public TenantAuthController(
            TenantAuthService authService,
            JwtService jwtService,
            TenantProfileRepository profileRepo
            
    ) {
        this.authService = authService;
        this.jwtService = jwtService;
        this.profileRepo=profileRepo;
    }

    @PostMapping("/login")
    public Map<String, Object> login(@RequestBody TenantLoginRequest req) {
        // Validate input
        if (req.getEmail() == null || req.getPassword() == null ||
            req.getEmail().trim().isEmpty() || req.getPassword().trim().isEmpty()) {
            throw new IllegalArgumentException("Email and password are required");
        }

        TenantAccount account = authService.login(req.getEmail(), req.getPassword());

        // Build JWT claims
        Map<String, Object> claims = Map.of(
                "tenantId", account.getId(),
                "role", "TENANT"
        );

        String token = jwtService.generateToken(account.getEmail(), claims);

        // Fetch profile to get full name
        TenantProfile profile = profileRepo.findByTenantId(account.getId())
                .orElse(new TenantProfile());  // fallback if missing

        String fullName = (profile.getFirstName() != null ? profile.getFirstName() : "") +
                          (profile.getLastName() != null ? " " + profile.getLastName() : "");

        // Return richer response
        return Map.of(
                "token", token,
                "tenantId", account.getId(),
                "email", account.getEmail(),
                "name", fullName.trim().isEmpty() ? "Tenant" : fullName.trim(),
                "requiresPasswordChange", account.isTempPassword()  // ← key for forcing password change
        );
    }
    @PostMapping("/change-password")
    public Map<String, String> changePassword(
            HttpServletRequest request,
            @RequestBody Map<String, String> body
    ) {
        Long tenantId = (Long) request.getAttribute("tenantId");
        String currentPassword = body.get("currentPassword");
        String newPassword = body.get("newPassword");

        authService.changePassword(tenantId, currentPassword, newPassword);

        return Map.of("message", "Password changed successfully");
    }
    @PostMapping("/logout")
    public Map<String, String> logout(HttpServletRequest request) {
        // If using token blacklist, add token here
        return Map.of("message", "Logged out successfully");
    }
}
