package com.parent.owner.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.parent.config.SecurityUtils;
import com.parent.owner.dto.OwnerRequest;
import com.parent.owner.dto.OwnerResponse;
import com.parent.owner.model.Owner;
import com.parent.owner.repository.OwnerRepository;
import com.parent.pg.dto.PgResponse;

@Service
public class OwnerServiceImpl implements OwnerService {

    @Autowired
    private OwnerRepository ownerRepository;

    @Autowired
    private com.parent.pg.service.PgService pgService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // ---------------------------------------------------------
    // 🔐 Get logged-in ownerId from JWT token
    // ---------------------------------------------------------
    private Long getOwnerId() {
        Long id = SecurityUtils.getLoggedInOwnerId();
        if (id == null)
            throw new RuntimeException("Unauthorized: Owner not found in token");
        return id;
    }

    // ---------------------------------------------------------
    // 🔁 Convert Owner → OwnerResponse (unchanged)
    // ---------------------------------------------------------
    private OwnerResponse toOwnerRes(Owner o) {
        List<PgResponse> pgList = (o.getPgs() == null) ? List.of()
                : o.getPgs().stream()
                        .map(pg -> pgService.getPgById(pg.getId()))
                        .collect(Collectors.toList());

        return new OwnerResponse(
                o.getId(),
                o.getFullName(),
                o.getEmail(),
                o.getPhoneNumber(),
                o.getGender(),
                pgList
        );
    }

    // ---------------------------------------------------------
    // 🔁 Apply OwnerRequest → Owner (unchanged)
    // ---------------------------------------------------------
    private void applyRequest(OwnerRequest r, Owner o) {
        o.setFullName(r.getFullName());
        o.setEmail(r.getEmail());
        o.setPhoneNumber(r.getPhoneNumber());
        o.setGender(r.getGender());

        // Only update password if provided
        if (r.getPassword() != null && !r.getPassword().isEmpty()) {
            o.setPassword(passwordEncoder.encode(r.getPassword()));
        }
    }

    // ---------------------------------------------------------
    // 🔥 Get ALL owners (SECURE: return only logged-in owner)
    // ---------------------------------------------------------
    @Override
    public List<OwnerResponse> getAllOwners() {
        Long ownerId = getOwnerId();

        Owner owner = ownerRepository.findById(ownerId)
                .orElseThrow(() -> new RuntimeException("Owner not found")); 

        return List.of(toOwnerRes(owner));
    }

    // ---------------------------------------------------------
    // 🔥 Get Owner by ID (SECURE: must be the same owner)
    // ---------------------------------------------------------
    @Override
    public OwnerResponse getOwnerById(Long id) {
        Long ownerId = getOwnerId();

        if (!id.equals(ownerId)) {
            throw new RuntimeException("Access Denied: You can only access your own account");
        }

        Owner owner = ownerRepository.findById(ownerId)
                .orElseThrow(() -> new RuntimeException("Owner not found"));

        return toOwnerRes(owner);
    }

    // ---------------------------------------------------------
    // 🔥 Update Owner (SECURE: only logged-in owner)
    // ---------------------------------------------------------
    @Override
    public OwnerResponse updateOwner(Long id, OwnerRequest request) {
        Long ownerId = getOwnerId();

        if (!id.equals(ownerId)) {
            throw new RuntimeException("Access Denied: You can only update your own account");
        }

        Owner o = ownerRepository.findById(ownerId)
                .orElseThrow(() -> new RuntimeException("Owner not found"));

        applyRequest(request, o);
        Owner saved = ownerRepository.save(o);

        return toOwnerRes(saved);
    }

    // ---------------------------------------------------------
    // 🔥 Delete Owner account (SECURE: only own account)
    // ---------------------------------------------------------
    @Override
    public void deleteOwner(Long id) {
        Long ownerId = getOwnerId();

        if (!id.equals(ownerId)) {
            throw new RuntimeException("Access Denied: You can only delete your own account");
        }

        Owner owner = ownerRepository.findById(ownerId)
                .orElseThrow(() -> new RuntimeException("Owner not found"));

        ownerRepository.delete(owner);
    }
}
