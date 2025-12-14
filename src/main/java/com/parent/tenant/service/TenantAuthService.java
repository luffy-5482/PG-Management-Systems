package com.parent.tenant.service;

import com.parent.config.JwtService;
import com.parent.config.exception.ResourceNotFoundException;
import com.parent.tenant.dto.TenantAuthRequest;
import com.parent.tenant.dto.TenantAuthResponse;
import com.parent.tenant.model.TenantEntity;
import com.parent.tenant.repository.TenantRepository;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class TenantAuthService {

    private final TenantRepository repo;
    private final PasswordEncoder encoder;
    private final JwtService jwt;

    public TenantAuthService(TenantRepository repo, PasswordEncoder encoder, JwtService jwt) {
        this.repo = repo;
        this.encoder = encoder;
        this.jwt = jwt;
    }

    public TenantAuthResponse login(TenantAuthRequest req) {

        TenantEntity t = repo.findByEmail(req.email)
                .orElseThrow(() -> new ResourceNotFoundException("Tenant not found"));

        if (!encoder.matches(req.password, t.getPassword())) {
            throw new RuntimeException("Invalid password");
        }

        Map<String, Object> claims = new HashMap<>();
        claims.put("role", "TENANT");
        claims.put("tenantId", t.getId());
        claims.put("pgId", t.getPgId());
        claims.put("roomId", t.getRoom().getId());

        String token = jwt.generateToken(t.getEmail(), claims);

        TenantAuthResponse res = new TenantAuthResponse();
        res.token = token;
        res.tenantId = t.getId();
        res.name = t.getName();
        res.roomId = t.getRoom().getId();
        res.pgId = t.getPgId();

        return res;
    }
}
