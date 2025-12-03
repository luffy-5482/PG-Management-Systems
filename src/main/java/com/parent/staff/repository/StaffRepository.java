package com.parent.staff.repository;

import com.parent.staff.model.Staff;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StaffRepository extends JpaRepository<Staff, Long> {
    List<Staff> findByPgId(Long pgId);
}
