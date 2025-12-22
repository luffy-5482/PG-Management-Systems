package com.parent.config;

import java.io.IOException;	
import java.util.ArrayList;
import java.util.List;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    public JwtAuthenticationFilter(JwtService jwtService) {
        this.jwtService = jwtService;
    }
    
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {

        String path = request.getRequestURI();

        return path.equals("/health")
        	||path.equals("/")
        	||path.equals("/api/public/health")
            || path.startsWith("/error")
            || path.startsWith("/api/auth")
            || path.startsWith("/api/admin/auth")
            || path.startsWith("/api/manager/auth")
            || path.startsWith("/api/tenant/auth")
            || request.getMethod().equalsIgnoreCase("OPTIONS");
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain chain)
            throws ServletException, IOException {

        String header = request.getHeader("Authorization");

        // No token → let Spring Security decide based on config
        if (!StringUtils.hasText(header) || !header.startsWith("Bearer ")) {
            chain.doFilter(request, response); 
            return;
        } 

        String token = header.substring(7);

        if (!jwtService.isTokenValid(token)) {
            chain.doFilter(request, response);
            return;
        }

        // ---- EXTRACT CONTEXT ----
        Long tenantId  = jwtService.extractTenantId(token);
        Long ownerId   = jwtService.extractOwnerId(token);
        Long adminId   = jwtService.extractAdminId(token);
        Long managerId = jwtService.extractManagerId(token);

        String username = jwtService.extractUsername(token);
        String role     = jwtService.extractRole(token);

        if (tenantId != null)  request.setAttribute("tenantId", tenantId);
        if (ownerId != null)   request.setAttribute("ownerId", ownerId);
        if (adminId != null)   request.setAttribute("adminId", adminId);
        if (managerId != null) request.setAttribute("managerId", managerId);

        request.setAttribute("permissions", jwtService.extractPermissions(token));
        request.setAttribute("allowedPgIds", jwtService.extractAllowedPgIdsFromToken(token));

        // ---- AUTHORITIES ----
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        if (role != null) {
            authorities.add(new SimpleGrantedAuthority("ROLE_" + role));
        }

        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(username, null, authorities);

        SecurityContextHolder.getContext().setAuthentication(authentication);
        chain.doFilter(request, response);
    }
}
