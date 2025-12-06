package com.parent.tenant.controller;

import com.parent.tenant.dto.TenantProfileDto;
import com.parent.tenant.service.TenantProfileService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tenant")
public class TenantProfileController {

    private final TenantProfileService tenantProfileService;

    public TenantProfileController(TenantProfileService tenantProfileService) {
        this.tenantProfileService = tenantProfileService;
    }

    @GetMapping("/{id}/profile")
    public ResponseEntity<TenantProfileDto> getProfile(@PathVariable("id") Long tenantId) {
        TenantProfileDto profile = tenantProfileService.getProfile(tenantId);
        return ResponseEntity.ok(profile);
    }

    @PutMapping("/{id}/profile")
    public ResponseEntity<TenantProfileDto> updateProfile(
            @PathVariable("id") Long tenantId,
            @RequestBody TenantProfileDto updateRequest
    ) {
        TenantProfileDto updatedProfile = tenantProfileService.updateProfile(tenantId, updateRequest);
        return ResponseEntity.ok(updatedProfile);
    }
}
