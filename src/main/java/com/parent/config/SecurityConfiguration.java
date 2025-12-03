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

        http.cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth

                        // --------------------------------------------------------
                        // PUBLIC (NO TOKEN REQUIRED)
                        // --------------------------------------------------------
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        .requestMatchers("/api/auth/**").permitAll()           // owner login
                        .requestMatchers("/api/admin/auth/**").permitAll()     // admin login
                        .requestMatchers("/api/manager/auth/**").permitAll()   // manager login

                        // --------------------------------------------------------
                        // PG ACCESS
                        // --------------------------------------------------------

                        // GET allowed for OWNER + ADMIN + MANAGER
                        .requestMatchers(HttpMethod.GET, "/api/pgs/**")
                            .hasAnyRole("OWNER", "ADMIN", "MANAGER")

                        // CREATE/UPDATE/DELETE PG allowed ONLY for OWNER
                        .requestMatchers(HttpMethod.POST, "/api/pgs/**").hasRole("OWNER")
                        .requestMatchers(HttpMethod.PUT, "/api/pgs/**").hasRole("OWNER")
                        .requestMatchers(HttpMethod.PATCH, "/api/pgs/**").hasRole("OWNER")
                        .requestMatchers(HttpMethod.DELETE, "/api/pgs/**").hasRole("OWNER")


                        // --------------------------------------------------------
                        // ROOMS
                        // --------------------------------------------------------
                        .requestMatchers(HttpMethod.GET, "/api/rooms/**")
                            .hasAnyRole("OWNER", "MANAGER")

                        .requestMatchers(HttpMethod.POST, "/api/rooms/**").hasRole("OWNER")
                        .requestMatchers(HttpMethod.PUT, "/api/rooms/**").hasRole("OWNER")
                        .requestMatchers(HttpMethod.PATCH, "/api/rooms/**").hasRole("OWNER")
                        .requestMatchers(HttpMethod.DELETE, "/api/rooms/**").hasRole("OWNER")


                        // --------------------------------------------------------
                        // FLOORS
                        // --------------------------------------------------------
                        .requestMatchers(HttpMethod.GET, "/api/floors/**")
                            .hasAnyRole("OWNER", "MANAGER")

                        .requestMatchers(HttpMethod.POST, "/api/floors/**").hasRole("OWNER")
                        .requestMatchers(HttpMethod.PUT, "/api/floors/**").hasRole("OWNER")
                        .requestMatchers(HttpMethod.PATCH, "/api/floors/**").hasRole("OWNER")
                        .requestMatchers(HttpMethod.DELETE, "/api/floors/**").hasRole("OWNER")


                        // --------------------------------------------------------
                        // MANAGER ROUTES
                        // --------------------------------------------------------
                        .requestMatchers("/api/manager/**")
                            .hasAnyRole("MANAGER", "OWNER")


                        // --------------------------------------------------------
                        // STAFF ROUTES
                        // --------------------------------------------------------
                        .requestMatchers("/api/staff/**")
                            .hasAnyRole("OWNER", "MANAGER")


                        // --------------------------------------------------------
                        // OWNER ROUTES
                        // --------------------------------------------------------
                        .requestMatchers("/api/owners/**").hasRole("OWNER")
                        .requestMatchers("/api/amenities/**").hasRole("OWNER")
                        .requestMatchers("/api/property-photos/**").hasRole("OWNER")
                        .requestMatchers("/api/owner/admins/**").hasRole("OWNER")


                        // --------------------------------------------------------
                        // ADMIN ROUTES
                        // --------------------------------------------------------
                        .requestMatchers("/api/admins/me")
                            .hasAnyRole("ADMIN", "OWNER")


                        // --------------------------------------------------------
                        // EVERYTHING ELSE MUST BE AUTHENTICATED
                        // --------------------------------------------------------
                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtAuthenticationFilter(),
                        org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    // --------------------------------------------------------
    // CORS CONFIG
    // --------------------------------------------------------
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {

        CorsConfiguration config = new CorsConfiguration();

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
