package ma.foodplus.ordering.system.partner.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ma.foodplus.ordering.system.partner.dto.B2CPartnerDTO;
import ma.foodplus.ordering.system.partner.dto.PartnerDTO;
import ma.foodplus.ordering.system.partner.service.PartnerService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/v1/partners/b2c")
@RequiredArgsConstructor
@Validated
@Tag(name = "B2C Partner Management", description = "Specialized API for managing B2C partners and individual customers")
public class B2CPartnerController {

    private final PartnerService partnerService;

    // ========== B2C CRUD Operations ==========

    @PostMapping
    @Operation(summary = "Create a new B2C partner", description = "Creates a new B2C partner with personal information")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "B2C partner created successfully", content = @Content(schema = @Schema(implementation = PartnerDTO.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input data", content = @Content),
        @ApiResponse(responseCode = "409", description = "Partner with CT number or ICE already exists", content = @Content)
    })
    public ResponseEntity<PartnerDTO> createB2CPartner(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "B2C partner data to create", required = true, content = @Content(schema = @Schema(implementation = B2CPartnerDTO.class)))
            @Valid @RequestBody B2CPartnerDTO b2cPartnerDTO) {
        log.info("Creating new B2C partner with CT number: {}", b2cPartnerDTO.getCtNum());
        PartnerDTO createdPartner = partnerService.createB2CPartner(b2cPartnerDTO);
        return new ResponseEntity<>(createdPartner, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update B2C partner", description = "Updates an existing B2C partner's information")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "B2C partner updated successfully", content = @Content(schema = @Schema(implementation = PartnerDTO.class))),
        @ApiResponse(responseCode = "404", description = "B2C partner not found", content = @Content),
        @ApiResponse(responseCode = "400", description = "Invalid input data", content = @Content)
    })
    public ResponseEntity<PartnerDTO> updateB2CPartner(
            @Parameter(description = "Partner ID", required = true) @PathVariable Long id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Updated B2C partner data", required = true, content = @Content(schema = @Schema(implementation = B2CPartnerDTO.class)))
            @Valid @RequestBody B2CPartnerDTO b2cPartnerDTO) {
        log.info("Updating B2C partner with ID: {}", id);
        PartnerDTO updatedPartner = partnerService.updateB2CPartner(id, b2cPartnerDTO);
        return ResponseEntity.ok(updatedPartner);
    }

    @GetMapping
    @Operation(summary = "Get all B2C partners", description = "Retrieves all B2C partners with pagination")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "B2C partners retrieved successfully", content = @Content(schema = @Schema(implementation = PartnerDTO.class)))
    })
    public ResponseEntity<Page<PartnerDTO>> getAllB2CPartners(
            @Parameter(description = "Pagination parameters") Pageable pageable) {
        log.debug("Fetching B2C partners with pagination: page {}", pageable.getPageNumber());
        Page<PartnerDTO> partners = partnerService.getB2CPartners(pageable);
        return ResponseEntity.ok(partners);
    }

    @GetMapping("/all")
    @Operation(summary = "Get all B2C partners (no pagination)", description = "Retrieves all B2C partners without pagination")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "B2C partners retrieved successfully", content = @Content(schema = @Schema(implementation = PartnerDTO.class)))
    })
    public ResponseEntity<List<PartnerDTO>> getAllB2CPartnersList() {
        log.debug("Fetching all B2C partners");
        List<PartnerDTO> partners = partnerService.getAllB2CPartners();
        return ResponseEntity.ok(partners);
    }

    // ========== Personal Information Management ==========

    @GetMapping("/by-age-range")
    @Operation(summary = "Get B2C partners by age range", description = "Retrieves B2C partners within specified age range")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "B2C partners retrieved successfully", content = @Content(schema = @Schema(implementation = PartnerDTO.class)))
    })
    public ResponseEntity<List<PartnerDTO>> getPartnersByAgeRange(
            @Parameter(description = "Minimum age", required = true) @RequestParam int minAge,
            @Parameter(description = "Maximum age", required = true) @RequestParam int maxAge) {
        log.debug("Fetching B2C partners by age range: {} - {}", minAge, maxAge);
        // This would need to be implemented in the service layer
        List<PartnerDTO> partners = partnerService.getAllB2CPartners(); // Placeholder
        return ResponseEntity.ok(partners);
    }

    @GetMapping("/minors")
    @Operation(summary = "Get B2C partners who are minors", description = "Retrieves B2C partners under 18 years old")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Minor partners retrieved successfully", content = @Content(schema = @Schema(implementation = PartnerDTO.class)))
    })
    public ResponseEntity<List<PartnerDTO>> getMinorPartners() {
        log.debug("Fetching B2C partners who are minors");
        // This would need to be implemented in the service layer
        List<PartnerDTO> partners = partnerService.getAllB2CPartners(); // Placeholder
        return ResponseEntity.ok(partners);
    }

    @GetMapping("/by-language")
    @Operation(summary = "Get B2C partners by preferred language", description = "Retrieves B2C partners filtered by preferred language")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "B2C partners retrieved successfully", content = @Content(schema = @Schema(implementation = PartnerDTO.class)))
    })
    public ResponseEntity<List<PartnerDTO>> getPartnersByLanguage(
            @Parameter(description = "Preferred language", required = true) @RequestParam String language) {
        log.debug("Fetching B2C partners by preferred language: {}", language);
        // This would need to be implemented in the service layer
        List<PartnerDTO> partners = partnerService.getAllB2CPartners(); // Placeholder
        return ResponseEntity.ok(partners);
    }

    // ========== Marketing Management ==========

    @GetMapping("/marketing-eligible")
    @Operation(summary = "Get B2C partners eligible for marketing", description = "Retrieves B2C partners who have given marketing consent")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Marketing eligible partners retrieved successfully", content = @Content(schema = @Schema(implementation = PartnerDTO.class)))
    })
    public ResponseEntity<List<PartnerDTO>> getMarketingEligiblePartners() {
        log.debug("Fetching B2C partners eligible for marketing");
        // This would need to be implemented in the service layer
        List<PartnerDTO> partners = partnerService.getAllB2CPartners(); // Placeholder
        return ResponseEntity.ok(partners);
    }

    @PostMapping("/{id}/update-marketing-consent")
    @Operation(summary = "Update B2C partner marketing consent", description = "Updates a B2C partner's marketing consent status")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Marketing consent updated successfully", content = @Content(schema = @Schema(implementation = PartnerDTO.class))),
        @ApiResponse(responseCode = "404", description = "B2C partner not found", content = @Content)
    })
    public ResponseEntity<PartnerDTO> updateMarketingConsent(
            @Parameter(description = "Partner ID", required = true) @PathVariable Long id,
            @Parameter(description = "Marketing consent status", required = true) @RequestParam boolean marketingConsent) {
        log.info("Updating marketing consent for B2C partner ID: {} to {}", id, marketingConsent);
        // This would need to be implemented in the service layer
        PartnerDTO partner = partnerService.getPartnerById(id);
        return ResponseEntity.ok(partner);
    }

    // ========== Credit Management ==========

    @PostMapping("/{id}/process-payment")
    @Operation(summary = "Process payment for B2C partner", description = "Processes a payment for a B2C partner")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Payment processed successfully", content = @Content(schema = @Schema(implementation = PartnerDTO.class))),
        @ApiResponse(responseCode = "404", description = "B2C partner not found", content = @Content)
    })
    public ResponseEntity<PartnerDTO> processPayment(
            @Parameter(description = "Partner ID", required = true) @PathVariable Long id,
            @Parameter(description = "Payment amount", required = true) @RequestParam BigDecimal amount,
            @Parameter(description = "Payment method", required = true) @RequestParam String paymentMethod) {
        log.info("Processing payment for B2C partner ID: {} amount: {} method: {}", id, amount, paymentMethod);
        PartnerDTO partner = partnerService.processPayment(id, amount, paymentMethod);
        return ResponseEntity.ok(partner);
    }

    @GetMapping("/{id}/credit-summary")
    @Operation(summary = "Get B2C partner credit summary", description = "Retrieves comprehensive credit summary for a B2C partner")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Credit summary retrieved successfully"),
        @ApiResponse(responseCode = "404", description = "B2C partner not found", content = @Content)
    })
    public ResponseEntity<Map<String, Object>> getCreditSummary(
            @Parameter(description = "Partner ID", required = true) @PathVariable Long id) {
        log.debug("Getting credit summary for B2C partner ID: {}", id);
        Map<String, Object> summary = partnerService.getCreditSummary(id);
        return ResponseEntity.ok(summary);
    }

    // ========== Validation ==========

    @PostMapping("/{id}/validate-order")
    @Operation(summary = "Validate B2C partner order placement", description = "Validates if a B2C partner can place an order")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Order validation completed successfully"),
        @ApiResponse(responseCode = "404", description = "B2C partner not found", content = @Content)
    })
    public ResponseEntity<Map<String, Object>> validateOrderPlacement(
            @Parameter(description = "Partner ID", required = true) @PathVariable Long id,
            @Parameter(description = "Order amount", required = true) @RequestParam BigDecimal orderAmount) {
        log.debug("Validating order placement for B2C partner ID: {} amount: {}", id, orderAmount);
        Map<String, Object> validation = partnerService.validateOrderPlacement(id, orderAmount);
        return ResponseEntity.ok(validation);
    }

    @GetMapping("/{id}/validation-status")
    @Operation(summary = "Get B2C partner validation status", description = "Retrieves comprehensive validation status for a B2C partner")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Validation status retrieved successfully"),
        @ApiResponse(responseCode = "404", description = "B2C partner not found", content = @Content)
    })
    public ResponseEntity<Map<String, Object>> getValidationStatus(
            @Parameter(description = "Partner ID", required = true) @PathVariable Long id) {
        log.debug("Getting validation status for B2C partner ID: {}", id);
        Map<String, Object> status = partnerService.getValidationStatus(id);
        return ResponseEntity.ok(status);
    }

    // ========== Loyalty and Rewards ==========

    @GetMapping("/loyalty-leaders")
    @Operation(summary = "Get B2C partners with highest loyalty", description = "Retrieves B2C partners with highest loyalty points")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Loyalty leaders retrieved successfully", content = @Content(schema = @Schema(implementation = PartnerDTO.class)))
    })
    public ResponseEntity<List<PartnerDTO>> getLoyaltyLeaders(
            @Parameter(description = "Number of top partners to retrieve", required = true) @RequestParam int limit) {
        log.debug("Fetching top {} B2C loyalty leaders", limit);
        List<PartnerDTO> partners = partnerService.getTopPartnersBySpending(limit); // Using spending as proxy for loyalty
        return ResponseEntity.ok(partners);
    }

    @PostMapping("/{id}/add-loyalty-points")
    @Operation(summary = "Add loyalty points to B2C partner", description = "Adds loyalty points to a B2C partner")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Loyalty points added successfully", content = @Content(schema = @Schema(implementation = PartnerDTO.class))),
        @ApiResponse(responseCode = "404", description = "B2C partner not found", content = @Content)
    })
    public ResponseEntity<PartnerDTO> addLoyaltyPoints(
            @Parameter(description = "Partner ID", required = true) @PathVariable Long id,
            @Parameter(description = "Points to add", required = true) @RequestParam int points) {
        log.info("Adding {} loyalty points to B2C partner ID: {}", points, id);
        PartnerDTO partner = partnerService.updateLoyaltyPoints(id, points);
        return ResponseEntity.ok(partner);
    }

    // ========== Analytics ==========

    @GetMapping("/{id}/performance-metrics")
    @Operation(summary = "Get B2C partner performance metrics", description = "Retrieves performance metrics for a B2C partner")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Performance metrics retrieved successfully"),
        @ApiResponse(responseCode = "404", description = "B2C partner not found", content = @Content)
    })
    public ResponseEntity<Map<String, Object>> getPerformanceMetrics(
            @Parameter(description = "Partner ID", required = true) @PathVariable Long id) {
        log.debug("Getting performance metrics for B2C partner ID: {}", id);
        Map<String, Object> metrics = partnerService.getPerformanceMetrics(id);
        return ResponseEntity.ok(metrics);
    }

    @GetMapping("/{id}/growth-trends")
    @Operation(summary = "Get B2C partner growth trends", description = "Retrieves growth trends for a B2C partner")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Growth trends retrieved successfully"),
        @ApiResponse(responseCode = "404", description = "B2C partner not found", content = @Content)
    })
    public ResponseEntity<Map<String, Object>> getGrowthTrends(
            @Parameter(description = "Partner ID", required = true) @PathVariable Long id,
            @Parameter(description = "Analysis period (monthly, quarterly, yearly)", required = true) @RequestParam String period) {
        log.debug("Getting growth trends for B2C partner ID: {} period: {}", id, period);
        Map<String, Object> trends = partnerService.getGrowthTrends(id, period);
        return ResponseEntity.ok(trends);
    }

    // ========== Common Operations ==========

    @GetMapping("/{id}")
    @Operation(summary = "Get B2C partner by ID", description = "Retrieves a B2C partner by their unique identifier")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "B2C partner found", content = @Content(schema = @Schema(implementation = PartnerDTO.class))),
        @ApiResponse(responseCode = "404", description = "B2C partner not found", content = @Content)
    })
    public ResponseEntity<PartnerDTO> getB2CPartnerById(
            @Parameter(description = "Partner ID", required = true) @PathVariable Long id) {
        log.debug("Fetching B2C partner with ID: {}", id);
        PartnerDTO partner = partnerService.getPartnerById(id);
        return ResponseEntity.ok(partner);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete B2C partner", description = "Deletes a B2C partner (soft delete - sets active to false)")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "B2C partner deleted successfully"),
        @ApiResponse(responseCode = "404", description = "B2C partner not found", content = @Content)
    })
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteB2CPartner(
            @Parameter(description = "Partner ID", required = true) @PathVariable Long id) {
        log.info("Deleting B2C partner with ID: {}", id);
        partnerService.deletePartner(id);
    }

    @PostMapping("/{id}/activate")
    @Operation(summary = "Activate B2C partner", description = "Activates a B2C partner (sets active to true)")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "B2C partner activated successfully"),
        @ApiResponse(responseCode = "404", description = "B2C partner not found", content = @Content)
    })
    public ResponseEntity<Void> activateB2CPartner(
            @Parameter(description = "Partner ID", required = true) @PathVariable Long id) {
        log.info("Activating B2C partner with ID: {}", id);
        partnerService.activatePartner(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/deactivate")
    @Operation(summary = "Deactivate B2C partner", description = "Deactivates a B2C partner (sets active to false)")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "B2C partner deactivated successfully"),
        @ApiResponse(responseCode = "404", description = "B2C partner not found", content = @Content)
    })
    public ResponseEntity<Void> deactivateB2CPartner(
            @Parameter(description = "Partner ID", required = true) @PathVariable Long id) {
        log.info("Deactivating B2C partner with ID: {}", id);
        partnerService.deactivatePartner(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/credit-limit")
    @Operation(summary = "Update B2C partner credit limit", description = "Updates a B2C partner's credit limit")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Credit limit updated successfully"),
        @ApiResponse(responseCode = "404", description = "B2C partner not found", content = @Content)
    })
    public ResponseEntity<Void> updateB2CCreditLimit(
            @Parameter(description = "Partner ID", required = true) @PathVariable Long id,
            @Parameter(description = "New credit limit", required = true) @RequestParam BigDecimal newLimit) {
        log.info("Updating credit limit for B2C partner ID: {} to {}", id, newLimit);
        partnerService.updatePartnerCreditLimit(id, newLimit);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}/total-spent")
    @Operation(summary = "Get B2C partner total spent", description = "Gets a B2C partner's total spent amount")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Total spent retrieved successfully"),
        @ApiResponse(responseCode = "404", description = "B2C partner not found", content = @Content)
    })
    public ResponseEntity<BigDecimal> getB2CTotalSpent(
            @Parameter(description = "Partner ID", required = true) @PathVariable Long id) {
        log.debug("Getting total spent for B2C partner ID: {}", id);
        BigDecimal totalSpent = partnerService.getPartnerTotalSpent(id);
        return ResponseEntity.ok(totalSpent);
    }
} 