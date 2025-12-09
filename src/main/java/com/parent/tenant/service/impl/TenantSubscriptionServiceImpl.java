package com.parent.tenant.service.impl;

import com.parent.payment.model.Tenant;
import com.parent.payment.model.TenantSubscription;
import com.parent.payment.repository.TenantRepository;
import com.parent.payment.repository.TenantSubscriptionRepository;
import com.parent.tenant.dto.TenantSubscriptionDto;
import com.parent.tenant.exception.SubscriptionExpiredException;
import com.parent.tenant.service.TenantSubscriptionService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Service
public class TenantSubscriptionServiceImpl implements TenantSubscriptionService {

    private final TenantSubscriptionRepository subscriptionRepository;
    private final TenantRepository tenantRepository;

    public TenantSubscriptionServiceImpl(TenantSubscriptionRepository subscriptionRepository,
                                         TenantRepository tenantRepository) {
        this.subscriptionRepository = subscriptionRepository;
        this.tenantRepository = tenantRepository;
    }

    // ----------------------------------------------------
    // 0) HARD CHECK FOR BACKEND ENFORCEMENT
    // ----------------------------------------------------
    @Override
    public void assertSubscriptionActiveOrThrow(Long tenantId) {

        TenantSubscription sub = subscriptionRepository.findByTenant_Id(tenantId)
                .orElseThrow(() ->
                        new SubscriptionExpiredException("No active subscription found for this tenant")
                );

        LocalDate today = LocalDate.now();

        boolean notActive = (sub.getStatus() == null)
                || !sub.getStatus().equalsIgnoreCase("ACTIVE");

        boolean overdue = (sub.getNextDueDate() == null)
                || sub.getNextDueDate().isBefore(today);

        if (notActive || overdue) {
            throw new SubscriptionExpiredException(
                    "Subscription expired or blocked. Please renew your plan."
            );
        }
    }

    // ----------------------------------------------------
    // 1) STATUS (FRIENDLY DTO FOR FRONTEND BANNER)
    // ----------------------------------------------------
    @Override
    public TenantSubscriptionDto getSubscriptionStatus(Long tenantId) {

        Optional<TenantSubscription> opt = subscriptionRepository.findByTenant_Id(tenantId);

        if (opt.isEmpty()) {
            // No subscription row → BLOCKED
            TenantSubscriptionDto dto = new TenantSubscriptionDto();
            dto.setTenantId(tenantId);
            dto.setMonthlyRent(null);     // keep null to avoid Double/Integer mismatch
            dto.setNextDueDate(null);
            dto.setStatus("BLOCKED");
            dto.setMessage("No active subscription found");
            return dto;
        }

        TenantSubscription sub = opt.get();
        return toDto(sub, "Subscription active");
    }

    // ----------------------------------------------------
    // 2) CREATE OR UPDATE SUBSCRIPTION
    // ----------------------------------------------------
    @Override
    public TenantSubscriptionDto createOrUpdateSubscription(Long tenantId,
                                                            TenantSubscriptionDto request) {

        Tenant tenant = tenantRepository.findById(tenantId)
                .orElseThrow(() -> new RuntimeException("Tenant not found with id: " + tenantId));

        TenantSubscription sub = subscriptionRepository.findByTenant_Id(tenantId)
                .orElseGet(() -> {
                    TenantSubscription s = new TenantSubscription();
                    s.setTenant(tenant);
                    return s;
                });

        // 💰 monthlyRent (type must match entity field: use Double both sides)
        if (request.getMonthlyRent() != null) {
            sub.setMonthlyRent(request.getMonthlyRent());
        }

        // 📅 nextDueDate: either from request, or auto-set if missing
        if (request.getNextDueDate() != null) {
            sub.setNextDueDate(request.getNextDueDate());
        } else if (sub.getNextDueDate() == null) {
            // first-time subscription: set next due 1 month from now
            sub.setNextDueDate(LocalDate.now().plusMonths(1));
        }

        // 🟢 status: from request or default ACTIVE
        if (request.getStatus() != null && !request.getStatus().isBlank()) {
            sub.setStatus(request.getStatus());
        } else {
            sub.setStatus("ACTIVE");
        }

        TenantSubscription saved = subscriptionRepository.save(sub);
        return toDto(saved, "Subscription created/updated");
    }

    // ----------------------------------------------------
    // 3) RENEW BY 1 MONTH
    // ----------------------------------------------------
    @Override
    public TenantSubscriptionDto renewSubscription(Long tenantId) {

        TenantSubscription sub = subscriptionRepository.findByTenant_Id(tenantId)
                .orElseThrow(() -> new RuntimeException("Subscription not found for tenant id: " + tenantId));

        LocalDate currentDue = sub.getNextDueDate();
        LocalDate newDue;

        if (currentDue == null || currentDue.isBefore(LocalDate.now())) {
            // if overdue or never set → next month from today
            newDue = LocalDate.now().plusMonths(1);
        } else {
            // normal renewal → push by 1 month from existing due date
            newDue = currentDue.plusMonths(1);
        }

        sub.setNextDueDate(newDue);
        sub.setStatus("ACTIVE");

        TenantSubscription saved = subscriptionRepository.save(sub);
        return toDto(saved, "Subscription renewed");
    }

    // ----------------------------------------------------
    // HELPER MAPPER
    // ----------------------------------------------------
    private TenantSubscriptionDto toDto(TenantSubscription sub, String message) {
        TenantSubscriptionDto dto = new TenantSubscriptionDto();

        if (sub.getTenant() != null) {
            dto.setTenantId(sub.getTenant().getId());
        }

        dto.setMonthlyRent(sub.getMonthlyRent());   // Double → Double
        dto.setNextDueDate(sub.getNextDueDate());
        dto.setStatus(sub.getStatus());
        dto.setMessage(message);

        return dto;
    }
}
