package com.parent.tenant.controller;

import com.parent.tenant.dto.*;
import com.parent.tenant.service.TenantSettingsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tenant/{tenantId}/settings")
public class TenantSettingsController {

    private final TenantSettingsService service;

    public TenantSettingsController(TenantSettingsService service) {
        this.service = service;
    }

    // profile
    @GetMapping("/profile")
    public ResponseEntity<TenantProfileDto> getProfile(@PathVariable Long tenantId) {
        return ResponseEntity.ok(service.getProfile(tenantId));
    }

    @PutMapping("/profile")
    public ResponseEntity<TenantProfileDto> updateProfile(@PathVariable Long tenantId,
                                                          @RequestBody TenantProfileDto update) {
        return ResponseEntity.ok(service.updateProfile(tenantId, update));
    }

    // emergency contacts
    @GetMapping("/contacts")
    public ResponseEntity<List<EmergencyContactDto>> listContacts(@PathVariable Long tenantId) {
        return ResponseEntity.ok(service.listEmergencyContacts(tenantId));
    }

    @PostMapping("/contacts")
    public ResponseEntity<EmergencyContactDto> addContact(@PathVariable Long tenantId,
                                                          @RequestBody EmergencyContactDto dto) {
        return ResponseEntity.ok(service.addEmergencyContact(tenantId, dto));
    }

    @PutMapping("/contacts/{contactId}")
    public ResponseEntity<EmergencyContactDto> updateContact(@PathVariable Long tenantId,
                                                             @PathVariable Long contactId,
                                                             @RequestBody EmergencyContactDto dto) {
        return ResponseEntity.ok(service.updateEmergencyContact(tenantId, contactId, dto));
    }

    @DeleteMapping("/contacts/{contactId}")
    public ResponseEntity<Void> deleteContact(@PathVariable Long tenantId,
                                              @PathVariable Long contactId) {
        service.deleteEmergencyContact(tenantId, contactId);
        return ResponseEntity.noContent().build();
    }

    // password
    @PostMapping("/change-password")
    public ResponseEntity<Void> changePassword(@PathVariable Long tenantId,
                                               @RequestBody ChangePasswordRequest req) {
        service.changePassword(tenantId, req);
        return ResponseEntity.ok().build();
    }

    // notification prefs
    @GetMapping("/notification-prefs")
    public ResponseEntity<NotificationPrefsDto> getPrefs(@PathVariable Long tenantId) {
        return ResponseEntity.ok(service.getNotificationPrefs(tenantId));
    }

    @PutMapping("/notification-prefs")
    public ResponseEntity<NotificationPrefsDto> updatePrefs(@PathVariable Long tenantId,
                                                            @RequestBody NotificationPrefsDto req) {
        return ResponseEntity.ok(service.updateNotificationPrefs(tenantId, req));
    }

    // privacy
    @GetMapping("/privacy")
    public ResponseEntity<PrivacySettingsDto> getPrivacy(@PathVariable Long tenantId) {
        return ResponseEntity.ok(service.getPrivacySettings(tenantId));
    }

    @PutMapping("/privacy")
    public ResponseEntity<PrivacySettingsDto> updatePrivacy(@PathVariable Long tenantId,
                                                            @RequestBody PrivacySettingsDto req) {
        return ResponseEntity.ok(service.updatePrivacySettings(tenantId, req));
    }
}
