package com.parent.manager.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.parent.manager.model.Attendance;

public interface AttendanceRepository extends JpaRepository<Attendance, Long> {
    List<Attendance> findByManagerIdAndDate(Long managerId, java.time.LocalDate date);
    List<Attendance> findByStaffId(Long staffId);
}
