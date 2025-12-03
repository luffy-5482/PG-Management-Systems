package com.parent.manager.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.parent.manager.model.ManagerTask;

public interface ManagerTaskRepository extends JpaRepository<ManagerTask, Long> {
    List<ManagerTask> findByPgId(Long pgId);
    List<ManagerTask> findByStaffId(Long staffId);
}
