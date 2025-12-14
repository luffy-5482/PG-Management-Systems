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
    // VALIDATION
    // --------------------------------------------------------
    public boolean isTokenValid(String token) {
        try {
            Claims claims = extractAllClaims(token);
            return claims.getExpiration() != null &&
                   claims.getExpiration().after(new Date());
        } catch (Exception e) {
            return false;
        }
    }

    // --------------------------------------------------------
    // BASIC EXTRACTIONS
    // --------------------------------------------------------
    public String extractUsername(String token) {
        return extractAllClaims(token).getSubject();
    }

    public Long extractOwnerId(String token) {
        return getLong(extractAllClaims(token).get("ownerId"));
    }

    public Long extractAdminId(String token) {
        return getLong(extractAllClaims(token).get("adminId"));
    }

    public Long extractManagerId(String token) {
        return getLong(extractAllClaims(token).get("managerId"));
    }

    public Long extractTenantId(String token) {
        return getLong(extractAllClaims(token).get("tenantId"));
    }

    public Long extractPgId(String token) {
        return getLong(extractAllClaims(token).get("pgId"));
    }

    public String extractRole(String token) {
        Object r = extractAllClaims(token).get("role");
        return r == null ? null : String.valueOf(r);
    }

    public Set<Long> extractAllowedPgIdsFromToken(String token) {
        Object obj = extractAllClaims(token).get("allowedPgIds");
        if (obj == null) return Set.of();

        try {
            Set<Long> set = new HashSet<>();
            for (Object o : (Collection<?>) obj) {
                set.add(Long.valueOf(String.valueOf(o)));
            }
            return set;
        } catch (Exception e) {
            return Set.of();
        }
    }

    public Set<String> extractPermissions(String token) {
        Object obj = extractAllClaims(token).get("permissions");
        if (obj == null) return Set.of();

        try {
            Set<String> set = new HashSet<>();
            for (Object o : (Collection<?>) obj) {
                set.add(String.valueOf(o));
            }
            return set;
        } catch (Exception e) {
            return Set.of();
        }
    }

    // --------------------------------------------------------
    // REQUEST HELPERS (restore your old methods!)
    // --------------------------------------------------------
    private String extractTokenFromRequest(HttpServletRequest request) {
        String h = request.getHeader("Authorization");
        if (h == null || !h.startsWith("Bearer ")) return null;
        return h.substring(7);
    }

    public Long extractOwnerIdFromRequest(HttpServletRequest request) {
        String token = extractTokenFromRequest(request);
        if (token == null || !isTokenValid(token)) return null;
        return extractOwnerId(token);
    }

    public Long extractAdminIdFromRequest(HttpServletRequest request) {
        String token = extractTokenFromRequest(request);
        if (token == null || !isTokenValid(token)) return null;
        return extractAdminId(token);
    }

    public Long extractManagerIdFromRequest(HttpServletRequest request) {
        String token = extractTokenFromRequest(request);
        if (token == null || !isTokenValid(token)) return null;
        return extractManagerId(token);
    }

    public Long extractTenantIdFromRequest(HttpServletRequest request) {
        String token = extractTokenFromRequest(request);
        if (token == null || !isTokenValid(token)) return null;
        return extractTenantId(token);
    }

    public String extractRoleFromRequest(HttpServletRequest request) {
        String token = extractTokenFromRequest(request);
        if (token == null || !isTokenValid(token)) return null;
        return extractRole(token);
    }

    public Set<Long> extractAllowedPgIdsFromRequest(HttpServletRequest request) {
        String token = extractTokenFromRequest(request);
        if (token == null || !isTokenValid(token)) return Set.of();
        return extractAllowedPgIdsFromToken(token);
    }

    // --------------------------------------------------------
    // INTERNAL JWT DECODING
    // --------------------------------------------------------
    private Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Key getSigningKey() {
        byte[] bytes = Decoders.BASE64.decode(jwtSecret);
        return Keys.hmacShaKeyFor(bytes);
    }

    private Long getLong(Object val) {
        return val == null ? null : Long.valueOf(String.valueOf(val));
    }
}
