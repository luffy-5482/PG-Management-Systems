package com.parent.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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

        http.cors(cors -> cors.configurationSource(corsConfigurationSource()))
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth

                // --------------------------------------------------------
                // PUBLIC (NO TOKEN REQUIRED)
                // --------------------------------------------------------
                .requestMatchers("/api/auth/**").permitAll()          // OWNER auth
                .requestMatchers("/api/staff/auth/**").permitAll()    // STAFF login

                // --------------------------------------------------------
                // STAFF DASHBOARD ROUTES (TOKEN ROLE_STAFF REQUIRED)
                // --------------------------------------------------------
                .requestMatchers("/api/staff/dashboard/**").hasRole("STAFF")
                .requestMatchers("/api/staff/tasks/**").hasRole("STAFF")
                .requestMatchers("/api/staff/complaints/**").hasRole("STAFF")
                .requestMatchers("/api/staff/requests/**").hasRole("STAFF")
                .requestMatchers("/api/staff/profile/**").hasRole("STAFF")
                .requestMatchers("/api/staff/tenants/**").hasRole("STAFF")

                // --------------------------------------------------------
                // STAFF MANAGEMENT (OWNER ONLY)
                // --------------------------------------------------------
                // create/update/delete staff
                .requestMatchers("/api/staff/manage/**").hasRole("OWNER")

                // --------------------------------------------------------
                // PG ACCESS
                // --------------------------------------------------------

                // View allowed for BOTH Owner + Staff
                .requestMatchers("/api/pgs/**").hasAnyRole("OWNER", "STAFF")

                // --------------------------------------------------------
                // ROOMS & FLOORS
                // --------------------------------------------------------

                // VIEW allowed for both OWNER + STAFF
                .requestMatchers("/api/rooms/**").hasAnyRole("OWNER", "STAFF")
                .requestMatchers("/api/floors/**").hasAnyRole("OWNER", "STAFF")

                // --------------------------------------------------------
                // OWNER-ONLY ROUTES
                // --------------------------------------------------------
                .requestMatchers("/api/owners/**").hasRole("OWNER")
                .requestMatchers("/api/amenities/**").hasRole("OWNER")
                .requestMatchers("/api/property-photos/**").hasRole("OWNER")

                // --------------------------------------------------------
                // ALL OTHER ROUTES SECURED BY DEFAULT
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
    // CORS CONFIG
    // --------------------------------------------------------
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        configuration.setAllowedOrigins(List.of("*"));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("Authorization", "Content-Type"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }
}
