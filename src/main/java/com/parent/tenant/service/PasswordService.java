package com.parent.tenant.service;

import com.parent.tenant.dto.ChangePasswordRequest;

public interface PasswordService {
    void changeTenantPassword(Long tenantId, ChangePasswordRequest req);
}
