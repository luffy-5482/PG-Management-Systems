package com.parent.config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

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

                		.requestMatchers("/error").permitAll()
                		.requestMatchers("/error/**").permitAll()
                		.requestMatchers("/api/public/health").permitAll()
                		.requestMatchers("/").permitAll()



                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                        .requestMatchers(
                                "/api/auth/**",
                                "/api/admin/auth/**",
                                "/api/manager/auth/**",
                                "/api/tenant/auth/**",
                                "/api/public/**",
                                "/swagger-ui.html",
                                "/swagger-ui/**",
                                "/v3/api-docs/**"
                        ).permitAll()

                        .requestMatchers("/api/tenant/self/**").hasRole("TENANT")
                        .requestMatchers("/api/tenant/**").hasAnyRole("OWNER","MANAGER","ADMIN")

                        .requestMatchers(HttpMethod.GET, "/api/pgs/**").hasAnyRole("OWNER","ADMIN","MANAGER")
                        .requestMatchers(HttpMethod.POST, "/api/pgs/**").hasRole("OWNER")
                        .requestMatchers(HttpMethod.PUT, "/api/pgs/**").hasRole("OWNER")
                        .requestMatchers(HttpMethod.PATCH, "/api/pgs/**").hasRole("OWNER")
                        .requestMatchers(HttpMethod.DELETE, "/api/pgs/**").hasRole("OWNER")

                        .requestMatchers(HttpMethod.GET, "/api/rooms/**").hasAnyRole("OWNER","MANAGER")
                        .requestMatchers(HttpMethod.POST, "/api/rooms/**").hasRole("OWNER")
                        .requestMatchers(HttpMethod.PUT, "/api/rooms/**").hasRole("OWNER")
                        .requestMatchers(HttpMethod.PATCH, "/api/rooms/**").hasRole("OWNER")
                        .requestMatchers(HttpMethod.DELETE, "/api/rooms/**").hasRole("OWNER")

                        .requestMatchers(HttpMethod.GET, "/api/floors/**").hasAnyRole("OWNER","MANAGER")
                        .requestMatchers(HttpMethod.POST, "/api/floors/**").hasRole("OWNER")
                        .requestMatchers(HttpMethod.PUT, "/api/floors/**").hasRole("OWNER")
                        .requestMatchers(HttpMethod.PATCH, "/api/floors/**").hasRole("OWNER")
                        .requestMatchers(HttpMethod.DELETE, "/api/floors/**").hasRole("OWNER")

                        .requestMatchers("/api/manager/**").hasAnyRole("MANAGER","OWNER")
                        .requestMatchers("/api/staff/**").hasAnyRole("OWNER","MANAGER")

                        .requestMatchers("/api/owners/**").hasRole("OWNER")
                        .requestMatchers("/api/amenities/**").hasRole("OWNER")
                        .requestMatchers("/api/property-photos/**").hasRole("OWNER")
                        .requestMatchers("/api/owner/admins/**").hasRole("OWNER")

                        .requestMatchers("/api/admins/me").hasAnyRole("ADMIN","OWNER")

                        .anyRequest().authenticated()
                )
                .addFilterBefore(
                        jwtAuthenticationFilter(),
                        org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter.class
                );

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {

        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOriginPatterns(List.of("*"));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        config.setExposedHeaders(List.of("Authorization"));
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);

        return source;
    }
}
