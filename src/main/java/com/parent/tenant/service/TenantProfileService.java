package com.parent.tenant.service;
import org.springframework.stereotype.Service;

import com.parent.tenant.model.TenantProfile;
import com.parent.tenant.repository.TenantProfileRepository;

import jakarta.transaction.Transactional;

@Service
public class TenantProfileService {

    private final TenantProfileRepository profileRepo;

    public TenantProfileService(TenantProfileRepository profileRepo) {
        this.profileRepo = profileRepo;
    }

    // ---------------------------------
    // VIEW PROFILE (POST-LOGIN)
    // ---------------------------------
    public TenantProfile getProfile(Long tenantId) {

        return profileRepo.findByTenantId(tenantId)
                .orElseThrow(() ->
                        new IllegalStateException("Profile not found"));
    }

    // ---------------------------------
    // UPDATE PROFILE (POST-LOGIN)
    // ---------------------------------
    @Transactional
    public TenantProfile updateProfile(Long tenantId, TenantProfile incoming) {

        TenantProfile existing = profileRepo.findByTenantId(tenantId)
                .orElseThrow(() ->
                        new IllegalStateException("Profile not found"));

        // 🔒 SAFE FIELDS ONLY
        existing.setFirstName(incoming.getFirstName());
        existing.setLastName(incoming.getLastName());
        existing.setDob(incoming.getDob());
        existing.setGender(incoming.getGender());
        existing.setOccupation(incoming.getOccupation());

        existing.setAddress(incoming.getAddress());
        existing.setCity(incoming.getCity());
        existing.setPincode(incoming.getPincode());

        existing.setEmergencyContact(incoming.getEmergencyContact());
        existing.setPhotoUrl(incoming.getPhotoUrl());

        return profileRepo.save(existing);
    }
}
