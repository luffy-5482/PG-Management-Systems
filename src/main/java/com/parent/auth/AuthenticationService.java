package com.parent.auth;

import java.util.HashMap;
import java.util.Map;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.parent.config.JwtService;
import com.parent.owner.dto.OwnerRequest;
import com.parent.owner.model.Owner;
import com.parent.owner.repository.OwnerRepository;

import jakarta.servlet.http.HttpServletRequest;

@Service
public class AuthenticationService {

    private final OwnerRepository ownerRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthenticationService(OwnerRepository ownerRepository,
                                 PasswordEncoder passwordEncoder,
                                 JwtService jwtService) {
        this.ownerRepository = ownerRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    // ---------------------------------------------------
    // REGISTER (manual owner ID)
    // ---------------------------------------------------
    public AuthenticationResponse register(OwnerRequest request) {

        // 🔥 Check ID provided
        if (request.getId() == null) {
            throw new RuntimeException("Owner ID must be provided");
        }

        // 🔥 Check ID duplicate
        if (ownerRepository.findById(request.getId()).isPresent()) {
            throw new RuntimeException("Owner ID already exists");
        }

        // 🔥 Check email duplicate
        if (ownerRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("Email already registered");
        }

        Owner owner = new Owner();
        owner.setId(request.getId());  // 🔥 manual ID
        owner.setFullName(request.getFullName());
        owner.setEmail(request.getEmail());
        owner.setPhoneNumber(request.getPhoneNumber());
        owner.setGender(request.getGender());
        owner.setPassword(passwordEncoder.encode(request.getPassword()));

        Owner saved = ownerRepository.save(owner);

        Map<String, Object> claims = new HashMap<>();
        claims.put("ownerId", saved.getId());

        String token = jwtService.generateToken(saved.getEmail(), claims);
        String refresh = jwtService.generateRefreshToken(saved.getEmail(), claims);

        return new AuthenticationResponse(token, refresh);
    }

    // ---------------------------------------------------
    // LOGIN
    // ---------------------------------------------------
    public AuthenticationResponse login(AuthenticationRequest request) {
        return authenticate(request);
    }

    // ---------------------------------------------------
    // AUTHENTICATE
    // ---------------------------------------------------
    public AuthenticationResponse authenticate(AuthenticationRequest request) {

        Owner owner = ownerRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new BadCredentialsException("Invalid credentials"));

        if (!passwordEncoder.matches(request.getPassword(), owner.getPassword())) {
            throw new BadCredentialsException("Invalid credentials");
        }

        Map<String, Object> claims = new HashMap<>();
        claims.put("ownerId", owner.getId());

        String token = jwtService.generateToken(owner.getEmail(), claims);
        String refresh = jwtService.generateRefreshToken(owner.getEmail(), claims);

        return new AuthenticationResponse(token, refresh);
    }

    // ---------------------------------------------------
    // REFRESH TOKEN
    // ---------------------------------------------------
    public AuthenticationResponse refreshToken(HttpServletRequest request) {

        final String header = request.getHeader("Authorization");
        if (header == null || !header.startsWith("Bearer ")) {
            throw new RuntimeException("Missing Refresh Token");
        }

        String refreshToken = header.substring(7);

        if (!jwtService.isTokenValid(refreshToken)) {
            throw new RuntimeException("Invalid Refresh Token");
        }

        String email = jwtService.extractUsername(refreshToken);
        Long ownerId = jwtService.extractOwnerId(refreshToken);

        Map<String, Object> claims = new HashMap<>();
        claims.put("ownerId", ownerId);

        String newToken = jwtService.generateToken(email, claims);
        String newRefresh = jwtService.generateRefreshToken(email, claims);

        return new AuthenticationResponse(newToken, newRefresh);
    }
}
