package com.parent.tenant.service;

import com.parent.tenant.dto.TenantProfileDto;

public interface TenantProfileService {

    TenantProfileDto getProfile(Long tenantId);

    TenantProfileDto updateProfile(Long tenantId, TenantProfileDto update);
}
