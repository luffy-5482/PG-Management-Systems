package com.parent.config;

import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    public JwtAuthenticationFilter(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {

        String raw = request.getRequestURI();
        System.out.println("RAW URI = [" + raw + "]");

        // Decode %20, %0A, unicode, everything
        String path = URLDecoder.decode(raw, StandardCharsets.UTF_8);

        // Remove hidden/invisible/trailing characters
        path = path
                .replaceAll("[\\u200B-\\u200D\\uFEFF]", "") // zero-width chars
                .replaceAll("[\\n\\r\\t]+", "")             // CR, LF, TAB
                .replaceAll("\\s+$", "")                    // whitespace at end
                .replaceAll("/+$", "")                      // trailing slash
                .trim();

        System.out.println("NORMALIZED PATH = [" + path + "]");

        // Allow all auth endpoints after cleaning
        return path.startsWith("/api/auth")
                || path.startsWith("/api/admin/auth")
                || path.startsWith("/api/manager/auth")
                || path.startsWith("/api/public")
                || request.getMethod().equalsIgnoreCase("OPTIONS");
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {

        String header = request.getHeader("Authorization");

        if (!StringUtils.hasText(header) || !header.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = header.substring(7);

        if (!jwtService.isTokenValid(token)) {
            filterChain.doFilter(request, response);
            return;
        }

        String username = jwtService.extractUsername(token);
        String role = jwtService.extractRole(token);

        Long ownerId = jwtService.extractOwnerId(token);
        Long adminId = jwtService.extractAdminId(token);
        Long managerId = jwtService.extractManagerId(token);
        Set<Long> allowedPgIds = jwtService.extractAllowedPgIdsFromToken(token);
        Long pgId = jwtService.extractPgId(token);

        List<SimpleGrantedAuthority> authorities = new ArrayList<>();

        if (StringUtils.hasText(role)) {
            role = role.trim();
            if (!role.startsWith("ROLE_")) {
                role = "ROLE_" + role.toUpperCase();
            }
            authorities.add(new SimpleGrantedAuthority(role));
        }

        UsernamePasswordAuthenticationToken auth =
                new UsernamePasswordAuthenticationToken(username, null, authorities);

        auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(auth);

        if (ownerId != null) request.setAttribute("ownerId", ownerId);
        if (adminId != null) request.setAttribute("adminId", adminId);
        if (managerId != null) request.setAttribute("managerId", managerId);

        if (allowedPgIds != null && !allowedPgIds.isEmpty())
            request.setAttribute("allowedPgIds", allowedPgIds);

        if (pgId != null)
            request.setAttribute("pgId", pgId);

        filterChain.doFilter(request, response);
    }
}
