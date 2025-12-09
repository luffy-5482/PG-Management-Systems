package com.parent.tenant.service.impl;

import com.parent.payment.model.Tenant;
import com.parent.payment.model.TenantPayment;
import com.parent.payment.model.TenantSubscription;
import com.parent.payment.repository.TenantPaymentRepository;
import com.parent.payment.repository.TenantRepository;
import com.parent.payment.repository.TenantSubscriptionRepository;
import com.parent.tenant.dto.TenantPaymentDto;
import com.parent.tenant.service.TenantPaymentService;
import com.parent.tenant.service.TenantActivityService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TenantPaymentServiceImpl implements TenantPaymentService {

    private final TenantRepository tenantRepository;
    private final TenantPaymentRepository paymentRepository;
    private final TenantSubscriptionRepository subscriptionRepository;
    private final TenantActivityService activityService;   // ✅ NEW

    public TenantPaymentServiceImpl(TenantRepository tenantRepository,
                                    TenantPaymentRepository paymentRepository,
                                    TenantSubscriptionRepository subscriptionRepository,
                                    TenantActivityService activityService) {   // ✅ NEW ARG
        this.tenantRepository = tenantRepository;
        this.paymentRepository = paymentRepository;
        this.subscriptionRepository = subscriptionRepository;
        this.activityService = activityService;
    }

    // ----------------------------------------------------
    // 1) LIST HISTORY
    // ----------------------------------------------------
    @Override
    public List<TenantPaymentDto> getPaymentsForTenant(Long tenantId) {

        // Optional: ensure tenant exists
        tenantRepository.findById(tenantId)
                .orElseThrow(() -> new RuntimeException("Tenant not found with id: " + tenantId));

        return paymentRepository.findByTenant_IdOrderByPaidAtDesc(tenantId)
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    // ----------------------------------------------------
    // 2) RECORD PAYMENT + UPDATE SUBSCRIPTION
    // ----------------------------------------------------
    @Override
    public TenantPaymentDto recordSubscriptionPayment(Long tenantId, TenantPaymentDto request) {

        Tenant tenant = tenantRepository.findById(tenantId)
                .orElseThrow(() -> new RuntimeException("Tenant not found with id: " + tenantId));

        TenantSubscription sub = subscriptionRepository.findByTenant_Id(tenantId)
                .orElseThrow(() -> new RuntimeException("Subscription not found for tenant id: " + tenantId));

        // ---- validate & normalize method (UPI / CASH) ----
        String method = request.getMethod();
        if (method == null || method.isBlank()) {
            throw new RuntimeException("Payment method is required (UPI or CASH)");
        }

        method = method.trim().toUpperCase();
        if (!method.equals("UPI") && !method.equals("CASH")) {
            throw new RuntimeException("Invalid payment method: " + method + " (allowed: UPI, CASH)");
        }

        // ---- create payment row ----
        TenantPayment payment = new TenantPayment();
        payment.setTenant(tenant);

        // amount: request wins, otherwise use monthlyRent
        Double amount = request.getAmount() != null
                ? request.getAmount()
                : sub.getMonthlyRent();

        payment.setAmount(amount);

        // paidAt: request or now
        if (request.getPaidAt() != null) {
            payment.setPaidAt(request.getPaidAt());
        } else {
            payment.setPaidAt(LocalDateTime.now());
        }

        // UPI / CASH
        payment.setMethod(method);

        // UPI ref id or any reference (optional for CASH)
        payment.setTransactionId(request.getTransactionId());

        // status default SUCCESS if not given
        if (request.getStatus() != null && !request.getStatus().isBlank()) {
            payment.setStatus(request.getStatus());
        } else {
            payment.setStatus("SUCCESS");
        }

        // description default based on method if not provided
        if (request.getDescription() != null && !request.getDescription().isBlank()) {
            payment.setDescription(request.getDescription());
        } else if (method.equals("UPI")) {
            payment.setDescription("Monthly rent paid via UPI");
        } else {
            payment.setDescription("Monthly rent paid in cash");
        }

        TenantPayment savedPayment = paymentRepository.save(payment);

        // ---- update subscription next due date & status ----
        LocalDate today = LocalDate.now();
        LocalDate base = sub.getNextDueDate();

        // if no due date or it is in the past, base on today
        if (base == null || base.isBefore(today)) {
            base = today;
        }

        sub.setNextDueDate(base.plusMonths(1));
        sub.setStatus("ACTIVE");
        TenantSubscription savedSub = subscriptionRepository.save(sub);

        // ✅ ---- RECORD ACTIVITY LOG ----
        String title = "Monthly subscription payment (" + method + ")";
        String desc = "Amount: ₹" + amount
                + ", status: " + savedPayment.getStatus()
                + ", next due: " + savedSub.getNextDueDate();

        activityService.recordActivity(
                tenantId,
                "PAYMENT",
                title,
                desc
        );

        return toDto(savedPayment);
    }

    // ----------------------------------------------------
    // MAPPER
    // ----------------------------------------------------
    private TenantPaymentDto toDto(TenantPayment p) {
        TenantPaymentDto dto = new TenantPaymentDto();
        dto.setId(p.getId());
        if (p.getTenant() != null) {
            dto.setTenantId(p.getTenant().getId());
        }
        dto.setAmount(p.getAmount());
        dto.setPaidAt(p.getPaidAt());
        dto.setMethod(p.getMethod());
        dto.setTransactionId(p.getTransactionId());
        dto.setStatus(p.getStatus());
        dto.setDescription(p.getDescription());
        return dto;
    }
}
