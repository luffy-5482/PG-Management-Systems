package com.parent.tenant.service;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.Set;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.parent.config.SecurityUtils;
import com.parent.config.exception.ConflictException;
import com.parent.config.exception.ResourceNotFoundException;
import com.parent.pg.model.RoomEntity;
import com.parent.pg.repository.RoomRepository;
import com.parent.tenant.dto.TenantRequest;
import com.parent.tenant.dto.TenantResponse;
import com.parent.tenant.model.TenantEntity;
import com.parent.tenant.repository.TenantRepository;

import jakarta.transaction.Transactional;

@Service
public class TenantServiceImpl implements TenantService {

    @Autowired
    private TenantRepository tenantRepo;

    @Autowired
    private RoomRepository roomRepo;

    @Autowired(required = false)
    private PasswordEncoder passwordEncoder;

    // -------------------------
    // Convert entity → response
    // -------------------------
    private final Function<TenantEntity, TenantResponse> toResponse = t -> {
        TenantResponse r = new TenantResponse();

        r.setId(t.getId());
        r.setName(t.getName());
        r.setEmail(t.getEmail());

        if (t.getRoom() != null) {
            r.setRoomId(t.getRoom().getId());
            r.setRoom(t.getRoom().getRoomNumber());   // room number string
        }

        r.setContact(t.getPhone()); // phone -> contact in response

        // billing fields
        r.setRent(t.getRent());
        r.setDue(t.getDue());
        r.setDueDate(t.getDueDate());
        r.setStatus(t.getRentStatus());
        r.setAvatar(t.getAvatar());

        // joinedAt -> joinDate
        r.setJoinDate(t.getJoinedAt());

        // Only populated on create
        return r;
    };

    // Role access helpers
    private Long getOwnerId() { return SecurityUtils.getLoggedInOwnerId(); }
    private Long getManagerId() { return SecurityUtils.getLoggedInManagerId(); }
    private Long getAdminId() { 
        Object a = SecurityUtils.getRequestAttribute("adminId");
        return a == null ? null : Long.valueOf(String.valueOf(a));
    }

    @SuppressWarnings("unchecked")
    private Set<String> getAdminPermissions() {
        Object p = SecurityUtils.getRequestAttribute("permissions");
        return p == null ? Set.of() : (Set<String>) p;
    }

    @SuppressWarnings("unchecked")
    private Set<Long> getAllowedPgIds() {
        Object p = SecurityUtils.getRequestAttribute("allowedPgIds");
        return p == null ? Set.of() : (Set<Long>) p;
    }

    // Generate password
    private String generatePassword() {
        SecureRandom r = new SecureRandom();
        byte[] buffer = new byte[10];
        r.nextBytes(buffer);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(buffer);
    }

    // -----------------------------------------------
    // ACCESS CHECK
    // -----------------------------------------------
    private void assertCanManageTenant(Long pgId) {

        Long ownerId = getOwnerId();
        Long managerId = getManagerId();
        Long adminId = getAdminId();
        Set<String> perms = getAdminPermissions();

        // OWNER = always allowed
        if (ownerId != null) return;

        // MANAGER = must have pg in allowed list
        if (managerId != null) {
            if (!getAllowedPgIds().contains(pgId)) {
                throw new ConflictException("Manager cannot manage tenants for this PG");
            }
            return;
        }

        // ADMIN = needs permissions or FULL_ACCESS
        if (adminId != null) {

            if (perms.contains("FULL_ACCESS")) return;

            if (!perms.contains("MANAGE_TENANTS_VIEW") &&
                !perms.contains("MANAGE_TENANTS_CREATE") &&
                !perms.contains("MANAGE_TENANTS_UPDATE") &&
                !perms.contains("MANAGE_TENANTS_DELETE")) 
            {
                throw new ConflictException("Admin missing tenant permissions");
            }

            if (perms.contains("ACCESS_LIMITED_TO_PG") &&
                !getAllowedPgIds().contains(pgId)) 
            {
                throw new ConflictException("Admin does not have access to this PG");
            }

            return;
        }

        throw new ConflictException("Unauthorized");
    }

    // -----------------------------------------------
    // CREATE TENANT
    // -----------------------------------------------
    @Override
    @Transactional
    public TenantResponse createTenant(TenantRequest req, String actor) {

        RoomEntity room = roomRepo.findById(req.getRoomId())
                .orElseThrow(() -> new ResourceNotFoundException("Room not found"));

        Long pgId = room.getPg().getId();

        // Permission check
        assertCanManageTenant(pgId);

        // CREATE-specific permission for admin
        if (getAdminId() != null &&
            !getAdminPermissions().contains("FULL_ACCESS") &&
            !getAdminPermissions().contains("MANAGE_TENANTS_CREATE")) 
        {
            throw new ConflictException("Admin lacks MANAGE_TENANTS_CREATE");
        }

        // Unique email check
        tenantRepo.findByEmail(req.getEmail())
                .ifPresent(t -> { throw new ConflictException("Email already in use"); });

        // Room availability
        if (!room.getAvailable()) throw new ConflictException("Room not available");

        long occupants = tenantRepo.countByRoom_IdAndActiveTrue(room.getId());
        if (occupants >= room.getCapacity()) 
            throw new ConflictException("Room capacity full");

        // Password handling
        String plain = req.getPassword();
        if (!StringUtils.hasText(plain)) plain = generatePassword();

        String encoded = (passwordEncoder != null)
                ? passwordEncoder.encode(plain)
                : plain;

        // Create entity
        TenantEntity t = new TenantEntity();
        t.setName(req.getName());
        t.setEmail(req.getEmail());
        t.setPhone(req.getPhone());
        t.setPassword(encoded);
        t.setRoom(room);
        t.setCreatedBy(actor);
        t.setActive(true);

        // Denormalized fields
        t.setOwnerId(room.getPg().getOwner().getId());
        t.setPgId(pgId);
        t.setFloorId(room.getFloor().getId());

        // NEW billing fields
        if (req.getRent() != null) t.setRent(req.getRent());
        if (req.getDue() != null) t.setDue(req.getDue());
        if (req.getDueDate() != null) t.setDueDate(req.getDueDate());
        if (req.getStatus() != null) t.setRentStatus(req.getStatus());
        if (req.getAvatar() != null) t.setAvatar(req.getAvatar());

        TenantEntity saved = tenantRepo.save(t);

        TenantResponse res = toResponse.apply(saved);
        // return plain password only immediately after creation
        res.setPassword(plain);
        return res;
    }

    // -----------------------------------------------
    // GET TENANT
    // -----------------------------------------------
    @Override
    public TenantResponse getTenant(Long id) {

        TenantEntity t = tenantRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tenant not found"));

        assertCanManageTenant(t.getPgId());

        return toResponse.apply(t);
    }

    // -----------------------------------------------
    // LIST TENANTS
    // -----------------------------------------------
    @Override
    public Page<TenantResponse> listTenants(Pageable pageable, Long roomId, String name, Boolean active) {

        Page<TenantEntity> page;

        if (roomId != null) {
            RoomEntity r = roomRepo.findById(roomId)
                    .orElseThrow(() -> new ResourceNotFoundException("Room not found"));
            assertCanManageTenant(r.getPg().getId());
            page = tenantRepo.findByRoom_Id(roomId, pageable);
        }
        else if (StringUtils.hasText(name)) {
            page = tenantRepo.findByNameContainingIgnoreCase(name, pageable);
        }
        else if (active != null && active) {
            page = tenantRepo.findByActiveTrue(pageable);
        }
        else page = tenantRepo.findAll(pageable);

        return page.map(toResponse);
    }

    // -----------------------------------------------
    // UPDATE TENANT
    // -----------------------------------------------
    @Override
    @Transactional
    public TenantResponse updateTenant(Long id, TenantRequest req, String actor) {

        TenantEntity t = tenantRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tenant not found"));

        assertCanManageTenant(t.getPgId());

        // Admin permission
        if (getAdminId() != null &&
            !getAdminPermissions().contains("FULL_ACCESS") &&
            !getAdminPermissions().contains("MANAGE_TENANTS_UPDATE")) 
        {
            throw new ConflictException("Admin lacks MANAGE_TENANTS_UPDATE");
        }

        // Updating fields
        if (req.getName() != null) t.setName(req.getName());

        if (req.getEmail() != null && !req.getEmail().equals(t.getEmail())) {
            tenantRepo.findByEmail(req.getEmail())
                    .ifPresent(x -> { throw new ConflictException("Email already used"); });
            t.setEmail(req.getEmail());
        }

        if (req.getPhone() != null) t.setPhone(req.getPhone());

        // ROOM CHANGE
        if (req.getRoomId() != null && !req.getRoomId().equals(t.getRoom().getId())) {

            RoomEntity newRoom = roomRepo.findById(req.getRoomId())
                    .orElseThrow(() -> new ResourceNotFoundException("Room not found"));

            assertCanManageTenant(newRoom.getPg().getId());

            if (getAdminId() != null &&
                !getAdminPermissions().contains("FULL_ACCESS") &&
                !getAdminPermissions().contains("ASSIGN_ROOM")) 
            {
                throw new ConflictException("Admin lacks ASSIGN_ROOM permission");
            }

            long occ = tenantRepo.countByRoom_IdAndActiveTrue(newRoom.getId());
            if (occ >= newRoom.getCapacity()) throw new ConflictException("Room capacity full");

            t.setRoom(newRoom);

            // FIXED ownerId assignment
            t.setOwnerId(newRoom.getPg().getOwner().getId());
            t.setPgId(newRoom.getPg().getId());
            t.setFloorId(newRoom.getFloor().getId());
        }

        // Password update
        if (StringUtils.hasText(req.getPassword())) {
            t.setPassword(passwordEncoder.encode(req.getPassword()));
        }

        // NEW billing updates
        if (req.getRent() != null) t.setRent(req.getRent());
        if (req.getDue() != null) t.setDue(req.getDue());
        if (req.getDueDate() != null) t.setDueDate(req.getDueDate());
        if (req.getStatus() != null) t.setRentStatus(req.getStatus());
        if (req.getAvatar() != null) t.setAvatar(req.getAvatar());

        t.setUpdatedBy(actor);
        TenantEntity saved = tenantRepo.save(t);

        return toResponse.apply(saved);
    }

    // -----------------------------------------------
    // SOFT DELETE TENANT
    // -----------------------------------------------
    @Override
    @Transactional
    public void softDeleteTenant(Long id, String actor) {

        TenantEntity t = tenantRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tenant not found"));

        assertCanManageTenant(t.getPgId());

        if (getAdminId() != null &&
            !getAdminPermissions().contains("FULL_ACCESS") &&
            !getAdminPermissions().contains("MANAGE_TENANTS_DELETE")) 
        {
            throw new ConflictException("Admin lacks MANAGE_TENANTS_DELETE");
        }

        t.setActive(false);
        t.setUpdatedBy(actor);

        tenantRepo.save(t);
    }

    // -----------------------------------------------
    // HARD DELETE (OWNER ONLY)
    // -----------------------------------------------
    @Override
    @Transactional
    public void hardDeleteTenant(Long id) {

        Long ownerId = getOwnerId();
        if (ownerId == null)
            throw new ConflictException("Only owner can permanently delete tenants");

        tenantRepo.deleteById(id);
    }
}
