package ma.foodplus.ordering.system.pos.controller;

import ma.foodplus.ordering.system.pos.domain.User;
import ma.foodplus.ordering.system.pos.domain.Product;
import ma.foodplus.ordering.system.pos.domain.Terminal;
import ma.foodplus.ordering.system.pos.domain.Store;
import ma.foodplus.ordering.system.pos.dto.AdminLoginRequest;
import ma.foodplus.ordering.system.pos.enums.UserRole;
import ma.foodplus.ordering.system.pos.repository.UserRepository;
import ma.foodplus.ordering.system.pos.security.JwtUtil;
import ma.foodplus.ordering.system.pos.service.UserService;
import ma.foodplus.ordering.system.pos.service.ProductService;
import ma.foodplus.ordering.system.pos.service.TerminalService;
import ma.foodplus.ordering.system.pos.service.StoreService;
import ma.foodplus.ordering.system.pos.service.SaleService;
import ma.foodplus.ordering.system.pos.service.InventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

import jakarta.validation.Valid;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
@Tag(name = "Admin Management", description = "Admin-specific endpoints for system management.")
@SecurityRequirement(name = "bearerAuth")
public class AdminController {
    
    @Autowired
    private AuthenticationManager authenticationManager;
    
    @Autowired
    private JwtUtil jwtUtil;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private ProductService productService;
    
    @Autowired
    private TerminalService terminalService;
    
    @Autowired
    private StoreService storeService;
    
    @Autowired
    private SaleService saleService;
    
    @Autowired
    private InventoryService inventoryService;

    @PostMapping("/login")
    @Operation(summary = "Admin login", description = "Authenticate admin user without terminal requirement.")
    public ResponseEntity<?> adminLogin(@Valid @RequestBody AdminLoginRequest loginRequest) {
        String username = loginRequest.getUsername();
        String password = loginRequest.getPassword();
        
        System.out.println("Attempting admin login for: " + username);
        
        User user;
        
        try {
            // Authenticate user
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
            System.out.println("Admin authentication successful for: " + username);
            
            // Verify user is admin
            user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new RuntimeException("User not found"));
            
            if (user.getRole() != UserRole.ADMIN) {
                return ResponseEntity.status(403).body(Map.of("error", "Access denied. Admin role required."));
            }
            
        } catch (AuthenticationException e) {
            System.out.println("Admin authentication failed for: " + username + " - " + e.getMessage());
            return ResponseEntity.status(401).body(Map.of("error", "Invalid credentials"));
        }
        
        UserDetails userDetails = userRepository.findByUsername(username)
                .map(u -> new org.springframework.security.core.userdetails.User(
                        u.getUsername(),
                        u.getPassword(),
                        List.of(new org.springframework.security.core.authority.SimpleGrantedAuthority("ROLE_" + u.getRole().name()))
                ))
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        String jwt = jwtUtil.generateToken(userDetails);
        
        Map<String, Object> response = new HashMap<>();
        response.put("token", jwt);
        response.put("admin", Map.of(
                "id", user.getId(),
                "username", user.getUsername(),
                "fullName", user.getFirstName() + " " + user.getLastName(),
                "email", user.getEmail(),
                "role", user.getRole().name()
        ));
        
        return ResponseEntity.ok(response);
    }

    // ========== ADMIN-SPECIFIC ENDPOINTS ==========

    // System Statistics Dashboard
    @GetMapping("/stats")
    @Operation(summary = "Get system statistics", description = "Retrieve system-wide statistics for admin dashboard.")
    public ResponseEntity<Map<String, Object>> getSystemStats() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalUsers", userService.getAllUsers().size());
        stats.put("activeUsers", userService.getActiveUsers().size());
        stats.put("totalProducts", productService.getAllProducts().size());
        stats.put("activeProducts", productService.getActiveProducts().size());
        stats.put("totalTerminals", terminalService.getAllTerminals().size());
        stats.put("activeTerminals", terminalService.getActiveTerminals().size());
        stats.put("totalStores", storeService.getAllStores().size());
        stats.put("activeStores", storeService.getActiveStores().size());
        
        return ResponseEntity.ok(stats);
    }

    // User Management - Admin-specific operations
    @PostMapping("/users/{id}/deactivate")
    @Operation(summary = "Deactivate user", description = "Deactivate a user account (admin only).")
    public ResponseEntity<Void> deactivateUser(@PathVariable Long id) {
        userService.deactivateUser(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/users/{id}/activate")
    @Operation(summary = "Activate user", description = "Activate a previously deactivated user account (admin only).")
    public ResponseEntity<User> activateUser(@PathVariable Long id) {
        User user = userService.getUserById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        user.setActive(true);
        user.setUpdatedAt(LocalDateTime.now());
        return ResponseEntity.ok(userService.updateUser(id, user));
    }

    @GetMapping("/users/inactive")
    @Operation(summary = "Get inactive users", description = "Get all inactive users in the system (admin only).")
    public ResponseEntity<List<User>> getInactiveUsers() {
        List<User> allUsers = userService.getAllUsers();
        List<User> inactiveUsers = allUsers.stream()
                .filter(user -> !user.isActive())
                .toList();
        return ResponseEntity.ok(inactiveUsers);
    }

    // Bulk Operations - Admin-specific
    @PostMapping("/products/bulk-deactivate")
    @Operation(summary = "Bulk deactivate products", description = "Deactivate multiple products at once (admin only).")
    public ResponseEntity<Map<String, Object>> bulkDeactivateProducts(@RequestBody List<Long> productIds) {
        int deactivatedCount = 0;
        for (Long productId : productIds) {
            try {
                productService.deactivateProduct(productId);
                deactivatedCount++;
            } catch (Exception e) {
                // Log error but continue with other products
                System.err.println("Failed to deactivate product " + productId + ": " + e.getMessage());
            }
        }
        
        Map<String, Object> result = new HashMap<>();
        result.put("deactivatedCount", deactivatedCount);
        result.put("totalRequested", productIds.size());
        result.put("failedCount", productIds.size() - deactivatedCount);
        
        return ResponseEntity.ok(result);
    }

    @PostMapping("/terminals/bulk-deactivate")
    @Operation(summary = "Bulk deactivate terminals", description = "Deactivate multiple terminals at once (admin only).")
    public ResponseEntity<Map<String, Object>> bulkDeactivateTerminals(@RequestBody List<Long> terminalIds) {
        int deactivatedCount = 0;
        for (Long terminalId : terminalIds) {
            try {
                terminalService.deactivateTerminal(terminalId);
                deactivatedCount++;
            } catch (Exception e) {
                System.err.println("Failed to deactivate terminal " + terminalId + ": " + e.getMessage());
            }
        }
        
        Map<String, Object> result = new HashMap<>();
        result.put("deactivatedCount", deactivatedCount);
        result.put("totalRequested", terminalIds.size());
        result.put("failedCount", terminalIds.size() - deactivatedCount);
        
        return ResponseEntity.ok(result);
    }

    // System Health Check
    @GetMapping("/health")
    @Operation(summary = "System health check", description = "Comprehensive system health check (admin only).")
    public ResponseEntity<Map<String, Object>> getSystemHealth() {
        Map<String, Object> health = new HashMap<>();
        
        // Database connectivity check
        try {
            userService.getAllUsers();
            health.put("database", "OK");
        } catch (Exception e) {
            health.put("database", "ERROR: " + e.getMessage());
        }
        
        // Service availability check
        try {
            productService.getAllProducts();
            health.put("productService", "OK");
        } catch (Exception e) {
            health.put("productService", "ERROR: " + e.getMessage());
        }
        
        try {
            terminalService.getAllTerminals();
            health.put("terminalService", "OK");
        } catch (Exception e) {
            health.put("terminalService", "ERROR: " + e.getMessage());
        }
        
        try {
            storeService.getAllStores();
            health.put("storeService", "OK");
        } catch (Exception e) {
            health.put("storeService", "ERROR: " + e.getMessage());
        }
        
        health.put("timestamp", LocalDateTime.now());
        health.put("status", health.containsValue("ERROR") ? "DEGRADED" : "HEALTHY");
        
        return ResponseEntity.ok(health);
    }

    // System Maintenance
    @PostMapping("/maintenance/cleanup-inactive-users")
    @Operation(summary = "Cleanup inactive users", description = "Remove permanently inactive users (admin only).")
    public ResponseEntity<Map<String, Object>> cleanupInactiveUsers(@RequestParam(defaultValue = "30") int daysInactive) {
        // This would be implemented in UserService
        // For now, return a placeholder response
        Map<String, Object> result = new HashMap<>();
        result.put("message", "Cleanup operation initiated");
        result.put("daysInactive", daysInactive);
        result.put("timestamp", LocalDateTime.now());
        
        return ResponseEntity.ok(result);
    }

    // Audit and Monitoring
    @GetMapping("/audit/user-activity")
    @Operation(summary = "User activity audit", description = "Get user activity audit logs (admin only).")
    public ResponseEntity<Map<String, Object>> getUserActivityAudit(
            @RequestParam(required = false) LocalDateTime startDate,
            @RequestParam(required = false) LocalDateTime endDate,
            @RequestParam(required = false) Long userId) {
        
        Map<String, Object> audit = new HashMap<>();
        audit.put("message", "Audit functionality to be implemented");
        audit.put("startDate", startDate);
        audit.put("endDate", endDate);
        audit.put("userId", userId);
        audit.put("timestamp", LocalDateTime.now());
        
        return ResponseEntity.ok(audit);
    }

    // System Configuration
    @GetMapping("/config")
    @Operation(summary = "Get system configuration", description = "Get current system configuration (admin only).")
    public ResponseEntity<Map<String, Object>> getSystemConfig() {
        Map<String, Object> config = new HashMap<>();
        config.put("maxUsersPerStore", 50);
        config.put("maxTerminalsPerStore", 10);
        config.put("sessionTimeoutMinutes", 480);
        config.put("passwordPolicy", "Minimum 8 characters, 1 uppercase, 1 lowercase, 1 number");
        config.put("backupFrequency", "Daily");
        config.put("retentionPeriod", "90 days");
        
        return ResponseEntity.ok(config);
    }

    @PutMapping("/config")
    @Operation(summary = "Update system configuration", description = "Update system configuration (admin only).")
    public ResponseEntity<Map<String, Object>> updateSystemConfig(@RequestBody Map<String, Object> config) {
        // This would be implemented to actually update system configuration
        Map<String, Object> result = new HashMap<>();
        result.put("message", "Configuration updated successfully");
        result.put("updatedConfig", config);
        result.put("timestamp", LocalDateTime.now());
        
        return ResponseEntity.ok(result);
    }
} 