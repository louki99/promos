package ma.foodplus.ordering.system.pos.controller;

import ma.foodplus.ordering.system.pos.domain.User;
import ma.foodplus.ordering.system.pos.dto.LoginRequest;
import ma.foodplus.ordering.system.pos.enums.UserRole;
import ma.foodplus.ordering.system.pos.repository.UserRepository;
import ma.foodplus.ordering.system.pos.security.CashierDetailsService;
import ma.foodplus.ordering.system.pos.security.JwtUtil;
import ma.foodplus.ordering.system.pos.service.TerminalAuthorizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Authentication", description = "Endpoints for user authentication and authorization.")
public class AuthController {
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private CashierDetailsService cashierDetailsService;
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TerminalAuthorizationService terminalAuthorizationService;

    @PostMapping("/login")
    @Operation(summary = "User login", description = "Authenticate user and validate terminal authorization for non-admin users.")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest loginRequest) {
        String username = loginRequest.getUsername();
        String password = loginRequest.getPassword();
        Long terminalId = loginRequest.getTerminalId();
        
        System.out.println("Attempting login for: " + username + " on terminal: " + terminalId);
        
        try {
            // Authenticate user
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
            System.out.println("Authentication successful for: " + username);
            
            // Get user details to check role
            User user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new RuntimeException("User not found"));
            
            // For admin users, skip terminal authorization
            if (user.getRole() == UserRole.ADMIN) {
                System.out.println("Admin user detected, skipping terminal authorization");
                
                UserDetails userDetails = cashierDetailsService.loadUserByUsername(username);
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
            
            // For non-admin users, validate terminal authorization
            if (terminalId == null) {
                return ResponseEntity.status(400).body(Map.of("error", "Terminal ID is required for non-admin users"));
            }
            
            terminalAuthorizationService.validateTerminalAuthorization(username, terminalId);
            System.out.println("Terminal authorization successful for: " + username + " on terminal: " + terminalId);
            
        } catch (AuthenticationException e) {
            System.out.println("Authentication failed for: " + username + " - " + e.getMessage());
            return ResponseEntity.status(401).body(Map.of("error", "Invalid credentials"));
        } catch (RuntimeException e) {
            System.out.println("Terminal authorization failed for: " + username + " - " + e.getMessage());
            return ResponseEntity.status(403).body(Map.of("error", e.getMessage()));
        }
        
        UserDetails userDetails = cashierDetailsService.loadUserByUsername(username);
        String jwt = jwtUtil.generateToken(userDetails);
        User user = userRepository.findByUsername(username).orElseThrow();
        
        Map<String, Object> response = new HashMap<>();
        response.put("token", jwt);
        response.put("terminalId", terminalId);
        response.put("cashier", Map.of(
                "id", user.getId(),
                "username", user.getUsername(),
                "fullName", user.getFirstName() + " " + user.getLastName(),
                "storeId", user.getStore() != null ? user.getStore().getId() : null,
                "role", user.getRole().name()
        ));
        
        return ResponseEntity.ok(response);
    }
} 