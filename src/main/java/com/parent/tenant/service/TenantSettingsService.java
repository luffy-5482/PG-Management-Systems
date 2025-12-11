package com.parent.tenant.service;

import com.parent.tenant.dto.*;
import java.util.List;

public interface TenantSettingsService {

    // profile
    com.parent.tenant.dto.TenantProfileDto getProfile(Long tenantId);
    com.parent.tenant.dto.TenantProfileDto updateProfile(Long tenantId, com.parent.tenant.dto.TenantProfileDto update);

    // emergency contacts
    List<com.parent.tenant.dto.EmergencyContactDto> listEmergencyContacts(Long tenantId);
    com.parent.tenant.dto.EmergencyContactDto addEmergencyContact(Long tenantId, com.parent.tenant.dto.EmergencyContactDto req);
    com.parent.tenant.dto.EmergencyContactDto updateEmergencyContact(Long tenantId, Long contactId, com.parent.tenant.dto.EmergencyContactDto req);
    void deleteEmergencyContact(Long tenantId, Long contactId);

    // notification prefs
    com.parent.tenant.dto.NotificationPrefsDto getNotificationPrefs(Long tenantId);
    com.parent.tenant.dto.NotificationPrefsDto updateNotificationPrefs(Long tenantId, com.parent.tenant.dto.NotificationPrefsDto prefs);

    // privacy settings
    com.parent.tenant.dto.PrivacySettingsDto getPrivacySettings(Long tenantId);
    com.parent.tenant.dto.PrivacySettingsDto updatePrivacySettings(Long tenantId, com.parent.tenant.dto.PrivacySettingsDto privacy);

    // password change
    void changePassword(Long tenantId, com.parent.tenant.dto.ChangePasswordRequest req);
}
