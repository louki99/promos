package ma.foodplus.ordering.system.partner.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Security configuration for the Partner Service with Keycloak integration.
 * 
 * <p>This configuration sets up OAuth2 resource server with JWT tokens from Keycloak
 * and implements role-based access control for the partner management APIs.</p>
 * 
 * <p>This configuration is only active in non-development profiles (prod, staging, etc.)</p>
 * 
 * @author FoodPlus Development Team
 * @version 3.0.0
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
@Profile("!dev") // Only active when NOT in dev profile
public class SecurityConfig {
    
    /**
     * Configures the security filter chain with OAuth2 resource server.
     * 
     * @param http the HTTP security configuration
     * @return the security filter chain
     * @throws Exception if configuration fails
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(AbstractHttpConfigurer::disable)
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(authz -> authz
                // Public endpoints
                .requestMatchers("/actuator/health", "/actuator/info").permitAll()
                .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()
                
                // Partner management endpoints
                .requestMatchers("/api/v1/partners/**").hasAnyRole("PARTNER_MANAGER", "ADMIN", "SYSTEM")
                .requestMatchers("/api/v1/b2b-partners/**").hasAnyRole("B2B_MANAGER", "PARTNER_MANAGER", "ADMIN")
                .requestMatchers("/api/v1/b2c-partners/**").hasAnyRole("B2C_MANAGER", "PARTNER_MANAGER", "ADMIN")
                .requestMatchers("/api/v1/supplier-partners/**").hasAnyRole("SUPPLIER_MANAGER", "PARTNER_MANAGER", "ADMIN")
                
                // Contract management endpoints
                .requestMatchers("/api/v1/contracts/**").hasAnyRole("CONTRACT_MANAGER", "PARTNER_MANAGER", "ADMIN")
                
                // Credit management endpoints
                .requestMatchers("/api/v1/credit/**").hasAnyRole("CREDIT_MANAGER", "FINANCE_MANAGER", "ADMIN")
                
                // Analytics and reporting endpoints
                .requestMatchers("/api/v1/analytics/**", "/api/v1/statistics/**").hasAnyRole("ANALYST", "MANAGER", "ADMIN")
                
                // Audit and history endpoints
                .requestMatchers("/api/v1/audit/**").hasAnyRole("AUDITOR", "ADMIN")
                
                // Bulk operations
                .requestMatchers("/api/v1/bulk/**").hasAnyRole("PARTNER_MANAGER", "ADMIN")
                
                // All other endpoints require authentication
                .anyRequest().authenticated()
            )
            .oauth2ResourceServer(oauth2 -> oauth2
                .jwt(jwt -> jwt
                    .jwtAuthenticationConverter(jwtAuthenticationConverter())
                )
            );
        
        return http.build();
    }
    
    /**
     * Configures CORS for cross-origin requests.
     * 
     * @return the CORS configuration source
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOriginPatterns(List.of("*"));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);
        configuration.setMaxAge(3600L);
        
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
    
    /**
     * Creates a JWT authentication converter that extracts roles from Keycloak JWT tokens.
     * 
     * @return the JWT authentication converter
     */
    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtGrantedAuthoritiesConverter grantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
        grantedAuthoritiesConverter.setAuthoritiesClaimName("realm_access.roles");
        grantedAuthoritiesConverter.setAuthorityPrefix("ROLE_");
        
        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(grantedAuthoritiesConverter);
        
        return jwtAuthenticationConverter;
    }
    
    /**
     * Custom JWT granted authorities converter for Keycloak roles.
     * 
     * @return the custom converter
     */
    @Bean
    public Converter<Jwt, Collection<GrantedAuthority>> keycloakRoleConverter() {
        return jwt -> {
            Map<String, Object> realmAccess = jwt.getClaim("realm_access");
            if (realmAccess == null) {
                return List.of();
            }
            
            @SuppressWarnings("unchecked")
            List<String> roles = (List<String>) realmAccess.get("roles");
            
            if (roles == null) {
                return List.of();
            }
            
            return roles.stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role.toUpperCase()))
                .collect(Collectors.toList());
        };
    }
} 