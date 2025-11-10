package com.announcements.AutomateAnnouncements.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Arrays;
import java.util.List;

@Configuration
public class CorsConfig {

    private final List<String> explicitOrigins;
    private final List<String> originPatterns;
    private final boolean wildcardMode;
    private final boolean allowCredentials;

    public CorsConfig(
            @Value("${app.cors.allowed-origins:*}") String allowedOriginsProperty,
            @Value("${app.cors.allow-credentials:true}") boolean allowCredentials) {
        var trimmed = allowedOriginsProperty.trim();
        this.allowCredentials = allowCredentials;

        if ("*".equals(trimmed)) {
            this.wildcardMode = true;
            this.explicitOrigins = List.of();
            this.originPatterns = List.of("*");
            return;
        }

        var origins = Arrays.stream(allowedOriginsProperty.split(","))
                .map(String::trim)
                .filter(StringUtils::hasText)
                .toList();

        if (origins.isEmpty()) {
            this.wildcardMode = true;
            this.explicitOrigins = List.of();
            this.originPatterns = List.of("*");
            return;
        }

        this.wildcardMode = origins.contains("*");
        this.explicitOrigins = origins.stream()
                .filter(origin -> !origin.contains("*"))
                .toList();
        this.originPatterns = origins.stream()
                .filter(origin -> origin.contains("*"))
                .map(origin -> "*".equals(origin) ? "*" : origin)
                .toList();
    }

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                var mapping = registry.addMapping("/api/**")
                        .allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS")
                        .allowedHeaders("*")
                        .exposedHeaders("Location");

                if (wildcardMode) {
                    mapping.allowedOriginPatterns("*").allowCredentials(false);
                    return;
                }

                if (!explicitOrigins.isEmpty()) {
                    mapping.allowedOrigins(explicitOrigins.toArray(new String[0]));
                }
                if (!originPatterns.isEmpty()) {
                    mapping.allowedOriginPatterns(originPatterns.toArray(new String[0]));
                }

                mapping.allowCredentials(allowCredentials);
            }
        };
    }
}
