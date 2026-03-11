package com.sss.cartnest.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.List;

/**
 * Global CORS configuration for CartNest backend.
 *
 * WHY THIS IS NEEDED:
 * Your frontend (Vercel) is on a different origin than your backend (Render).
 * Browsers send a preflight OPTIONS request before every cross-origin POST/PUT/DELETE.
 * Without this config, Spring Boot (or Spring Security) rejects those OPTIONS
 * requests with 403 — which is exactly what you are seeing in the network tab.
 *
 * HOW TO USE:
 * Drop this file into your backend project at:
 *   src/main/java/com/cartnest/backend/config/CorsConfig.java
 *
 * Adjust the package name to match your actual package structure.
 * Update ALLOWED_ORIGINS with your real Vercel domain before going to production.
 */
@Configuration
public class CorsConfig {

    // ── Add every frontend origin that needs to talk to this backend ──────────
    private static final List<String> ALLOWED_ORIGINS = List.of(
        "http://localhost:5173",          // Vite dev server
        "http://localhost:3000",          // CRA / alternative dev port
        "https://cartnest.vercel.app",    // ← replace with YOUR actual Vercel URL
        "https://cartnest-git-main-yourname.vercel.app"  // preview deployments
    );

    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration config = new CorsConfiguration();

        // Allowed origins — never use "*" when credentials are involved
        config.setAllowedOrigins(ALLOWED_ORIGINS);

        // Allow all standard HTTP methods including OPTIONS (the preflight method)
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));

        // Allow the headers the browser sends on a JSON request
        config.setAllowedHeaders(List.of(
            "Content-Type",
            "Authorization",
            "Accept",
            "Origin",
            "X-Requested-With"
        ));

        // Allow cookies / Authorization headers if you add JWT later
        config.setAllowCredentials(true);

        // Cache the preflight response for 1 hour — reduces OPTIONS round-trips
        config.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config); // apply to ALL routes

        return new CorsFilter(source);
    }
}