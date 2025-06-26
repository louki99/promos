package ma.foodplus.ordering.system.pos.controller;

import ma.foodplus.ordering.system.pos.domain.User;
import ma.foodplus.ordering.system.pos.repository.UserRepository;
import ma.foodplus.ordering.system.pos.security.CashierDetailsService;
import ma.foodplus.ordering.system.pos.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private CashierDetailsService cashierDetailsService;
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private UserRepository userRepository;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> loginRequest) {
        String username = loginRequest.get("username");
        String password = loginRequest.get("password");
        System.out.println("Attempting login for: " + username);
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
            System.out.println("Authentication successful for: " + username);
        } catch (AuthenticationException e) {
            System.out.println("Authentication failed for: " + username + " - " + e.getMessage());
            return ResponseEntity.status(401).body("Invalid credentials");
        }
        UserDetails userDetails = cashierDetailsService.loadUserByUsername(username);
        String jwt = jwtUtil.generateToken(userDetails);
        User user = userRepository.findByUsername(username).orElseThrow();
        Map<String, Object> response = new HashMap<>();
        response.put("token", jwt);
        response.put("cashier", Map.of(
                "id", user.getId(),
                "username", user.getUsername(),
                "fullName", user.getFirstName() + " " + user.getLastName()
        ));
        return ResponseEntity.ok(response);
    }
} 