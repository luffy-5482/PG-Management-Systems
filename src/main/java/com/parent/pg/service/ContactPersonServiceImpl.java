package com.parent.pg.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.parent.config.SecurityUtils;
import com.parent.pg.dto.ContactPersonRequest;
import com.parent.pg.dto.ContactPersonResponse;
import com.parent.pg.model.ContactPerson;
import com.parent.pg.model.PgEntity;
import com.parent.pg.repository.ContactPersonRepository;
import com.parent.pg.repository.PgRepository;

@Service
public class ContactPersonServiceImpl implements ContactPersonService {

    @Autowired
    private ContactPersonRepository contactRepo;

    @Autowired
    private PgRepository pgRepo;

    // -------------------------
    // Convert Entity → Response
    // -------------------------
    private ContactPersonResponse toResponse(ContactPerson c) {
        return new ContactPersonResponse(
                c.getId(),
                c.getName(),
                c.getEmail(),
                c.getPhoneNumber(),
                c.getRole(),
                c.getIsPrimary(),
                c.getPg() != null ? c.getPg().getId() : null
        );
    }

    // -------------------------
    // Logged-in owner helper
    // -------------------------
    private Long getOwnerId() {
        Long id = SecurityUtils.getLoggedInOwnerId();
        if (id == null)
            throw new RuntimeException("Unauthorized: Owner not found in token");
        return id;
    }

    // -------------------------
    // CREATE
    // -------------------------
    @Override
    public ContactPersonResponse createContact(ContactPersonRequest req) {

        Long ownerId = getOwnerId();

        PgEntity pg = pgRepo.findByIdAndOwnerId(req.getPgId(), ownerId)
                .orElseThrow(() -> new RuntimeException("Unauthorized: PG does not belong to you"));

        ContactPerson c = new ContactPerson();
        c.setName(req.getName());
        c.setEmail(req.getEmail());
        c.setPhoneNumber(req.getPhoneNumber());
        c.setRole(req.getRole());
        c.setIsPrimary(req.getIsPrimary());
        c.setPg(pg);

        return toResponse(contactRepo.save(c));
    }

    // -------------------------
    // UPDATE
    // -------------------------
    @Override
    public ContactPersonResponse updateContact(Long id, ContactPersonRequest req) {

        Long ownerId = getOwnerId();

        ContactPerson existing = contactRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Contact not found"));

        // SECURITY: ensure this contact belongs to logged-in owner
        if (!existing.getPg().getOwner().getId().equals(ownerId)) {
            throw new RuntimeException("Unauthorized: This contact does not belong to your PG");
        }

        existing.setName(req.getName());
        existing.setEmail(req.getEmail());
        existing.setPhoneNumber(req.getPhoneNumber());
        existing.setRole(req.getRole());
        existing.setIsPrimary(req.getIsPrimary());

        // if user wants to move contact to another PG
        if (req.getPgId() != null) {
            PgEntity pg = pgRepo.findByIdAndOwnerId(req.getPgId(), ownerId)
                    .orElseThrow(() -> new RuntimeException("Unauthorized: PG does not belong to you"));
            existing.setPg(pg);
        }

        return toResponse(contactRepo.save(existing));
    }

    // -------------------------
    // DELETE
    // -------------------------
    @Override
    public void deleteContact(Long id) {

        Long ownerId = getOwnerId();

        ContactPerson contact = contactRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Contact not found"));

        if (!contact.getPg().getOwner().getId().equals(ownerId)) {
            throw new RuntimeException("Unauthorized: Cannot delete contact for another owner's PG");
        }

        contactRepo.delete(contact);
    }

    // -------------------------
    // GET CONTACTS BY PG
    // -------------------------
    @Override
    public List<ContactPersonResponse> getContactsByPg(Long pgId) {

        Long ownerId = getOwnerId();

        pgRepo.findByIdAndOwnerId(pgId, ownerId)
                .orElseThrow(() -> new RuntimeException("Unauthorized: PG does not belong to you"));

        return contactRepo.findByPg_Id(pgId)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }
}
