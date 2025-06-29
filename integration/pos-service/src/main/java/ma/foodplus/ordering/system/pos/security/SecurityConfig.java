package ma.foodplus.ordering.system.pos.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;

@Configuration
public class SecurityConfig {
    @Autowired
    private CashierDetailsService cashierDetailsService;
    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    // Password encoder bean
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(cashierDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    // Main security filter chain
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(
                    "/api/auth/login",
                    "/api/admin/login",
                    "/swagger-ui.html", "/swagger-ui/**", "/v3/api-docs/**"
                ).permitAll()
                // Admin endpoints - only ADMIN role can access
                .requestMatchers("/api/admin/**").hasRole("ADMIN")
                // User management - only MANAGER or ADMIN can access
                .requestMatchers("/api/users/**").hasAnyRole("MANAGER", "ADMIN")
                // Product management - only INVENTORY_MANAGER, MANAGER, or ADMIN can access
                .requestMatchers("/api/products/**").hasAnyRole("INVENTORY_MANAGER", "MANAGER", "ADMIN")
                // Terminal management - only MANAGER or ADMIN can access
                .requestMatchers("/api/terminals/**").hasAnyRole("MANAGER", "ADMIN")
                // Store management - only MANAGER or ADMIN can access
                .requestMatchers("/api/stores/**").hasAnyRole("MANAGER", "ADMIN")
                // Category management - only INVENTORY_MANAGER, MANAGER, or ADMIN can access
                .requestMatchers("/api/categories/**").hasAnyRole("INVENTORY_MANAGER", "MANAGER", "ADMIN")
                // Inventory management - only INVENTORY_MANAGER, MANAGER, or ADMIN can access
                .requestMatchers("/api/inventories/**").hasAnyRole("INVENTORY_MANAGER", "MANAGER", "ADMIN")
                // Stock movement management - only INVENTORY_MANAGER, MANAGER, or ADMIN can access
                .requestMatchers("/api/stock-movements/**").hasAnyRole("INVENTORY_MANAGER", "MANAGER", "ADMIN")
                // Tax management - only MANAGER or ADMIN can access
                .requestMatchers("/api/taxes/**").hasAnyRole("MANAGER", "ADMIN")
                // Customer management - only MANAGER or ADMIN can access
                .requestMatchers("/api/customers/**").hasAnyRole("MANAGER", "ADMIN")
                // Payment management - only MANAGER or ADMIN can access
                .requestMatchers("/api/payments/**").hasAnyRole("MANAGER", "ADMIN")
                // Dashboard and reporting - only MANAGER or ADMIN can access
                .requestMatchers("/api/dashboard/**").hasAnyRole("MANAGER", "ADMIN")
                // Only MANAGER or ADMIN can create shifts
                .requestMatchers(HttpMethod.POST, "/api/shifts", "/api/shifts/open").hasAnyRole("MANAGER", "ADMIN")
                // Only CASHIER can access shift endpoints (GET, PUT, etc.)
                .requestMatchers("/api/shifts/**").hasRole("CASHIER")
                // Only CASHIER can access cash session endpoints
                .requestMatchers("/api/caisse/**").hasRole("CASHIER")
                // Sales management - CASHIER can create/update, MANAGER/ADMIN can view all
                .requestMatchers(HttpMethod.GET, "/api/sales/**").hasAnyRole("CASHIER", "MANAGER", "ADMIN")
                .requestMatchers(HttpMethod.POST, "/api/sales/**").hasRole("CASHIER")
                .requestMatchers(HttpMethod.PUT, "/api/sales/**").hasAnyRole("CASHIER", "MANAGER", "ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/api/sales/**").hasAnyRole("MANAGER", "ADMIN")
                .anyRequest().authenticated()
            )
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
            .authenticationProvider(authenticationProvider());
        return http.build();
    }
} 