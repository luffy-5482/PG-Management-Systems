package com.parent.admin.repository;

import com.parent.admin.model.Admin;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AdminRepository extends JpaRepository<Admin, Long> {
    Optional<Admin> findByEmail(String email);

    // convenience: find by id or email (optional)
    default Optional<Admin> findByIdOrEmail(String idOrEmail) {
        try {
            Long id = Long.valueOf(idOrEmail);
            return findById(id);
        } catch (NumberFormatException ex) {
            return findByEmail(idOrEmail);
        }
    }
}
