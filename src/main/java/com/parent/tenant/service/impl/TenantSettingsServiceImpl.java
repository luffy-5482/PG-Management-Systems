package com.parent.tenant.service.impl;

import com.parent.tenant.dto.EmergencyContactDto;
import com.parent.tenant.dto.NotificationPrefsDto;
import com.parent.tenant.dto.PrivacySettingsDto;
import com.parent.tenant.dto.TenantProfileDto;
import com.parent.tenant.dto.TenantRoomDetailsDto;
import com.parent.tenant.dto.ChangePasswordRequest;
import com.parent.tenant.model.TenantEmergencyContact;
import com.parent.tenant.model.TenantNotificationPrefs;
import com.parent.tenant.model.TenantPrivacySettings;
import com.parent.tenant.repository.TenantEmergencyContactRepository;
import com.parent.tenant.repository.TenantNotificationPrefsRepository;
import com.parent.tenant.repository.TenantPrivacySettingsRepository;
import com.parent.tenant.repository.TenantCredentialsRepository;
import com.parent.tenant.service.TenantSettingsService;
import com.parent.tenant.service.PasswordService;
import com.parent.payment.model.Tenant;
import com.parent.payment.repository.TenantRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TenantSettingsServiceImpl implements TenantSettingsService {

    private final TenantRepository tenantRepository;
    private final TenantEmergencyContactRepository contactRepo;
    private final TenantNotificationPrefsRepository notifRepo;
    private final TenantPrivacySettingsRepository privacyRepo;
    private final TenantCredentialsRepository credsRepo;
    private final PasswordService passwordService;

    public TenantSettingsServiceImpl(
            TenantRepository tenantRepository,
            TenantEmergencyContactRepository contactRepo,
            TenantNotificationPrefsRepository notifRepo,
            TenantPrivacySettingsRepository privacyRepo,
            TenantCredentialsRepository credsRepo,
            PasswordService passwordService
    ) {
        this.tenantRepository = tenantRepository;
        this.contactRepo = contactRepo;
        this.notifRepo = notifRepo;
        this.privacyRepo = privacyRepo;
        this.credsRepo = credsRepo;
        this.passwordService = passwordService;
    }

    // ---------------- PROFILE ----------------
    @Override
    public TenantProfileDto getProfile(Long tenantId) {
        Tenant t = tenantRepository.findById(tenantId)
                .orElseThrow(() -> new RuntimeException("Tenant not found: " + tenantId));

        TenantProfileDto dto = new TenantProfileDto();
        dto.setId(t.getId());
        dto.setName(t.getName());
        dto.setEmail(t.getEmail());
        dto.setContact(t.getContact());
        dto.setDateOfBirth(t.getDateOfBirth());
        dto.setGender(t.getGender());
        dto.setOccupation(t.getOccupation());
        dto.setSubscriptionBlocked(t.getSubscriptionBlocked());
        dto.setNextDueDate(t.getNextDueDate());

        // --- monthlySubscriptionAmount: entity stores Integer, DTO expects Double ---
        Integer monthlyInt = t.getMonthlySubscriptionAmount(); // may be null
        dto.setMonthlySubscriptionAmount(monthlyInt); // because DTO expects Integer


        // --- Room details (TenantRoomDetailsDto.rent is Integer) ---
        TenantRoomDetailsDto rd = null;
        if (t.getRoom() != null) {
            rd = new TenantRoomDetailsDto();
            rd.setRoomNumber(t.getRoom());

            // Use Integer values from the model (avoid mixing Double/Integer in ternaries)
            rd.setRent(monthlyInt);                 // Integer or null
            rd.setDueAmount(t.getDue());            // Integer or null
            rd.setMoveInDate(t.getJoinDate());      // LocalDate or null
            rd.setStatus(t.getStatus());
            // bookingId/roomId/pgName/floorName/sharingType/pgAddress left blank if not available
        }
        dto.setRoomDetails(rd);

        return dto;
    }

    @Override
    @Transactional
    public TenantProfileDto updateProfile(Long tenantId, TenantProfileDto update) {
        Tenant t = tenantRepository.findById(tenantId)
                .orElseThrow(() -> new RuntimeException("Tenant not found: " + tenantId));
        if (update.getName() != null) t.setName(update.getName());
        if (update.getEmail() != null) t.setEmail(update.getEmail());
        if (update.getContact() != null) t.setContact(update.getContact());
        if (update.getDateOfBirth() != null) t.setDateOfBirth(update.getDateOfBirth());
        if (update.getGender() != null) t.setGender(update.getGender());
        if (update.getOccupation() != null) t.setOccupation(update.getOccupation());
        tenantRepository.save(t);
        return getProfile(tenantId);
    }

    // -------------- EMERGENCY CONTACTS --------------
    @Override
    public List<EmergencyContactDto> listEmergencyContacts(Long tenantId) {
        // repository method: findByTenant_IdOrderByCreatedAtDesc
        return contactRepo.findByTenant_IdOrderByCreatedAtDesc(tenantId)
                .stream()
                .map(this::toEmergencyDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public EmergencyContactDto addEmergencyContact(Long tenantId, EmergencyContactDto req) {
        tenantRepository.findById(tenantId)
                .orElseThrow(() -> new RuntimeException("Tenant not found: " + tenantId));
        TenantEmergencyContact c = new TenantEmergencyContact();
        c.setTenantId(tenantId);
        c.setName(req.getName());
        c.setRelationship(req.getRelationship());
        c.setPhone(req.getPhone());
        c.setEmail(req.getEmail());
        c.setCreatedAt(Instant.now());
        TenantEmergencyContact saved = contactRepo.save(c);
        return toEmergencyDto(saved);
    }

    @Override
    @Transactional
    public EmergencyContactDto updateEmergencyContact(Long tenantId, Long contactId, EmergencyContactDto req) {
        TenantEmergencyContact c = contactRepo.findById(contactId)
                .orElseThrow(() -> new RuntimeException("Contact not found: " + contactId));
        if (!c.getTenantId().equals(tenantId)) throw new RuntimeException("Not allowed");
        if (req.getName() != null) c.setName(req.getName());
        if (req.getRelationship() != null) c.setRelationship(req.getRelationship());
        if (req.getPhone() != null) c.setPhone(req.getPhone());
        if (req.getEmail() != null) c.setEmail(req.getEmail());
        TenantEmergencyContact saved = contactRepo.save(c);
        return toEmergencyDto(saved);
    }

    @Override
    @Transactional
    public void deleteEmergencyContact(Long tenantId, Long contactId) {
        TenantEmergencyContact c = contactRepo.findById(contactId)
                .orElseThrow(() -> new RuntimeException("Contact not found: " + contactId));
        if (!c.getTenantId().equals(tenantId)) throw new RuntimeException("Not allowed");
        contactRepo.deleteById(contactId);
    }

    private EmergencyContactDto toEmergencyDto(TenantEmergencyContact c) {
        EmergencyContactDto dto = new EmergencyContactDto();
        dto.setId(c.getId());
        dto.setName(c.getName());
        dto.setRelationship(c.getRelationship());
        dto.setPhone(c.getPhone());
        dto.setEmail(c.getEmail());
        return dto;
    }

    // -------------- NOTIFICATION PREFS --------------
    @Override
    public NotificationPrefsDto getNotificationPrefs(Long tenantId) {
        Optional<TenantNotificationPrefs> opt = notifRepo.findById(tenantId);
        if (opt.isEmpty()) {
            NotificationPrefsDto d = new NotificationPrefsDto();
            d.setPaymentAlerts(true);
            d.setMaintenanceAlerts(true);
            d.setNoticeAlerts(true);
            d.setGeneralAlerts(true);
            return d;
        }
        TenantNotificationPrefs p = opt.get();
        NotificationPrefsDto d = new NotificationPrefsDto();
        d.setPaymentAlerts(p.getPaymentAlerts());
        d.setMaintenanceAlerts(p.getMaintenanceAlerts());
        d.setNoticeAlerts(p.getNoticeAlerts());
        d.setGeneralAlerts(p.getGeneralAlerts());
        return d;
    }

    @Override
    @Transactional
    public NotificationPrefsDto updateNotificationPrefs(Long tenantId, NotificationPrefsDto dto) {
        TenantNotificationPrefs p = notifRepo.findById(tenantId).orElseGet(() -> {
            TenantNotificationPrefs np = new TenantNotificationPrefs();
            np.setTenantId(tenantId);
            return np;
        });

        if (dto.getPaymentAlerts() != null) p.setPaymentAlerts(dto.getPaymentAlerts());
        if (dto.getMaintenanceAlerts() != null) p.setMaintenanceAlerts(dto.getMaintenanceAlerts());
        if (dto.getNoticeAlerts() != null) p.setNoticeAlerts(dto.getNoticeAlerts());
        if (dto.getGeneralAlerts() != null) p.setGeneralAlerts(dto.getGeneralAlerts());
        p.setUpdatedAt(Instant.now());
        TenantNotificationPrefs saved = notifRepo.save(p);

        NotificationPrefsDto out = new NotificationPrefsDto();
        out.setPaymentAlerts(saved.getPaymentAlerts());
        out.setMaintenanceAlerts(saved.getMaintenanceAlerts());
        out.setNoticeAlerts(saved.getNoticeAlerts());
        out.setGeneralAlerts(saved.getGeneralAlerts());
        return out;
    }

    // -------------- PRIVACY SETTINGS --------------
    @Override
    public PrivacySettingsDto getPrivacySettings(Long tenantId) {
        Optional<TenantPrivacySettings> opt = privacyRepo.findById(tenantId);
        if (opt.isEmpty()) {
            PrivacySettingsDto d = new PrivacySettingsDto();
            d.setShowPhone(false);
            d.setShowEmail(true);
            d.setShowProfile(true);
            return d;
        }
        TenantPrivacySettings p = opt.get();
        PrivacySettingsDto d = new PrivacySettingsDto();
        d.setShowPhone(p.getShowPhone());
        d.setShowEmail(p.getShowEmail());
        d.setShowProfile(p.getShowProfile());
        return d;
    }

    @Override
    @Transactional
    public PrivacySettingsDto updatePrivacySettings(Long tenantId, PrivacySettingsDto dto) {
        TenantPrivacySettings p = privacyRepo.findById(tenantId).orElseGet(() -> {
            TenantPrivacySettings np = new TenantPrivacySettings();
            np.setTenantId(tenantId);
            return np;
        });

        if (dto.getShowPhone() != null) p.setShowPhone(dto.getShowPhone());
        if (dto.getShowEmail() != null) p.setShowEmail(dto.getShowEmail());
        if (dto.getShowProfile() != null) p.setShowProfile(dto.getShowProfile());
        p.setUpdatedAt(Instant.now());
        TenantPrivacySettings saved = privacyRepo.save(p);

        PrivacySettingsDto out = new PrivacySettingsDto();
        out.setShowPhone(saved.getShowPhone());
        out.setShowEmail(saved.getShowEmail());
        out.setShowProfile(saved.getShowProfile());
        return out;
    }

    // -------------- PASSWORD --------------
    @Override
    @Transactional
    public void changePassword(Long tenantId, ChangePasswordRequest req) {
        // delegate to password service (use the method name that exists in your PasswordService)
        passwordService.changeTenantPassword(tenantId, req);
    }
}
