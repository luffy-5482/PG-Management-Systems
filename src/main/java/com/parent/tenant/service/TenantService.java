package com.parent.tenant.service;

import com.parent.tenant.dto.*;
import org.springframework.data.domain.*;

public interface TenantService {

    TenantResponse createTenant(TenantRequest req, String actor);
    TenantResponse updateTenant(Long id, TenantRequest req, String actor);
    TenantResponse getTenant(Long id);

    Page<TenantResponse> listTenants(Pageable pageable, Long roomId, String name, Boolean active);

    void softDeleteTenant(Long id, String actor);
    void hardDeleteTenant(Long id);
}
