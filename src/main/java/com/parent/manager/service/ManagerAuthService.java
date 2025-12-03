package com.parent.manager.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.parent.auth.AuthenticationResponse;
import com.parent.manager.model.Manager;
import com.parent.manager.repository.ManagerRepository;
import com.parent.config.JwtService;

@Service
public class ManagerAuthService {

	private final ManagerRepository repo;
	private final PasswordEncoder passwordEncoder;
	private final JwtService jwtService;

	public ManagerAuthService(ManagerRepository repo, PasswordEncoder passwordEncoder, JwtService jwtService) {
		this.repo = repo;
		this.passwordEncoder = passwordEncoder;
		this.jwtService = jwtService;
	}

	public AuthenticationResponse login(String email, String password) {
	    Manager m = repo.findByEmail(email)
	            .orElseThrow(() -> new BadCredentialsException("Invalid credentials"));

	    if (!passwordEncoder.matches(password, m.getPassword()))
	        throw new BadCredentialsException("Invalid credentials");

	    Map<String, Object> claims = new HashMap<>();

	    // Role
	    claims.put("role", "MANAGER");

	    // Manager ID
	    claims.put("managerId", m.getId());

	    // Assigned PGs
	    claims.put("allowedPgIds", m.getAllowedPgIds());

	    // 👉 OPTIONAL but very helpful (primary PG)
	    if (m.getAllowedPgIds() != null && !m.getAllowedPgIds().isEmpty()) {
	        claims.put("pgId", m.getAllowedPgIds().iterator().next());
	    }

	    String token = jwtService.generateToken(m.getEmail(), claims);
	    String refresh = jwtService.generateRefreshToken(m.getEmail(), claims);

	    return new AuthenticationResponse(token, refresh);
	}

}
