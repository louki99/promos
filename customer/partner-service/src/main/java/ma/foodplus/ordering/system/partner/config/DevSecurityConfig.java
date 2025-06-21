package ma.foodplus.ordering.system.partner.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Development Security Configuration - Disables all security for development.
 * 
 * <p>This configuration is only active when the 'dev' profile is active.
 * It allows all requests without authentication or authorization.</p>
 * 
 * <p><strong>WARNING:</strong> This should NEVER be used in production!</p>
 * 
 * @author FoodPlus Development Team
 * @version 3.0.0
 */
@Configuration
@EnableWebSecurity
@Profile("dev")
public class DevSecurityConfig {
    
    /**
     * Configures security to allow all requests without authentication.
     * 
     * @param http the HTTP security configuration
     * @return the security filter chain
     * @throws Exception if configuration fails
     */
    @Bean
    public SecurityFilterChain devFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(AbstractHttpConfigurer::disable)
            .cors(AbstractHttpConfigurer::disable)
            .authorizeHttpRequests(authz -> authz
                .anyRequest().permitAll()
            );
        
        return http.build();
    }
} 