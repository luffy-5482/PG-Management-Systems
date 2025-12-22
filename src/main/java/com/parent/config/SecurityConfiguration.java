package com.parent.config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import jakarta.servlet.http.HttpServletResponse;

@Configuration
public class SecurityConfiguration {

    private final JwtService jwtService;

    public SecurityConfiguration(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter(jwtService);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
            .csrf(csrf -> csrf.disable())
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))

            // ---- STATELESS ----
            .sessionManagement(session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )

            // ---- IMPORTANT - DISABLE REQUEST CACHE ----
            .requestCache(cache -> cache.disable())

            // ---- CRITICAL: Change this from HttpStatusEntryPoint ----
            .exceptionHandling(ex -> ex
                .authenticationEntryPoint((request, response, authException) -> {
                    // For health checks, just return 200
                    if (request.getRequestURI().equals("/health") || 
                        request.getRequestURI().equals("/api/public/health") ||
                        request.getRequestURI().equals("/")) {
                        response.setStatus(HttpServletResponse.SC_OK);
                        response.getWriter().write("OK");
                    } else {
                        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    } 
                })
            )

            // ---- AUTH RULES ----
            .authorizeHttpRequests(auth -> auth
                // PUBLIC
                .requestMatchers(
                    "/",
                    "/health",
                    "/error",
                    "/error/**",
                    "/api/public/**",
                    "/api/auth/**",
                    "/api/admin/auth/**",
                    "/api/manager/auth/**",
                    "/api/tenant/auth/**",
                    "/swagger-ui.html",
                    "/swagger-ui/**",
                    "/v3/api-docs/**"
                ).permitAll()

                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                // TENANT
                .requestMatchers("/api/tenant/self/**").hasRole("TENANT")
                .requestMatchers("/api/tenant/**").hasAnyRole("OWNER","MANAGER","ADMIN")

                // PG
                .requestMatchers(HttpMethod.GET, "/api/pgs/**")
                    .hasAnyRole("OWNER","ADMIN","MANAGER")
                .requestMatchers("/api/pgs/**").hasRole("OWNER")

                // ROOMS
                .requestMatchers(HttpMethod.GET, "/api/rooms/**")
                    .hasAnyRole("OWNER","MANAGER")
                .requestMatchers("/api/rooms/**").hasRole("OWNER")

                // FLOORS
                .requestMatchers(HttpMethod.GET, "/api/floors/**")
                    .hasAnyRole("OWNER","MANAGER")
                .requestMatchers("/api/floors/**").hasRole("OWNER")

                // MANAGER / STAFF
                .requestMatchers("/api/manager/**").hasAnyRole("MANAGER","OWNER")
                .requestMatchers("/api/staff/**").hasAnyRole("OWNER","MANAGER")

                // OWNER
                .requestMatchers(
                    "/api/owners/**",
                    "/api/amenities/**",
                    "/api/property-photos/**",
                    "/api/owner/admins/**"
                ).hasRole("OWNER")

                // ADMIN
                .requestMatchers("/api/admins/me")
                    .hasAnyRole("ADMIN","OWNER")

                .anyRequest().authenticated()
            )

            // ---- JWT FILTER ----
            .addFilterBefore(
                jwtAuthenticationFilter(),
                UsernamePasswordAuthenticationFilter.class
            );

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOriginPatterns(List.of("*"));
        config.setAllowedMethods(List.of("GET","POST","PUT","PATCH","DELETE","OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        config.setExposedHeaders(List.of("Authorization"));
        config.setAllowCredentials(false);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}
