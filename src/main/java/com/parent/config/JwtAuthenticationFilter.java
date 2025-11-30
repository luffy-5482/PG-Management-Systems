package com.parent.config;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
        String path = request.getServletPath();

        return path.startsWith("/api/auth/")
                || path.startsWith("/api/staff/auth/")
                || path.startsWith("/api/public/")
                || request.getMethod().equalsIgnoreCase("OPTIONS");  // needed for CORS preflight
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        final String header = request.getHeader("Authorization");

        // No token → continue
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
        Long staffId = jwtService.extractStaffId(token);
        Long pgId = jwtService.extractStaffPgId(token);

        List<SimpleGrantedAuthority> authorities = new ArrayList<>();

        // ROLE NORMALIZATION
        if (StringUtils.hasText(role)) {
            role = role.trim();

            if (role.toUpperCase().startsWith("ROLE_")) {
                String core = role.substring(5).toUpperCase();
                role = "ROLE_" + core;
            } else {
                role = "ROLE_" + role.toUpperCase();
            }

            authorities.add(new SimpleGrantedAuthority(role));
        }

        UsernamePasswordAuthenticationToken auth =
                new UsernamePasswordAuthenticationToken(
                        username,
                        null,
                        authorities
                );

        auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(auth);

        if (ownerId != null) request.setAttribute("ownerId", ownerId);
        if (staffId != null) request.setAttribute("staffId", staffId);
        if (pgId != null) request.setAttribute("pgId", pgId);

        filterChain.doFilter(request, response);
    }
}
