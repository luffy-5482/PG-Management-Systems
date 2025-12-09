package com.parent.payment.repository;

import com.parent.payment.model.TenantSubscription;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TenantSubscriptionRepository extends JpaRepository<TenantSubscription, Long> {

    Optional<TenantSubscription> findByTenant_Id(Long tenantId);
}
