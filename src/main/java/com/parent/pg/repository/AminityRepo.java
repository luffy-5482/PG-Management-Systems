package com.parent.pg.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.parent.pg.model.Amenity;

public interface AminityRepo extends JpaRepository<Amenity, Long> {

    // 🔥 Owner-based filtering
    List<Amenity> findByPgOwnerId(Long ownerId);

    Optional<Amenity> findByIdAndPgOwnerId(Long id, Long ownerId);
}
