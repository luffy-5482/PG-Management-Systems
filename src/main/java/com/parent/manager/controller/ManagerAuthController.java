package com.parent.manager.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.parent.manager.dto.ManagerLoginRequest;
import com.parent.manager.service.ManagerAuthService;
import com.parent.auth.AuthenticationResponse;

@RestController
@RequestMapping("/api/manager/auth")
@CrossOrigin(origins = "*")
public class ManagerAuthController {

	private final ManagerAuthService authService;

	public ManagerAuthController(ManagerAuthService authService) {
		this.authService = authService;
	}

	@PostMapping("/login")
	public ResponseEntity<AuthenticationResponse> login(@RequestBody ManagerLoginRequest req) {
		return ResponseEntity.ok(authService.login(req.getEmail(), req.getPassword()));
	}
}
