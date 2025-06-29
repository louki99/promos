package ma.foodplus.ordering.system.pos.controller;

import ma.foodplus.ordering.system.pos.domain.CashSession;
import ma.foodplus.ordering.system.pos.domain.User;
import ma.foodplus.ordering.system.pos.dto.CashSessionCloseRequest;
import ma.foodplus.ordering.system.pos.dto.CashSessionOpenRequest;
import ma.foodplus.ordering.system.pos.repository.UserRepository;
import ma.foodplus.ordering.system.pos.service.CashSessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

import jakarta.validation.Valid;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/caisse")
@Tag(name = "Cash Session Management", description = "Endpoints for managing cash register sessions.")
@SecurityRequirement(name = "bearerAuth")
@Validated
public class CashSessionController {

    @Autowired
    private CashSessionService cashSessionService;

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/open")
    @Operation(summary = "Open cash session", description = "Open a new cash session with initial amount. Only CASHIER role can access.")
    public ResponseEntity<CashSession> openSession(@Valid @RequestBody CashSessionOpenRequest request) {
        Long cashierId = getCurrentCashierId();
        CashSession session = cashSessionService.openSession(cashierId, request);
        return ResponseEntity.ok(session);
    }

    @PostMapping("/close")
    @Operation(summary = "Close cash session", description = "Close the current cash session with cash collected amount. Only CASHIER role can access.")
    public ResponseEntity<CashSession> closeSession(@Valid @RequestBody CashSessionCloseRequest request) {
        Long cashierId = getCurrentCashierId();
        CashSession session = cashSessionService.closeSession(cashierId, request);
        return ResponseEntity.ok(session);
    }

    @GetMapping("/current")
    @Operation(summary = "Get current session", description = "Get the current open cash session for the logged-in cashier. Only CASHIER role can access.")
    public ResponseEntity<?> getCurrentSession() {
        Long cashierId = getCurrentCashierId();
        Optional<CashSession> session = cashSessionService.getCurrentSession(cashierId);
        
        if (session.isPresent()) {
            return ResponseEntity.ok(session.get());
        } else {
            Map<String, String> response = new HashMap<>();
            response.put("message", "No open session found");
            response.put("status", "NO_SESSION");
            return ResponseEntity.ok(response);
        }
    }

    @GetMapping("/current/{storeId}")
    @Operation(summary = "Get current session by store", description = "Get the current open cash session for the logged-in cashier in a specific store. Only CASHIER role can access.")
    public ResponseEntity<?> getCurrentSessionByStore(@PathVariable Long storeId) {
        Long cashierId = getCurrentCashierId();
        Optional<CashSession> session = cashSessionService.getCurrentSession(cashierId, storeId);
        
        if (session.isPresent()) {
            return ResponseEntity.ok(session.get());
        } else {
            Map<String, String> response = new HashMap<>();
            response.put("message", "No open session found for this store");
            response.put("status", "NO_SESSION");
            return ResponseEntity.ok(response);
        }
    }

    @GetMapping("/current/{storeId}/{terminalId}")
    @Operation(summary = "Get current session by store and terminal", description = "Get the current open cash session for the logged-in cashier in a specific store and terminal. Only CASHIER role can access.")
    public ResponseEntity<?> getCurrentSessionByStoreAndTerminal(@PathVariable Long storeId, @PathVariable Long terminalId) {
        Long cashierId = getCurrentCashierId();
        Optional<CashSession> session = cashSessionService.getCurrentSession(cashierId, storeId, terminalId);
        
        if (session.isPresent()) {
            return ResponseEntity.ok(session.get());
        } else {
            Map<String, String> response = new HashMap<>();
            response.put("message", "No open session found for this store and terminal");
            response.put("status", "NO_SESSION");
            return ResponseEntity.ok(response);
        }
    }

    @GetMapping("/history")
    @Operation(summary = "Get session history", description = "Get all cash sessions for the logged-in cashier. Only CASHIER role can access.")
    public ResponseEntity<List<CashSession>> getSessionHistory() {
        Long cashierId = getCurrentCashierId();
        List<CashSession> sessions = cashSessionService.getSessionsByCashier(cashierId);
        return ResponseEntity.ok(sessions);
    }

    @GetMapping("/history/{storeId}")
    @Operation(summary = "Get session history by store", description = "Get all cash sessions for a specific store. Only CASHIER role can access.")
    public ResponseEntity<List<CashSession>> getSessionHistoryByStore(@PathVariable Long storeId) {
        List<CashSession> sessions = cashSessionService.getSessionsByStore(storeId);
        return ResponseEntity.ok(sessions);
    }

    @GetMapping("/history/{storeId}/date-range")
    @Operation(summary = "Get session history by date range", description = "Get cash sessions for a store within a date range. Only CASHIER role can access.")
    public ResponseEntity<List<CashSession>> getSessionHistoryByDateRange(
            @PathVariable Long storeId,
            @RequestParam LocalDateTime startDate,
            @RequestParam LocalDateTime endDate) {
        List<CashSession> sessions = cashSessionService.getSessionsByStoreAndDateRange(storeId, startDate, endDate);
        return ResponseEntity.ok(sessions);
    }

    @GetMapping("/{sessionId}")
    @Operation(summary = "Get session by ID", description = "Get a specific cash session by its ID. Only CASHIER role can access.")
    public ResponseEntity<CashSession> getSessionById(@PathVariable String sessionId) {
        CashSession session = cashSessionService.getSessionById(java.util.UUID.fromString(sessionId));
        return ResponseEntity.ok(session);
    }

    @GetMapping("/status")
    @Operation(summary = "Check session status", description = "Check if the logged-in cashier has an open session. Only CASHIER role can access.")
    public ResponseEntity<Map<String, Object>> checkSessionStatus() {
        Long cashierId = getCurrentCashierId();
        boolean hasOpenSession = cashSessionService.hasOpenSession(cashierId);
        
        Map<String, Object> response = new HashMap<>();
        response.put("hasOpenSession", hasOpenSession);
        response.put("cashierId", cashierId);
        
        if (hasOpenSession) {
            Optional<CashSession> session = cashSessionService.getCurrentSession(cashierId);
            session.ifPresent(s -> {
                response.put("sessionId", s.getSessionId());
                response.put("openedAt", s.getOpenedAt());
                response.put("initialAmount", s.getInitialAmount());
                response.put("totalSales", s.getTotalSales());
                response.put("expectedCash", s.getExpectedCash());
            });
        }
        
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{sessionId}")
    @Operation(summary = "Delete session", description = "Delete a closed cash session. Only CASHIER role can access.")
    public ResponseEntity<Void> deleteSession(@PathVariable String sessionId) {
        cashSessionService.deleteSession(java.util.UUID.fromString(sessionId));
        return ResponseEntity.noContent().build();
    }

    // Helper method to get current cashier ID from security context
    private Long getCurrentCashierId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof org.springframework.security.core.userdetails.UserDetails) {
            String username = ((org.springframework.security.core.userdetails.UserDetails) authentication.getPrincipal()).getUsername();
            // Get user by username to get the ID
            User user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new RuntimeException("User not found"));
            return user.getId();
        }
        throw new RuntimeException("Unable to determine current cashier");
    }
} 