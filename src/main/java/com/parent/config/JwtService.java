package com.parent.config;

import java.security.Key;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String jwtSecret;

    // ✅ Universal issuer & audience for BOTH localhost + Render
    private static final String ISSUER   = "pgman-auth";
    private static final String AUDIENCE = "pgman-users";

    // ----------------------------
    // ACCESS TOKEN
    // ----------------------------
    public String generateToken(String subject, Map<String, Object> extraClaims) {
        return Jwts.builder()
                .setClaims(extraClaims)
                .setSubject(subject)
                .setIssuer(ISSUER)            // NEW
                .setAudience(AUDIENCE)        // NEW
                .setIssuedAt(Date.from(Instant.now()))
                .setExpiration(Date.from(Instant.now().plus(1, ChronoUnit.DAYS)))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }
 
    // ----------------------------
    // REFRESH TOKEN
    // ----------------------------
    public String generateRefreshToken(String subject, Map<String, Object> extraClaims) {
        return Jwts.builder()
                .setClaims(extraClaims)
                .setSubject(subject)
                .setIssuer(ISSUER)            // NEW
                .setAudience(AUDIENCE)        // NEW
                .setIssuedAt(Date.from(Instant.now()))
                .setExpiration(Date.from(Instant.now().plus(7, ChronoUnit.DAYS)))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    // ----------------------------
    // VALIDATION
    // ----------------------------
    public boolean isTokenValid(String token) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            // We mainly care about signature + expiry.
            // Issuer/audience are set for NEW tokens, but we don't hard-fail old tokens.
            Date expiration = claims.getExpiration();
            return expiration == null || expiration.after(new Date());

        } catch (Exception ex) {
            return false;
        }
    }

    // ----------------------------
    // CLAIM EXTRACTORS
    // ----------------------------

    public String extractUsername(String token) {
        return extractAllClaims(token).getSubject();
    }

    public Long extractOwnerId(String token) {
        Object owner = extractAllClaims(token).get("ownerId");
        return owner == null ? null : Long.valueOf(String.valueOf(owner));
    }

    public Long extractStaffId(String token) {
        Object staff = extractAllClaims(token).get("staffId");
        return staff == null ? null : Long.valueOf(String.valueOf(staff));
    }

    public Long extractStaffPgId(String token) {
        Object pg = extractAllClaims(token).get("pgId");
        return pg == null ? null : Long.valueOf(String.valueOf(pg));
    }

    public String extractRole(String token) {
        Object role = extractAllClaims(token).get("role");
        return role == null ? null : String.valueOf(role);
    }

    // ----------------------------
    // INTERNAL
    // ----------------------------
    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Key getSigningKey() {
        // NOTE: jwtSecret must be BASE64 string in both localhost + Render
        byte[] keyBytes = Decoders.BASE64.decode(jwtSecret);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
