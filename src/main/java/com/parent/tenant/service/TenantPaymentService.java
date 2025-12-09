package com.parent.tenant.service;

import com.parent.tenant.dto.TenantPaymentDto;

import java.util.List;

public interface TenantPaymentService {

    // list all payments for a tenant
    List<TenantPaymentDto> getPaymentsForTenant(Long tenantId);

    // record one subscription payment and update subscription
    TenantPaymentDto recordSubscriptionPayment(Long tenantId, TenantPaymentDto request);
}
