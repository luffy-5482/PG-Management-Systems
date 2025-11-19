package com.parent.staff.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.parent.config.SecurityUtils;
import com.parent.pg.model.PgEntity;
import com.parent.pg.repository.PgRepository;
import com.parent.staff.dto.StaffRequest;
import com.parent.staff.dto.StaffResponse;
import com.parent.staff.model.StaffEntity;
import com.parent.staff.repository.StaffRepository;

@Service
public class StaffServiceImpl implements StaffService {

    private final StaffRepository staffRepository;
    private final PgRepository pgRepository;
    private final PasswordEncoder passwordEncoder;

    public StaffServiceImpl(
            StaffRepository staffRepository,
            PgRepository pgRepository,
            PasswordEncoder passwordEncoder
    ) {
        this.staffRepository = staffRepository;
        this.pgRepository = pgRepository;
        this.passwordEncoder = passwordEncoder;
    }

    private StaffResponse toResponse(StaffEntity s) {
        StaffResponse r = new StaffResponse();
        r.setId(s.getId());
        r.setPgId(s.getPg().getId());
        r.setFullName(s.getFullName());
        r.setEmail(s.getEmail());
        r.setPhone(s.getPhone());
        r.setDesignation(s.getDesignation());
        r.setJoinDate(s.getJoinDate());
        r.setShiftTiming(s.getShiftTiming());
        r.setActive(s.getActive());
        return r;
    }

    @Override
    public StaffResponse createStaff(StaffRequest req) {
        Long ownerId = SecurityUtils.getLoggedInOwnerId();

        PgEntity pg = pgRepository.findByIdAndOwnerId(req.getPgId(), ownerId)
                .orElseThrow(() -> new RuntimeException("PG does not belong to this owner"));

        StaffEntity s = new StaffEntity();

        s.setPg(pg);
        s.setFullName(req.getFullName());
        s.setEmail(req.getEmail());
        s.setPhone(req.getPhone());
        s.setDesignation(req.getDesignation());
        s.setJoinDate(req.getJoinDate());
        s.setShiftTiming(req.getShiftTiming());
        s.setActive(req.getActive() != null ? req.getActive() : true);

        s.setPassword(passwordEncoder.encode(req.getPassword()));

        staffRepository.save(s);
        return toResponse(s);
    }

    @Override
    public StaffResponse updateStaff(Long id, StaffRequest req) {
        Long ownerId = SecurityUtils.getLoggedInOwnerId();

        StaffEntity s = staffRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Staff not found"));

        if (!s.getPg().getOwner().getId().equals(ownerId))
            throw new RuntimeException("Unauthorized");

        s.setFullName(req.getFullName());
        s.setPhone(req.getPhone());
        s.setDesignation(req.getDesignation());
        s.setJoinDate(req.getJoinDate());
        s.setShiftTiming(req.getShiftTiming());
        s.setActive(req.getActive());

        if (req.getPassword() != null && !req.getPassword().isBlank()) {
            s.setPassword(passwordEncoder.encode(req.getPassword()));
        }

        staffRepository.save(s);
        return toResponse(s);
    }

    @Override
    public void deleteStaff(Long id) {
        Long ownerId = SecurityUtils.getLoggedInOwnerId();

        StaffEntity s = staffRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Staff not found"));

        if (!s.getPg().getOwner().getId().equals(ownerId))
            throw new RuntimeException("Unauthorized");

        staffRepository.delete(s);
    }

    @Override
    public StaffResponse getStaff(Long id) {
        Long ownerId = SecurityUtils.getLoggedInOwnerId();

        StaffEntity s = staffRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Staff not found"));

        if (!s.getPg().getOwner().getId().equals(ownerId))
            throw new RuntimeException("Unauthorized");

        return toResponse(s);
    }

    @Override
    public List<StaffResponse> getStaffByPg(Long pgId) {
        Long ownerId = SecurityUtils.getLoggedInOwnerId();

        pgRepository.findByIdAndOwnerId(pgId, ownerId)
                .orElseThrow(() -> new RuntimeException("Unauthorised PG"));

        return staffRepository.findByPgId(pgId)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }
}
