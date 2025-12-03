package com.parent.pg.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

import com.parent.pg.model.PgEntity;

public interface PgRepository extends JpaRepository<PgEntity, Long> {

    // 🔹 Owner-based filtering (required)
    List<PgEntity> findByOwnerId(Long ownerId);
    Optional<PgEntity> findByIdAndOwnerId(Long id, Long ownerId);

    // 🔹 Your existing filters
    List<PgEntity> findByAddress_City(String city);
    List<PgEntity> findByTypeAndAvailability(String type, Boolean availability);
    List<PgEntity> findAllById(Iterable<Long> ids);

    
}
