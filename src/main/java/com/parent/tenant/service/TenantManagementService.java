package com.parent.tenant.service;

import com.parent.tenant.dto.TenantManagementDto;

import java.util.List;

public interface TenantManagementService {

    // 1. Fetch all tenants
    List<TenantManagementDto> getAllTenants();

    // 2. Search tenants for dropdown (by query)
    List<TenantManagementDto> searchTenants(String query);

    // 3. Create tenant
    TenantManagementDto createTenant(TenantManagementDto request);

    // 4. Update tenant
    TenantManagementDto updateTenant(Long id, TenantManagementDto request);

    // 5. Delete tenant
    void deleteTenant(Long id);

    // optional: single tenant by id
    TenantManagementDto getTenantById(Long id);
}
