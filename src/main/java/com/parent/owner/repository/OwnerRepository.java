package com.parent.owner.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.parent.owner.model.Owner;

public interface OwnerRepository extends JpaRepository<Owner, Long> {
    Optional<Owner> findByEmail(String email);

    // optional helper for future security
    Optional<Owner> findByIdAndEmail(Long id, String email);
}
