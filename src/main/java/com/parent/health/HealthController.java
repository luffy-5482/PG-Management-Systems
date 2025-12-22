package com.parent.health;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
public class HealthController {
	private static final Logger log = LoggerFactory.getLogger(HealthController.class);

    @GetMapping("/health")
    public ResponseEntity<String> health() {
        log.info("Health check called - returning OK");  // Add this to verify it reaches here
        return ResponseEntity.ok("OK");
    }
    
    @GetMapping("/")
    public ResponseEntity<String> root() {
        return ResponseEntity.ok("OK");
    }
    
    @GetMapping("/api/public/health")
    public ResponseEntity<String> apiHealth() {
        return ResponseEntity.ok("OK");
    }
}