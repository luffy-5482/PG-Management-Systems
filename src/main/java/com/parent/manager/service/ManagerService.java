package com.parent.manager.service;

import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.parent.manager.dto.CreateManagerRequest;
import com.parent.manager.dto.ManagerResponse;
import com.parent.manager.dto.UpdateManagerRequest;
import com.parent.manager.model.Manager;
import com.parent.manager.repository.ManagerRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class ManagerService {

    private final ManagerRepository repo;
    private final PasswordEncoder passwordEncoder;

    public ManagerService(ManagerRepository repo, PasswordEncoder passwordEncoder) {
        this.repo = repo;
        this.passwordEncoder = passwordEncoder;
    }

    // OWNER CREATES MANAGER
    public ManagerResponse createManager(CreateManagerRequest req, Long ownerId) {

        Manager m = new Manager();
        m.setName(req.fullName);
        m.setEmail(req.email);
        m.setPassword(passwordEncoder.encode(req.password));
        m.setPhone(req.phone);

        // 🔥 REQUIRED or Hibernate throws NOT NULL error
        m.setOwnerId(ownerId);

        // Allowed PGs (null-safe)
        if (req.allowedPgIds != null) {
            m.setAllowedPgIds(new HashSet<>(req.allowedPgIds));
        }

        Manager saved = repo.save(m);
        return toResponse(saved);
    }

    // OWNER UPDATES MANAGER
    public ManagerResponse updateManager(Long id, UpdateManagerRequest req, Long ownerId) {

        Manager m = repo.findById(id).orElseThrow(() ->
                new EntityNotFoundException("Manager not found")
        );

        if (req.fullName != null) m.setName(req.fullName);
        if (req.phone != null) m.setPhone(req.phone);
        if (req.allowedPgIds != null) m.setAllowedPgIds(req.allowedPgIds);

        Manager saved = repo.save(m);
        return toResponse(saved);
    }

    // OWNER DELETES MANAGER
    public void deleteManager(Long id, Long ownerId) {
        repo.deleteById(id);
    }

    public ManagerResponse getManager(Long id) {
        return toResponse(repo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Manager not found")));
    }

    public List<ManagerResponse> listManagers() {
        return repo.findAll()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    private ManagerResponse toResponse(Manager m) {
        ManagerResponse r = new ManagerResponse();
        r.id = m.getId();
        r.fullName = m.getName();
        r.email = m.getEmail();
        r.phone = m.getPhone();
        r.allowedPgIds = m.getAllowedPgIds();
        r.createdAt = m.getCreatedAt();
        return r;
    }
}
