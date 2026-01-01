package com.parent.tenant.controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.parent.tenant.model.TenantProfile;
import com.parent.tenant.service.TenantProfileService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/tenant/self/profile")
public class TenantProfileController {

    private final TenantProfileService service;

    public TenantProfileController(TenantProfileService service) {
        this.service = service;
    }

    // ----------------------------
    // GET PROFILE
    // ----------------------------
    @GetMapping
    public TenantProfile get(HttpServletRequest request) {

        Long tenantId = (Long) request.getAttribute("tenantId");
        return service.getProfile(tenantId);
    }

    // ----------------------------
    // UPDATE PROFILE
    // ----------------------------
    @PutMapping
    public TenantProfile update(
            HttpServletRequest request,
            @RequestBody TenantProfile profile) {

        Long tenantId = (Long) request.getAttribute("tenantId");
        return service.updateProfile(tenantId, profile);
    }
}
