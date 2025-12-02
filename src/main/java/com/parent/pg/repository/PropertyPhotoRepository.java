package com.parent.pg.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.parent.pg.model.PropertyPhoto;

@Repository
public interface PropertyPhotoRepository extends JpaRepository<PropertyPhoto, Long> {

    // Required for owner-based protection
    List<PropertyPhoto> findByPgOwnerId(Long ownerId);

    Optional<PropertyPhoto> findByIdAndPgOwnerId(Long id, Long ownerId);
}