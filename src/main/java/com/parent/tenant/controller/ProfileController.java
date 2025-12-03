package com.parent.tenant.controller;

import com.parent.tenant.dto.*;
import com.parent.tenant.service.ProfileService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/tenant")
public class ProfileController {

    private final ProfileService profileService;

    public ProfileController(ProfileService profileService) {
        this.profileService = profileService;
    }

    @GetMapping("/{id}/profile")
    public ResponseEntity<?> getProfile(@PathVariable("id") Long tenantId) {
        TenantProfileDto dto = profileService.getProfile(tenantId);
        if (dto == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(dto);
    }

    @PutMapping("/{id}/profile")
    public ResponseEntity<?> updateProfile(@PathVariable("id") Long tenantId,
                                           @RequestBody TenantProfileDto update) {
        TenantProfileDto dto = profileService.updateProfile(tenantId, update);
        if (dto == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(dto);
    }

    @PostMapping("/{id}/profile/photo")
    public ResponseEntity<?> uploadPhoto(@PathVariable("id") Long tenantId,
                                         @RequestParam("file") MultipartFile file) {
        String url = profileService.saveProfilePhoto(tenantId, file);
        return ResponseEntity.ok(java.util.Map.of("url", url));
    }

    @PostMapping("/{id}/profile/emergency")
    public ResponseEntity<?> addContact(@PathVariable("id") Long tenantId,
                                        @RequestBody EmergencyContactDto contact) {
        EmergencyContactDto saved = profileService.addEmergencyContact(tenantId, contact);
        return ResponseEntity.ok(saved);
    }

    @PutMapping("/{id}/profile/emergency/{contactId}")
    public ResponseEntity<?> updateContact(@PathVariable("id") Long tenantId,
                                           @PathVariable("contactId") Long contactId,
                                           @RequestBody EmergencyContactDto contact) {
        EmergencyContactDto updated = profileService.updateEmergencyContact(tenantId, contactId, contact);
        if (updated == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}/profile/emergency/{contactId}")
    public ResponseEntity<?> deleteContact(@PathVariable("id") Long tenantId,
                                           @PathVariable("contactId") Long contactId) {
        profileService.deleteEmergencyContact(tenantId, contactId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/profile/change-password")
    public ResponseEntity<?> changePassword(@PathVariable("id") Long tenantId,
                                            @RequestParam("current") String current,
                                            @RequestParam("newPassword") String newPassword) {
        profileService.changePassword(tenantId, current, newPassword);
        return ResponseEntity.ok().build();
    }
}
