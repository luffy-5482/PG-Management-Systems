package com.parent.tenant.service.impl;

import com.parent.tenant.dto.*;
import com.parent.tenant.service.ProfileService;
import com.parent.payment.repository.TenantRepository;
import com.parent.payment.model.Tenant;
import com.parent.room.repository.RoomRepository;
import com.parent.room.model.Room;
import com.parent.contact.repository.EmergencyContactRepository;
import com.parent.contact.model.EmergencyContact;
import com.parent.doc.repository.DocumentRepository;
import com.parent.doc.model.Document;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class ProfileServiceImpl implements ProfileService {

    private final TenantRepository tenantRepository;
    private final RoomRepository roomRepository;
    private final EmergencyContactRepository contactRepository;
    private final DocumentRepository documentRepository;

    public ProfileServiceImpl(TenantRepository tenantRepository,
                              RoomRepository roomRepository,
                              EmergencyContactRepository contactRepository,
                              DocumentRepository documentRepository) {
        this.tenantRepository = tenantRepository;
        this.roomRepository = roomRepository;
        this.contactRepository = contactRepository;
        this.documentRepository = documentRepository;
    }

    @Override
    public TenantProfileDto getProfile(Long tenantId) {
        Optional<Tenant> tOpt = tenantRepository.findById(tenantId);
        if (tOpt.isEmpty()) return null;

        Tenant t = tOpt.get();
        TenantProfileDto dto = new TenantProfileDto();
        dto.setTenantId(t.getId());
        dto.setFullName(t.getName());
        dto.setEmail(t.getEmail());
        dto.setPhone(t.getContact());
        // if your Tenant model has DOB/occupation/gender/profilePhoto, set them
        // Example: dto.setDateOfBirth(t.getDateOfBirth());

        // fill room if exists
        roomRepository.findByTenantId(tenantId).ifPresent(room -> {
            dto.setRoomNumber(room.getRoomNumber());
            dto.setFloor(room.getFloor());
            dto.setSharingType(room.getSharingType());
            dto.setMonthlyRent(room.getMonthlyRent());
            // optional: dto.setCheckinDate(...); dto.setLeaseDuration(...);
        });

        List<EmergencyContactDto> contacts = contactRepository.findByTenantId(tenantId)
                .stream().map(c -> {
                    EmergencyContactDto ec = new EmergencyContactDto();
                    ec.setId(c.getId());
                    ec.setName(c.getName());
                    ec.setRelationship(c.getRelationship());
                    ec.setPhone(c.getPhone());
                    ec.setEmail(c.getEmail());
                    return ec;
                }).collect(Collectors.toList());
        dto.setEmergencyContacts(contacts);

        List<DocumentDto> docs = documentRepository.findByTenantId(tenantId)
                .stream().map(d -> {
                    DocumentDto dd = new DocumentDto();
                    dd.setId(d.getId());
                    dd.setType(d.getType());
                    dd.setUrl(d.getUrl());
                    dd.setVerified(d.getVerified());
                    return dd;
                }).collect(Collectors.toList());
        dto.setDocuments(docs);

        return dto;
    }

    @Override
    public TenantProfileDto updateProfile(Long tenantId, TenantProfileDto update) {
        Optional<Tenant> tOpt = tenantRepository.findById(tenantId);
        if (tOpt.isEmpty()) return null;
        Tenant t = tOpt.get();
        if (update.getFullName() != null) t.setName(update.getFullName());
        if (update.getEmail() != null) t.setEmail(update.getEmail());
        if (update.getPhone() != null) t.setContact(update.getPhone());
        // handle other fields as appropriate
        tenantRepository.save(t);
        return getProfile(tenantId);
    }

    @Override
    public String saveProfilePhoto(Long tenantId, MultipartFile file) {
        try {
            if (file == null || file.isEmpty()) return null;
            Path uploads = Path.of(System.getProperty("user.dir"), "uploads", "tenants", String.valueOf(tenantId));
            Files.createDirectories(uploads);
            String filename = System.currentTimeMillis() + "-" + file.getOriginalFilename();
            Path target = uploads.resolve(filename);
            Files.copy(file.getInputStream(), target, StandardCopyOption.REPLACE_EXISTING);
            String url = "/uploads/tenants/" + tenantId + "/" + filename;
            // optionally save a Document entry or set tenant profile photo field if exists
            return url;
        } catch (Exception e) {
            throw new RuntimeException("Failed to store file", e);
        }
    }

    @Override
    public EmergencyContactDto addEmergencyContact(Long tenantId, EmergencyContactDto contact) {
        EmergencyContact c = new EmergencyContact();
        c.setName(contact.getName());
        c.setRelationship(contact.getRelationship());
        c.setPhone(contact.getPhone());
        c.setEmail(contact.getEmail());
        c.setTenantId(tenantId);
        EmergencyContact saved = contactRepository.save(c);
        contact.setId(saved.getId());
        return contact;
    }

    @Override
    public EmergencyContactDto updateEmergencyContact(Long tenantId, Long contactId, EmergencyContactDto contact) {
        Optional<EmergencyContact> cOpt = contactRepository.findById(contactId);
        if (cOpt.isEmpty()) return null;
        EmergencyContact e = cOpt.get();
        if (!tenantId.equals(e.getTenantId())) return null; // guard
        if (contact.getName() != null) e.setName(contact.getName());
        if (contact.getRelationship() != null) e.setRelationship(contact.getRelationship());
        if (contact.getPhone() != null) e.setPhone(contact.getPhone());
        if (contact.getEmail() != null) e.setEmail(contact.getEmail());
        contactRepository.save(e);
        contact.setId(e.getId());
        return contact;
    }

    @Override
    public void deleteEmergencyContact(Long tenantId, Long contactId) {
        Optional<EmergencyContact> cOpt = contactRepository.findById(contactId);
        if (cOpt.isEmpty()) return;
        EmergencyContact e = cOpt.get();
        if (!tenantId.equals(e.getTenantId())) return;
        contactRepository.delete(e);
    }

    @Override
    public void changePassword(Long tenantId, String currentPassword, String newPassword) {
        Optional<Tenant> tOpt = tenantRepository.findById(tenantId);
        if (tOpt.isEmpty()) throw new RuntimeException("tenant not found");
        Tenant t = tOpt.get();
        // YOU MUST IMPLEMENT: password hashing and verification logic appropriate to your auth
        if (!t.getPassword().equals(currentPassword)) {
            throw new RuntimeException("current password mismatch");
        }
        t.setPassword(newPassword);
        tenantRepository.save(t);
    }
}
