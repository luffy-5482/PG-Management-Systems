package com.parent.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

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
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth

                // --------------------------------------------------------
                // PUBLIC (NO TOKEN REQUIRED)
                // --------------------------------------------------------
            	.requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                .requestMatchers("/api/auth/**").permitAll()          // OWNER auth
                .requestMatchers("/api/staff/auth/**").permitAll()    // STAFF login

                // --------------------------------------------------------
                // STAFF DASHBOARD ROUTES (ROLE_STAFF)
                // --------------------------------------------------------
                .requestMatchers("/api/staff/dashboard/**").hasRole("STAFF")
                .requestMatchers("/api/staff/tasks/**").hasRole("STAFF")
                .requestMatchers("/api/staff/complaints/**").hasRole("STAFF")
                .requestMatchers("/api/staff/requests/**").hasRole("STAFF")
                .requestMatchers("/api/staff/profile/**").hasRole("STAFF")
                .requestMatchers("/api/staff/tenants/**").hasRole("STAFF")

                // OWNER: staff management + see staff per PG
                .requestMatchers("/api/staff/manage/**").hasRole("OWNER")
                .requestMatchers("/api/staff/pg/**").hasRole("OWNER")

                // --------------------------------------------------------
                // PG ACCESS (OWNER + STAFF)
                // --------------------------------------------------------
                .requestMatchers("/api/pgs/**").hasAnyRole("OWNER", "STAFF")

                // --------------------------------------------------------
                // ROOMS (OWNER writes, both read)
                // --------------------------------------------------------
                .requestMatchers(HttpMethod.POST,   "/api/rooms/**").hasRole("OWNER")
                .requestMatchers(HttpMethod.PUT,    "/api/rooms/**").hasRole("OWNER")
                .requestMatchers(HttpMethod.PATCH,  "/api/rooms/**").hasRole("OWNER")
                .requestMatchers(HttpMethod.DELETE, "/api/rooms/**").hasRole("OWNER")
                .requestMatchers(HttpMethod.GET,    "/api/rooms/**").hasAnyRole("OWNER", "STAFF")

                // --------------------------------------------------------
                // FLOORS (OWNER writes, both read)
                // --------------------------------------------------------
                .requestMatchers(HttpMethod.GET,    "/api/floors/**").hasAnyRole("OWNER", "STAFF")
                .requestMatchers(HttpMethod.POST,   "/api/floors/**").hasRole("OWNER")
                .requestMatchers(HttpMethod.PUT,    "/api/floors/**").hasRole("OWNER")
                .requestMatchers(HttpMethod.PATCH,  "/api/floors/**").hasRole("OWNER")
                .requestMatchers(HttpMethod.DELETE, "/api/floors/**").hasRole("OWNER")

                // --------------------------------------------------------
                // OWNER-ONLY ROUTES
                // --------------------------------------------------------
                .requestMatchers("/api/owners/**").hasRole("OWNER")
                .requestMatchers("/api/amenities/**").hasRole("OWNER")
                .requestMatchers("/api/property-photos/**").hasRole("OWNER")

                // --------------------------------------------------------
                // ALL OTHER ROUTES
                // --------------------------------------------------------
                .anyRequest().authenticated()
            )
            .addFilterBefore(
                jwtAuthenticationFilter(),
                org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter.class
            );

        return http.build();
    }

    // --------------------------------------------------------
    // CORS CONFIG (localhost + Render)
    // --------------------------------------------------------
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {

        CorsConfiguration config = new CorsConfiguration();

        // Frontends that are allowed to call your backend
        config.setAllowedOrigins(List.of(
                "http://localhost:3000",
                "http://localhost:5173",
                "http://localhost:4200",
                "http://localhost:8080",
                "https://pgman.onrender.com",
                "http://3.110.104.186",
                "http://3.110.104.186:8080"
        ));

        config.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        config.setExposedHeaders(List.of("Authorization"));
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}
