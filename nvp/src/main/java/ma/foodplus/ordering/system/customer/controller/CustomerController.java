package ma.foodplus.ordering.system.customer.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ma.foodplus.ordering.system.customer.dto.CustomerDTO;
import ma.foodplus.ordering.system.customer.model.CustomerType;
import ma.foodplus.ordering.system.customer.service.CustomerService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.Parameter;
import ma.foodplus.ordering.system.common.dto.ErrorResponse;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/v1/customers")
@RequiredArgsConstructor
@Tag(name = "Customer Management", description = "APIs for managing customers")
public class CustomerController {

    private final CustomerService customerService;

    @PostMapping
    @Operation(summary = "Create a new customer", description = "Creates a new customer in the system.")
    @ApiResponse(responseCode = "201", description = "Customer created successfully", content = @Content(schema = @Schema(implementation = CustomerDTO.class)))
    @ApiResponse(responseCode = "400", description = "Invalid customer data", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    public ResponseEntity<CustomerDTO> createCustomer(@Valid @RequestBody CustomerDTO customerDTO) {
        log.info("Creating new customer: {}", customerDTO);
        return new ResponseEntity<>(customerService.createCustomer(customerDTO), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an existing customer", description = "Updates the details of an existing customer by ID.")
    @ApiResponse(responseCode = "200", description = "Customer updated successfully", content = @Content(schema = @Schema(implementation = CustomerDTO.class)))
    @ApiResponse(responseCode = "404", description = "Customer not found", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    public ResponseEntity<CustomerDTO> updateCustomer(
            @Parameter(description = "Customer ID", required = true) @PathVariable Long id,
            @Valid @RequestBody CustomerDTO customerDTO) {
        log.info("Updating customer with id {}: {}", id, customerDTO);
        return ResponseEntity.ok(customerService.updateCustomer(id, customerDTO));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a customer", description = "Deletes a customer by ID.")
    @ApiResponse(responseCode = "204", description = "Customer deleted successfully")
    @ApiResponse(responseCode = "404", description = "Customer not found", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    public ResponseEntity<Void> deleteCustomer(@Parameter(description = "Customer ID", required = true) @PathVariable Long id) {
        log.info("Deleting customer with id: {}", id);
        customerService.deleteCustomer(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a customer by ID", description = "Retrieves a customer by their unique ID.")
    @ApiResponse(responseCode = "200", description = "Customer found", content = @Content(schema = @Schema(implementation = CustomerDTO.class)))
    @ApiResponse(responseCode = "404", description = "Customer not found", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    public ResponseEntity<CustomerDTO> getCustomerById(@Parameter(description = "Customer ID", required = true) @PathVariable Long id) {
        log.info("Getting customer with id: {}", id);
        return ResponseEntity.ok(customerService.getCustomerById(id));
    }

    @GetMapping("/ct-num/{ctNum}")
    @Operation(summary = "Get a customer by CT number", description = "Retrieves a customer by CT number.")
    @ApiResponse(responseCode = "200", description = "Customer found", content = @Content(schema = @Schema(implementation = CustomerDTO.class)))
    @ApiResponse(responseCode = "404", description = "Customer not found", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    public ResponseEntity<CustomerDTO> getCustomerByCtNum(@Parameter(description = "Customer CT number", required = true) @PathVariable String ctNum) {
        return ResponseEntity.ok(customerService.getCustomerByCtNum(ctNum));
    }

    @GetMapping("/ice/{ice}")
    @Operation(summary = "Get a customer by ICE", description = "Retrieves a customer by ICE.")
    @ApiResponse(responseCode = "200", description = "Customer found", content = @Content(schema = @Schema(implementation = CustomerDTO.class)))
    @ApiResponse(responseCode = "404", description = "Customer not found", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    public ResponseEntity<CustomerDTO> getCustomerByIce(@Parameter(description = "Customer ICE", required = true) @PathVariable String ice) {
        return ResponseEntity.ok(customerService.getCustomerByIce(ice));
    }

    @GetMapping
    @Operation(summary = "Get all customers", description = "Retrieves all customers in the system.")
    @ApiResponse(responseCode = "200", description = "Customers retrieved", content = @Content(schema = @Schema(implementation = CustomerDTO.class)))
    public ResponseEntity<Page<CustomerDTO>> getAllCustomers(Pageable pageable) {
        log.info("Getting all customers with pagination");
        return ResponseEntity.ok(customerService.getAllCustomers(pageable));
    }

    @GetMapping("/active")
    @Operation(summary = "Get all active customers", description = "Retrieves all active customers.")
    @ApiResponse(responseCode = "200", description = "Active customers retrieved", content = @Content(schema = @Schema(implementation = CustomerDTO.class)))
    public ResponseEntity<List<CustomerDTO>> getAllActiveCustomers() {
        return ResponseEntity.ok(customerService.getAllActiveCustomers());
    }

    @GetMapping("/category-tarif/{categoryTarifId}")
    @Operation(summary = "Get customers by category tariff ID", description = "Retrieves customers by their category tariff ID.")
    @ApiResponse(responseCode = "200", description = "Customers retrieved", content = @Content(schema = @Schema(implementation = CustomerDTO.class)))
    public ResponseEntity<List<CustomerDTO>> getCustomersByCategoryTarif(@Parameter(description = "Category Tariff ID", required = true) @PathVariable Long categoryTarifId) {
        return ResponseEntity.ok(customerService.getCustomersByCategoryTarif(categoryTarifId));
    }

    @GetMapping("/search")
    @Operation(summary = "Search customers by description")
    public ResponseEntity<Page<CustomerDTO>> searchCustomers(
            @RequestParam String searchTerm,
            Pageable pageable) {
        log.info("Searching customers with term: {}", searchTerm);
        return ResponseEntity.ok(customerService.searchCustomers(searchTerm, pageable));
    }

    @PutMapping("/{id}/activate")
    @Operation(summary = "Activate a customer")
    public ResponseEntity<Void> activateCustomer(@PathVariable Long id) {
        customerService.activateCustomer(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}/deactivate")
    @Operation(summary = "Deactivate a customer")
    public ResponseEntity<Void> deactivateCustomer(@PathVariable Long id) {
        customerService.deactivateCustomer(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/type/{type}")
    public ResponseEntity<List<CustomerDTO>> getCustomersByType(@PathVariable CustomerType type) {
        log.info("Getting customers by type: {}", type);
        return ResponseEntity.ok(customerService.getCustomersByType(type));
    }

    @GetMapping("/group/{groupId}")
    public ResponseEntity<List<CustomerDTO>> getCustomersByGroup(@PathVariable Long groupId) {
        log.info("Getting customers by group id: {}", groupId);
        return ResponseEntity.ok(customerService.getCustomersByGroup(groupId));
    }

    @GetMapping("/vip")
    public ResponseEntity<List<CustomerDTO>> getVipCustomers() {
        log.info("Getting VIP customers");
        return ResponseEntity.ok(customerService.getVipCustomers());
    }

    @GetMapping("/credit-rating/{rating}")
    public ResponseEntity<List<CustomerDTO>> getCustomersByCreditRating(@PathVariable String rating) {
        log.info("Getting customers by credit rating: {}", rating);
        return ResponseEntity.ok(customerService.getCustomersByCreditRating(rating));
    }

    @GetMapping("/expiring-contracts")
    public ResponseEntity<List<CustomerDTO>> getCustomersWithExpiringContracts(
            @RequestParam(defaultValue = "30") int daysThreshold) {
        log.info("Getting customers with contracts expiring within {} days", daysThreshold);
        return ResponseEntity.ok(customerService.getCustomersWithExpiringContracts(daysThreshold));
    }

    @GetMapping("/overdue-payments")
    public ResponseEntity<List<CustomerDTO>> getCustomersWithOverduePayments() {
        log.info("Getting customers with overdue payments");
        return ResponseEntity.ok(customerService.getCustomersWithOverduePayments());
    }

    @GetMapping("/annual-turnover")
    public ResponseEntity<List<CustomerDTO>> getCustomersByAnnualTurnoverRange(
            @RequestParam BigDecimal min,
            @RequestParam BigDecimal max) {
        log.info("Getting customers by annual turnover range: {} - {}", min, max);
        return ResponseEntity.ok(customerService.getCustomersByAnnualTurnoverRange(min, max));
    }

    @GetMapping("/business-activity")
    public ResponseEntity<List<CustomerDTO>> getCustomersByBusinessActivity(
            @RequestParam String activity) {
        log.info("Getting customers by business activity: {}", activity);
        return ResponseEntity.ok(customerService.getCustomersByBusinessActivity(activity));
    }

    @PatchMapping("/{id}/credit-limit")
    public ResponseEntity<Void> updateCustomerCreditLimit(
            @PathVariable Long id,
            @RequestParam BigDecimal newLimit) {
        log.info("Updating credit limit for customer {} to {}", id, newLimit);
        customerService.updateCustomerCreditLimit(id, newLimit);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{id}/credit-score")
    public ResponseEntity<Void> updateCustomerCreditScore(
            @PathVariable Long id,
            @RequestParam Integer newScore) {
        log.info("Updating credit score for customer {} to {}", id, newScore);
        customerService.updateCustomerCreditScore(id, newScore);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{customerId}/groups/{groupId}")
    public ResponseEntity<Void> addCustomerToGroup(
            @PathVariable Long customerId,
            @PathVariable Long groupId) {
        log.info("Adding customer {} to group {}", customerId, groupId);
        customerService.addCustomerToGroup(customerId, groupId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{customerId}/groups/{groupId}")
    public ResponseEntity<Void> removeCustomerFromGroup(
            @PathVariable Long customerId,
            @PathVariable Long groupId) {
        log.info("Removing customer {} from group {}", customerId, groupId);
        customerService.removeCustomerFromGroup(customerId, groupId);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{id}/loyalty-points")
    public ResponseEntity<Void> updateCustomerLoyaltyPoints(
            @PathVariable Long id,
            @RequestParam Integer points) {
        log.info("Updating loyalty points for customer {} to {}", id, points);
        customerService.updateCustomerLoyaltyPoints(id, points);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{id}/vip-status")
    public ResponseEntity<Void> updateCustomerVipStatus(
            @PathVariable Long id,
            @RequestParam boolean isVip) {
        log.info("Updating VIP status for customer {} to {}", id, isVip);
        customerService.updateCustomerVipStatus(id, isVip);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}/validate-contract")
    public ResponseEntity<Boolean> validateCustomerContract(@PathVariable Long id) {
        log.info("Validating contract for customer {}", id);
        return ResponseEntity.ok(customerService.validateCustomerContract(id));
    }

    @GetMapping("/{id}/validate-credit")
    public ResponseEntity<Boolean> validateCustomerCredit(
            @PathVariable Long id,
            @RequestParam BigDecimal amount) {
        log.info("Validating credit for customer {} with amount {}", id, amount);
        return ResponseEntity.ok(customerService.validateCustomerCredit(id, amount));
    }

    @GetMapping("/{id}/active")
    public ResponseEntity<Boolean> isCustomerActive(@PathVariable Long id) {
        log.info("Checking if customer {} is active", id);
        return ResponseEntity.ok(customerService.isCustomerActive(id));
    }

    @GetMapping("/statistics")
    public ResponseEntity<Map<String, Object>> getCustomerStatistics() {
        log.info("Getting customer statistics");
        return ResponseEntity.ok(customerService.getCustomerStatistics());
    }

    @GetMapping("/top-spenders")
    public ResponseEntity<List<CustomerDTO>> getTopCustomersBySpending(
            @RequestParam(defaultValue = "10") int limit) {
        log.info("Getting top {} customers by spending", limit);
        return ResponseEntity.ok(customerService.getTopCustomersBySpending(limit));
    }

    @GetMapping("/product-preferences/{productId}")
    public ResponseEntity<List<CustomerDTO>> getCustomersByProductPreference(
            @PathVariable Long productId) {
        log.info("Getting customers by product preference: {}", productId);
        return ResponseEntity.ok(customerService.getCustomersByProductPreference(productId));
    }

    @GetMapping("/distribution-by-type")
    public ResponseEntity<Map<String, Integer>> getCustomerDistributionByType() {
        log.info("Getting customer distribution by type");
        return ResponseEntity.ok(customerService.getCustomerDistributionByType());
    }

    @GetMapping("/average-order-value-by-type")
    public ResponseEntity<Map<String, BigDecimal>> getAverageOrderValueByCustomerType() {
        log.info("Getting average order value by customer type");
        return ResponseEntity.ok(customerService.getAverageOrderValueByCustomerType());
    }
} 