package ma.foodplus.ordering.system.pos.controller;

import ma.foodplus.ordering.system.pos.domain.Sale;
import ma.foodplus.ordering.system.pos.domain.User;
import ma.foodplus.ordering.system.pos.dto.SaleCreateRequest;
import ma.foodplus.ordering.system.pos.dto.SaleResponse;
import ma.foodplus.ordering.system.pos.enums.PaymentMethod;
import ma.foodplus.ordering.system.pos.repository.UserRepository;
import ma.foodplus.ordering.system.pos.service.SaleService;
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
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/sales")
@Tag(name = "Sales Management", description = "Endpoints for managing sales transactions and orders.")
@SecurityRequirement(name = "bearerAuth")
@Validated
public class SaleController {
    @Autowired
    private SaleService saleService;

    @Autowired
    private UserRepository userRepository;

    @GetMapping
    @Operation(summary = "Get all sales", description = "Retrieve a list of all sales transactions.")
    public List<Sale> getAllSales() {
        return saleService.getAllSales();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get sale by ID", description = "Retrieve a sale transaction by its unique ID.")
    public ResponseEntity<SaleResponse> getSaleById(@PathVariable Long id) {
        return saleService.getSaleResponseById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/customer/{customerId}")
    @Operation(summary = "Get sales by customer", description = "Retrieve all sales for a specific customer.")
    public List<Sale> getSalesByCustomer(@PathVariable Long customerId) {
        return saleService.getSalesByPartner(customerId);
    }

    @GetMapping("/store/{storeId}")
    @Operation(summary = "Get sales by store", description = "Retrieve all sales for a specific store.")
    public List<Sale> getSalesByStore(@PathVariable Long storeId) {
        return saleService.getSalesByStore(storeId);
    }

    @GetMapping("/date-range")
    @Operation(summary = "Get sales by date range", description = "Retrieve sales within a specific date range.")
    public List<Sale> getSalesByDateRange(@RequestParam LocalDateTime start, @RequestParam LocalDateTime end) {
        return saleService.getSalesByDateRange(start, end);
    }

    @PostMapping
    @Operation(summary = "Create sale", description = "Create a new sale transaction with proper session validation.")
    public ResponseEntity<SaleResponse> createSale(@Valid @RequestBody SaleCreateRequest request) {
        Long cashierId = getCurrentCashierId();
        SaleResponse saleResponse = saleService.createSaleFromRequest(request, cashierId);
        return ResponseEntity.ok(saleResponse);
    }

    @PostMapping("/legacy")
    @Operation(summary = "Create sale (legacy)", description = "Create a new sale transaction using the legacy entity-based approach.")
    public Sale createSaleLegacy(@RequestBody Sale sale) {
        return saleService.createSale(sale);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update sale", description = "Update an existing sale transaction by ID.")
    public Sale updateSale(@PathVariable Long id, @RequestBody Sale sale) {
        return saleService.updateSale(id, sale);
    }

    @PutMapping("/{id}/pay")
    @Operation(summary = "Process sale payment", description = "Process payment for a sale transaction.")
    public Sale processSalePayment(@PathVariable Long id, @RequestParam BigDecimal paidAmount, @RequestParam PaymentMethod paymentMethod) {
        return saleService.processSalePayment(id, paidAmount, paymentMethod);
    }

    @PutMapping("/{id}/cancel")
    @Operation(summary = "Cancel sale", description = "Cancel a sale transaction and restore inventory.")
    public void cancelSale(@PathVariable Long id) {
        saleService.cancelSale(id);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete sale", description = "Delete a sale transaction by ID.")
    public ResponseEntity<Void> deleteSale(@PathVariable Long id) {
        saleService.deleteSale(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/validate-session")
    @Operation(summary = "Validate session for sale", description = "Check if current cashier can create sales for a specific terminal.")
    public ResponseEntity<Object> validateSessionForSale(
            @RequestParam Long storeId, 
            @RequestParam Long terminalId) {
        Long cashierId = getCurrentCashierId();
        boolean canCreate = saleService.canCreateSale(cashierId, storeId, terminalId);
        
        if (canCreate) {
            Optional<ma.foodplus.ordering.system.pos.domain.CashSession> session = 
                saleService.getCurrentSessionForSale(cashierId, storeId, terminalId);
            
            return ResponseEntity.ok(Map.of(
                "canCreate", true,
                "sessionId", session.map(s -> s.getSessionId()).orElse(null),
                "message", "Session is valid for creating sales"
            ));
        } else {
            return ResponseEntity.ok(Map.of(
                "canCreate", false,
                "message", "No active cash session found for this terminal"
            ));
        }
    }

    // Helper method to get current cashier ID from security context
    private Long getCurrentCashierId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof org.springframework.security.core.userdetails.UserDetails) {
            String username = ((org.springframework.security.core.userdetails.UserDetails) authentication.getPrincipal()).getUsername();
            User user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new RuntimeException("User not found"));
            return user.getId();
        }
        throw new RuntimeException("Unable to determine current cashier");
    }
} 