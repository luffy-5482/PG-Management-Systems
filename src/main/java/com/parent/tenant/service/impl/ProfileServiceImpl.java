package com.parent.tenant.service.impl;

import com.parent.tenant.dto.EmergencyContactDto;
import com.parent.tenant.dto.TenantProfileDto;
import com.parent.tenant.service.ProfileService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ProfileServiceImpl implements ProfileService {

    @Override
    public TenantProfileDto getProfile(Long tenantId) {
        throw new UnsupportedOperationException("Profile feature not implemented yet");
    }

    @Override
    public TenantProfileDto updateProfile(Long tenantId, TenantProfileDto update) {
        throw new UnsupportedOperationException("Profile feature not implemented yet");
    }

    @Override
    public String saveProfilePhoto(Long tenantId, MultipartFile file) {
        throw new UnsupportedOperationException("Profile feature not implemented yet");
    }

    @Override
    public EmergencyContactDto addEmergencyContact(Long tenantId, EmergencyContactDto contact) {
        throw new UnsupportedOperationException("Profile feature not implemented yet");
    }

    @Override
    public EmergencyContactDto updateEmergencyContact(Long tenantId, Long contactId, EmergencyContactDto contact) {
        throw new UnsupportedOperationException("Profile feature not implemented yet");
    }

    @Override
    public void deleteEmergencyContact(Long tenantId, Long contactId) {
        throw new UnsupportedOperationException("Profile feature not implemented yet");
    }

    @Override
    public void changePassword(Long tenantId, String currentPassword, String newPassword) {
        throw new UnsupportedOperationException("Profile feature not implemented yet");
    }
}
