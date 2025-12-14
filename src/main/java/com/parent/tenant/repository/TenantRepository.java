package com.parent.tenant.repository;

import com.parent.tenant.model.TenantEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.*;

import java.util.Optional;

public interface TenantRepository extends JpaRepository<TenantEntity, Long> {

    Optional<TenantEntity> findByEmail(String email);

    long countByRoom_IdAndActiveTrue(Long roomId);

    Page<TenantEntity> findByRoom_Id(Long roomId, Pageable pageable);

    Page<TenantEntity> findByActiveTrue(Pageable pageable);

    Page<TenantEntity> findByNameContainingIgnoreCase(String name, Pageable pageable);
}
