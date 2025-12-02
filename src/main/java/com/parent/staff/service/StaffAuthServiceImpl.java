package com.parent.staff.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.parent.config.JwtService;
import com.parent.staff.dto.StaffLoginRequest;
import com.parent.staff.dto.StaffLoginResponse;
import com.parent.staff.model.StaffEntity;
import com.parent.staff.repository.StaffRepository;

@Service
public class StaffAuthServiceImpl implements StaffAuthService {

    private final StaffRepository staffRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    public StaffAuthServiceImpl(
            StaffRepository staffRepository,
            JwtService jwtService,
            PasswordEncoder passwordEncoder
    ) {
        this.staffRepository = staffRepository;
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
    }
 
    @Override
    public StaffLoginResponse login(StaffLoginRequest req) {

        StaffEntity staff = staffRepository.findByEmail(req.getEmail())
                .orElseThrow(() -> new RuntimeException("Invalid email or password"));

        if (!passwordEncoder.matches(req.getPassword(), staff.getPassword())) {
            throw new RuntimeException("Invalid email or password");
        }

        Map<String, Object> claims = new HashMap<>();
        claims.put("role", "ROLE_STAFF");
        claims.put("staffId", staff.getId());
        claims.put("pgId", staff.getPg().getId());

        String token = jwtService.generateToken(staff.getEmail(), claims);
        String refreshToken = jwtService.generateRefreshToken(staff.getEmail(), claims);

        return new StaffLoginResponse(
                token, 
                refreshToken,
                staff.getId(),
                staff.getPg().getId(),
                "ROLE_STAFF"
        );
    }
}
