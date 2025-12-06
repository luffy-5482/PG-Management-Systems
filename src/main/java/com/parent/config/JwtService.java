package com.parent.config;

import java.security.Key;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Map;
import java.util.Set;
import java.util.HashSet;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;

@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String jwtSecret;

    private static final String ISSUER   = "pgman-auth";
    private static final String AUDIENCE = "pgman-users";

    // --------------------------------------------------------
    // TOKEN GENERATION
    // --------------------------------------------------------
    public String generateToken(String subject, Map<String, Object> extraClaims) {
        return Jwts.builder()  
                .setClaims(extraClaims)
                .setSubject(subject)
                .setIssuer(ISSUER)
                .setAudience(AUDIENCE)
                .setIssuedAt(Date.from(Instant.now()))
                .setExpiration(Date.from(Instant.now().plus(1, ChronoUnit.DAYS)))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public String generateRefreshToken(String subject, Map<String, Object> extraClaims) {
        return Jwts.builder()
                .setClaims(extraClaims)
                .setSubject(subject)
                .setIssuer(ISSUER)
                .setAudience(AUDIENCE)
                .setIssuedAt(Date.from(Instant.now()))
                .setExpiration(Date.from(Instant.now().plus(7, ChronoUnit.DAYS)))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    // --------------------------------------------------------
    // VALIDATION + EXTRACTION HELPERS
    // --------------------------------------------------------
    public boolean isTokenValid(String token) {
        try {
            Claims claims = extractAllClaims(token);
            Date expiration = claims.getExpiration();
            return expiration != null && expiration.after(new Date());
        } catch (Exception ex) {
            return false;
        }
    }

    public String extractUsername(String token) {
        return extractAllClaims(token).getSubject();
    }

    public Long extractOwnerId(String token) {
        Object owner = extractAllClaims(token).get("ownerId");
        return owner == null ? null : Long.valueOf(String.valueOf(owner));
    }

    public Long extractAdminId(String token) {
        Object admin = extractAllClaims(token).get("adminId");
        return admin == null ? null : Long.valueOf(String.valueOf(admin));
    }

    public Long extractManagerId(String token) {
        Object m = extractAllClaims(token).get("managerId");
        return m == null ? null : Long.valueOf(String.valueOf(m));
    }

    // --------------------------------------------------------
    // MANAGER: allowed PG IDs
    // --------------------------------------------------------
    @SuppressWarnings("unchecked")
    public Set<Long> extractAllowedPgIdsFromToken(String token) {
        Object obj = extractAllClaims(token).get("allowedPgIds");
        if (obj == null) return Set.of();

        try {
            Collection<?> list = (Collection<?>) obj;
            Set<Long> ids = new HashSet<>();
            for (Object o : list) ids.add(Long.valueOf(String.valueOf(o)));
            return ids;
        } catch (Exception e) {
            return Set.of();
        }
    }

    // --------------------------------------------------------
    // MANAGER: extract single PG ID (optional)
    // --------------------------------------------------------
    public Long extractPgId(String token) {
        Object pg = extractAllClaims(token).get("pgId");
        return pg == null ? null : Long.valueOf(String.valueOf(pg));
    }

    public String extractRole(String token) {
        Object role = extractAllClaims(token).get("role");
        return role == null ? null : String.valueOf(role);
    }

    // --------------------------------------------------------
    // INTERNAL JWT DECODING
    // --------------------------------------------------------
    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Key getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(jwtSecret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    // --------------------------------------------------------
    // REQUEST HELPERS
    // --------------------------------------------------------
    private String extractTokenFromRequest(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        if (header == null || !header.startsWith("Bearer ")) return null;
        return header.substring(7);
    }

    public Long extractOwnerIdFromRequest(HttpServletRequest request) {
        String token = extractTokenFromRequest(request);
        if (token == null) return null;
        if (!isTokenValid(token)) return null;
        return extractOwnerId(token);
    }

    public Long extractAdminIdFromRequest(HttpServletRequest request) {
        String token = extractTokenFromRequest(request);
        if (token == null) return null;
        if (!isTokenValid(token)) return null;
        return extractAdminId(token);
    }

    public Long extractManagerIdFromRequest(HttpServletRequest request) {
        String token = extractTokenFromRequest(request);
        if (token == null) return null;
        if (!isTokenValid(token)) return null;
        return extractManagerId(token);
    }

    public String extractRoleFromRequest(HttpServletRequest request) {
        String token = extractTokenFromRequest(request);
        if (token == null) return null;
        if (!isTokenValid(token)) return null;
        return extractRole(token);
    }
    public Set<Long> extractAllowedPgIdsFromRequest(HttpServletRequest request) {
        String token = extractTokenFromRequest(request);
        if (token == null || !isTokenValid(token)) return Set.of();
        return extractAllowedPgIdsFromToken(token);
    }

}
