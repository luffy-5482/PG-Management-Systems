package com.parent.tenant.service;

import com.parent.tenant.dto.EmergencyContactDto;
import com.parent.tenant.dto.TenantProfileDto;
import org.springframework.web.multipart.MultipartFile;

public interface ProfileService {
    TenantProfileDto getProfile(Long tenantId);
    TenantProfileDto updateProfile(Long tenantId, TenantProfileDto update);
    String saveProfilePhoto(Long tenantId, MultipartFile file);
    EmergencyContactDto addEmergencyContact(Long tenantId, EmergencyContactDto contact);
    EmergencyContactDto updateEmergencyContact(Long tenantId, Long contactId, EmergencyContactDto contact);
    void deleteEmergencyContact(Long tenantId, Long contactId);
    void changePassword(Long tenantId, String currentPassword, String newPassword);
}
