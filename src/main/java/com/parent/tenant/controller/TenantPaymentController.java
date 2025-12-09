package com.parent.tenant.controller;

import com.parent.tenant.dto.TenantPaymentDto;
import com.parent.tenant.dto.TenantSubscriptionDto;
import com.parent.tenant.service.TenantPaymentService;
import com.parent.tenant.service.TenantSubscriptionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/tenant/{tenantId}/payments")
public class TenantPaymentController {

    private final TenantPaymentService paymentService;
    private final TenantSubscriptionService subscriptionService;

    public TenantPaymentController(TenantPaymentService paymentService,
                                   TenantSubscriptionService subscriptionService) {
        this.paymentService = paymentService;
        this.subscriptionService = subscriptionService;
    }

    // 1) list all payments for a tenant (history)
    @GetMapping
    public ResponseEntity<List<TenantPaymentDto>> listPayments(@PathVariable Long tenantId) {
        return ResponseEntity.ok(paymentService.getPaymentsForTenant(tenantId));
    }

    // 2) record subscription payment + update subscription
    //
    // Frontend can call this *after* Razorpay/Stripe success.
    //
    // Request example:
    // {
    //   "amount": 6500,
    //   "gateway": "RAZORPAY",
    //   "referenceId": "pay_123",
    //   "status": "SUCCESS"
    // }
    //
    @PostMapping("/subscription")
    public ResponseEntity<Map<String, Object>> recordSubscriptionPayment(
            @PathVariable Long tenantId,
            @RequestBody TenantPaymentDto request
    ) {
        TenantPaymentDto payment = paymentService.recordSubscriptionPayment(tenantId, request);
        TenantSubscriptionDto subscription = subscriptionService.getSubscriptionStatus(tenantId);

        Map<String, Object> response = new HashMap<>();
        response.put("payment", payment);
        response.put("subscription", subscription);

        return ResponseEntity.ok(response);
    }
}
