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

        System.out.println(">>> HIT /api/tenant/" + tenantId + "/profile");

        // 🔴 TEMP: return a dummy DTO to see if the endpoint itself works
        TenantProfileDto dto = new TenantProfileDto();
        dto.setId(tenantId);
        dto.setName("TEST USER");
        dto.setEmail("test@example.com");
        dto.setContact("0000000000");

        return ResponseEntity.ok(dto);

        // ✅ Once this works, we’ll put back:
        // TenantProfileDto profile = tenantProfileService.getProfile(tenantId);
        // return ResponseEntity.ok(profile);
    }

    @PutMapping("/{id}/profile")
    public ResponseEntity<TenantProfileDto> updateProfile(
            @PathVariable("id") Long tenantId,
            @RequestBody TenantProfileDto updateRequest
    ) {
        System.out.println(">>> HIT PUT /api/tenant/" + tenantId + "/profile");
        TenantProfileDto updatedProfile = tenantProfileService.updateProfile(tenantId, updateRequest);
        return ResponseEntity.ok(updatedProfile);
    }
}
