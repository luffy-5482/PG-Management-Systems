package com.parent.tenant.controller;

import com.parent.tenant.dto.TenantRequest;
import com.parent.tenant.dto.TenantResponse;
import com.parent.tenant.service.TenantService;
import com.parent.config.SecurityUtils;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

@RestController
@RequestMapping("/api/tenant")
@CrossOrigin(origins = "*")
public class TenantController {

    @Autowired
    private TenantService tenantService;

    private String getActor() {
        String email = SecurityUtils.getLoggedInEmail();
        return email == null ? "SYSTEM" : email;
    }

    // CREATE TENANT (OWNER / MANAGER / ADMIN)
    @PostMapping
    public ResponseEntity<TenantResponse> createTenant(
            @Valid @RequestBody TenantRequest req) {

        TenantResponse res = tenantService.createTenant(req, getActor());
        return ResponseEntity.status(HttpStatus.CREATED).body(res);
    }

    // GET TENANT BY ID
    @GetMapping("/{id}")
    public TenantResponse getTenant(@PathVariable Long id) {
        return tenantService.getTenant(id);
    }

    // LIST TENANTS (FILTERS + PAGINATION)
    @GetMapping
    public Page<TenantResponse> listTenants(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "20") int size,
            @RequestParam(value = "roomId", required = false) Long roomId,
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "active", required = false) Boolean active) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("joinedAt").descending());
        return tenantService.listTenants(pageable, roomId, name, active);
    }

    // UPDATE TENANT
    @PutMapping("/{id}")
    public TenantResponse updateTenant(
            @PathVariable Long id,
            @Valid @RequestBody TenantRequest req) {

        return tenantService.updateTenant(id, req, getActor());
    }

    // SOFT DELETE TENANT
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> softDelete(
            @PathVariable Long id) {

        tenantService.softDeleteTenant(id, getActor());
        return ResponseEntity.noContent().build();
    }

    // HARD DELETE TENANT (OWNER ONLY)
    @DeleteMapping("/{id}/hard")
    public ResponseEntity<Void> hardDelete(@PathVariable Long id) {
        tenantService.hardDeleteTenant(id);
        return ResponseEntity.noContent().build();
    }
}
