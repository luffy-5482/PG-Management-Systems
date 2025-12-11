package com.parent.tenant.controller;

import com.parent.tenant.dto.ChangePasswordRequest;
import com.parent.tenant.dto.NotificationPrefsDto;
import com.parent.tenant.dto.PrivacySettingsDto;
import com.parent.tenant.service.PasswordService;
import com.parent.tenant.service.TenantEmergencyContactService;
import com.parent.tenant.service.TenantSettingsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
// Changed to avoid ambiguous mapping with TenantSettingsController
@RequestMapping("/api/tenant/{tenantId}/account/settings")
public class AccountSettingsController {

    private final TenantSettingsService settingsService;
    private final PasswordService passwordService;
    private final TenantEmergencyContactService emergencyService;

    public AccountSettingsController(TenantSettingsService settingsService,
                                     PasswordService passwordService,
                                     TenantEmergencyContactService emergencyService) {
        this.settingsService = settingsService;
        this.passwordService = passwordService;
        this.emergencyService = emergencyService;
    }

    // ---- Notification prefs ----
    @GetMapping("/notifications")
    public NotificationPrefsDto getNotificationPrefs(@PathVariable Long tenantId) {
        return settingsService.getNotificationPrefs(tenantId);
    }

    @PutMapping("/notifications")
    public NotificationPrefsDto updateNotificationPrefs(@PathVariable Long tenantId,
                                                        @RequestBody NotificationPrefsDto dto) {
        return settingsService.updateNotificationPrefs(tenantId, dto);
    }

    // ---- Privacy settings ----
    @GetMapping("/privacy")
    public PrivacySettingsDto getPrivacy(@PathVariable Long tenantId) {
        return settingsService.getPrivacySettings(tenantId);
    }

    @PutMapping("/privacy")
    public PrivacySettingsDto updatePrivacy(@PathVariable Long tenantId,
                                            @RequestBody PrivacySettingsDto dto) {
        return settingsService.updatePrivacySettings(tenantId, dto);
    }

    // ---- Emergency contacts (delegates to TenantEmergencyContactController/service) ----
    // Reuse existing routes or call service here (you already have controller previously).
    // e.g., GET /settings/emergency is defined in EmergencyContactController that we created.

    // ---- Change password ----
    @PostMapping("/change-password")
    public ResponseEntity<?> changePassword(@PathVariable Long tenantId,
                                            @RequestBody ChangePasswordRequest req) {
        passwordService.changeTenantPassword(tenantId, req);
        return ResponseEntity.ok().build();
    }
}
