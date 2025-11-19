package com.parent.staff.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.parent.staff.model.StaffEntity;

public interface StaffRepository extends JpaRepository<StaffEntity, Long> {

    Optional<StaffEntity> findByEmail(String email);

    List<StaffEntity> findByPgId(Long pgId);
}
