package com.github.hantsy.ee8sample;

import java.util.Set;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

/**
 * The one exception to the fail-closed access control of SecurityConfig: the
 * health probes answer without a login, so an orchestrator or a container
 * HEALTHCHECK can call them. The chain matches exactly three GET requests
 * (HEALTH_PATHS, compared to the request URI minus the context path); every
 * other request, /actuator/prometheus included, falls through to SecurityConfig
 * and needs an authenticated user. The health body is {"status":"UP"} only
 * (management.endpoint.health.show-details=never).
 */
@Configuration
public class ActuatorSecurityConfig {

    private static final Set<String> HEALTH_PATHS = Set.of("/actuator/health", "/actuator/health/liveness", "/actuator/health/readiness");

    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    SecurityFilterChain actuatorHealthFilterChain(HttpSecurity http) throws Exception {
        http
                .securityMatcher(request -> "GET".equals(request.getMethod())
                        && HEALTH_PATHS.contains(request.getRequestURI().substring(request.getContextPath().length())))
                .authorizeHttpRequests(requests -> requests.anyRequest().permitAll())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .csrf(csrf -> csrf.disable());
        return http.build();
    }
}
