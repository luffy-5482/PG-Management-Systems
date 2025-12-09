package com.parent.tenant.service.impl;

import com.parent.payment.model.Tenant;
import com.parent.payment.repository.TenantRepository;
import com.parent.tenant.dto.TenantProfileDto;
import com.parent.tenant.dto.TenantRoomDetailsDto;
import com.parent.tenant.service.TenantProfileService;
import org.springframework.stereotype.Service;

@Service
public class TenantProfileServiceImpl implements TenantProfileService {

    private final TenantRepository tenantRepository;

    public TenantProfileServiceImpl(TenantRepository tenantRepository) {
        this.tenantRepository = tenantRepository;
    }

    @Override
    public TenantProfileDto getProfile(Long tenantId) {
        Tenant tenant = tenantRepository.findById(tenantId)
                .orElseThrow(() -> new RuntimeException("Tenant not found with id: " + tenantId));

        return mapToDto(tenant);
    }

    @Override
    public TenantProfileDto updateProfile(Long tenantId, TenantProfileDto update) {
        Tenant tenant = tenantRepository.findById(tenantId)
                .orElseThrow(() -> new RuntimeException("Tenant not found with id: " + tenantId));

        applyUpdate(tenant, update);

        Tenant saved = tenantRepository.save(tenant);
        return mapToDto(saved);
    }

    // ----------------- helper methods -----------------

    private TenantProfileDto mapToDto(Tenant tenant) {
        TenantProfileDto dto = new TenantProfileDto();
        dto.setId(tenant.getId());
        dto.setName(tenant.getName());
        dto.setEmail(tenant.getEmail());
        dto.setContact(tenant.getContact());

        // extra basic fields
        dto.setDateOfBirth(tenant.getDateOfBirth());
        dto.setGender(tenant.getGender());
        dto.setOccupation(tenant.getOccupation());

        // For now: no room mapping, just send null
        dto.setRoomDetails((TenantRoomDetailsDto) null);

        return dto;
    }

    private void applyUpdate(Tenant tenant, TenantProfileDto update) {
        if (update.getName() != null) {
            tenant.setName(update.getName());
        }
        if (update.getEmail() != null) {
            tenant.setEmail(update.getEmail());
        }
        if (update.getContact() != null) {
            tenant.setContact(update.getContact());
        }

        if (update.getDateOfBirth() != null) {
            tenant.setDateOfBirth(update.getDateOfBirth());
        }
        if (update.getGender() != null) {
            tenant.setGender(update.getGender());
        }
        if (update.getOccupation() != null) {
            tenant.setOccupation(update.getOccupation());
        }

        // ⚠️ Not touching room / payment info from here.
    }
}
