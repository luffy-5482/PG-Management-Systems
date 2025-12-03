package com.parent.admin.service;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.parent.admin.model.Admin;
import com.parent.admin.repository.AdminRepository;
import com.parent.config.JwtService;
import com.parent.auth.AuthenticationResponse;

@Service
public class AdminAuthService {

    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AdminAuthService(AdminRepository adminRepository,
                            PasswordEncoder passwordEncoder,
                            JwtService jwtService) {
        this.adminRepository = adminRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public AuthenticationResponse login(String email, String password) {
        Admin admin = adminRepository.findByEmail(email)
                .orElseThrow(() -> new BadCredentialsException("Invalid credentials"));

        if (!passwordEncoder.matches(password, admin.getPassword())) {
            throw new BadCredentialsException("Invalid credentials");
        }

        Map<String, Object> claims = new HashMap<>();
        claims.put("adminId", admin.getId());
        claims.put("role", "ADMIN"); // store without ROLE_ prefix
        Set<Long> allowed = admin.getAllowedPgIds();
        if (allowed != null && !allowed.isEmpty()) {
            claims.put("allowedPgIds", allowed);
        }

        String token = jwtService.generateToken(admin.getEmail(), claims);
        String refresh = jwtService.generateRefreshToken(admin.getEmail(), claims);
        return new AuthenticationResponse(token, refresh);
    }
}
