package com.parent.staff.service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.parent.manager.model.Manager;
import com.parent.manager.repository.ManagerRepository;
import com.parent.staff.dto.StaffRequest;
import com.parent.staff.dto.StaffResponse;
import com.parent.staff.model.Staff;
import com.parent.staff.repository.StaffRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class StaffService {

    private final StaffRepository repo;
    private final ManagerRepository managerRepo;

    public StaffService(StaffRepository repo, ManagerRepository managerRepo) {
        this.repo = repo;
        this.managerRepo = managerRepo;
    }

    public StaffResponse createStaff(StaffRequest req, Long managerId, Long ownerId) {

        // Manager PG validation
        if (managerId != null) {
            Manager manager = managerRepo.findById(managerId)
                    .orElseThrow(() -> new EntityNotFoundException("Manager not found"));

            if (req.getPgId() == null || !manager.getAllowedPgIds().contains(req.getPgId())) {
                throw new RuntimeException("Manager cannot create staff for another PG");
            }
        }

        Staff s = new Staff();
        s.setFullName(req.getFullName());
        s.setPhone(req.getPhone());
        s.setRole(req.getRole());
        s.setPgId(req.getPgId());
        s.setJoinDate(LocalDate.now());

        return toResponse(repo.save(s));
    }

    public StaffResponse updateStaff(Long id, StaffRequest req, Long managerId, Long ownerId) {

        Staff s = repo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Staff not found"));

        // Manager PG validation
        if (managerId != null) {
            Manager manager = managerRepo.findById(managerId)
                    .orElseThrow(() -> new EntityNotFoundException("Manager not found"));

            if (!manager.getAllowedPgIds().contains(s.getPgId())) {
                throw new RuntimeException("Manager cannot update staff of another PG");
            }
        }

        if (req.getFullName() != null) s.setFullName(req.getFullName());
        if (req.getPhone() != null) s.setPhone(req.getPhone());
        if (req.getRole() != null) s.setRole(req.getRole());
        // Managers are not allowed to change staff.pgId — only owner can if you implement that.

        return toResponse(repo.save(s));
    }

    public void deleteStaff(Long id, Long managerId, Long ownerId) {

        Staff s = repo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Staff not found"));

        // Manager PG validation
        if (managerId != null) {
            Manager manager = managerRepo.findById(managerId)
                    .orElseThrow(() -> new EntityNotFoundException("Manager not found"));

            if (!manager.getAllowedPgIds().contains(s.getPgId())) {
                throw new RuntimeException("Manager cannot delete staff of another PG");
            }
        }

        repo.delete(s);
    }

    public StaffResponse getStaff(Long id) {
        return toResponse(repo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Staff not found")));
    }

    public List<StaffResponse> getStaffByPg(Long pgId) {
        return repo.findByPgId(pgId)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    /**
     * Helper used by controller to validate if a manager has access to a pg.
     * Returns true if manager exists and manager.allowedPgIds contains pgId.
     */
    public boolean managerCanAccessPg(Long managerId, Long pgId) {
        if (managerId == null) return false;
        Manager manager = managerRepo.findById(managerId)
                .orElseThrow(() -> new EntityNotFoundException("Manager not found"));
        return manager.getAllowedPgIds() != null && manager.getAllowedPgIds().contains(pgId);
    }

    private StaffResponse toResponse(Staff s) {
        StaffResponse r = new StaffResponse();
        r.setId(s.getId());
        r.setFullName(s.getFullName());
        r.setPhone(s.getPhone());
        r.setRole(s.getRole());
        r.setPgId(s.getPgId());
        r.setJoinDate(s.getJoinDate());
        return r;
    }
}
