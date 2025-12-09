package com.parent.tenant.controller;

import com.parent.tenant.dto.TenantManagementDto;
import com.parent.tenant.service.TenantManagementService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tenants")
public class TenantManagementController {

    private final TenantManagementService tenantManagementService;

    public TenantManagementController(TenantManagementService tenantManagementService) {
        this.tenantManagementService = tenantManagementService;
    }

    // 1. Fetch all tenants
    @GetMapping
    public ResponseEntity<List<TenantManagementDto>> getAllTenants(
            @RequestParam(value = "query", required = false) String query
    ) {
        // if query present, use search endpoint behavior
        if (query != null && !query.isBlank()) {
            return ResponseEntity.ok(tenantManagementService.searchTenants(query));
        }
        return ResponseEntity.ok(tenantManagementService.getAllTenants());
    }

    // 2. Fetch single tenant by ID
    @GetMapping("/{id}")
    public ResponseEntity<TenantManagementDto> getTenantById(@PathVariable Long id) {
        return ResponseEntity.ok(tenantManagementService.getTenantById(id));
    }

    // 3. Create tenant
    @PostMapping
    public ResponseEntity<TenantManagementDto> createTenant(
            @RequestBody TenantManagementDto request
    ) {
        TenantManagementDto created = tenantManagementService.createTenant(request);
        return ResponseEntity.ok(created);
    }

    // 4. Edit tenant
    @PutMapping("/{id}")
    public ResponseEntity<TenantManagementDto> updateTenant(
            @PathVariable Long id,
            @RequestBody TenantManagementDto request
    ) {
        TenantManagementDto updated = tenantManagementService.updateTenant(id, request);
        return ResponseEntity.ok(updated);
    }

    // 5. Delete tenant
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTenant(@PathVariable Long id) {
        tenantManagementService.deleteTenant(id);
        return ResponseEntity.noContent().build();
    }

    // 2 (explicit) Search endpoint for dropdown if they want a dedicated route
    @GetMapping("/search")
    public ResponseEntity<List<TenantManagementDto>> searchTenants(
            @RequestParam("query") String query
    ) {
        return ResponseEntity.ok(tenantManagementService.searchTenants(query));
    }
}
