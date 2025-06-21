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
import ma.foodplus.ordering.system.partner.dto.B2BPartnerDTO;
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
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/v1/partners/b2b")
@RequiredArgsConstructor
@Validated
@Tag(name = "B2B Partner Management", description = "Specialized API for managing B2B partners and business relationships")
public class B2BPartnerController {

    private final PartnerService partnerService;

    // ========== B2B CRUD Operations ==========

    @PostMapping
    @Operation(summary = "Create a new B2B partner", description = "Creates a new B2B partner with company and contract information")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "B2B partner created successfully", content = @Content(schema = @Schema(implementation = PartnerDTO.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input data", content = @Content),
        @ApiResponse(responseCode = "409", description = "Partner with CT number or ICE already exists", content = @Content)
    })
    public ResponseEntity<PartnerDTO> createB2BPartner(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "B2B partner data to create", required = true, content = @Content(schema = @Schema(implementation = B2BPartnerDTO.class)))
            @Valid @RequestBody B2BPartnerDTO b2bPartnerDTO) {
        log.info("Creating new B2B partner with CT number: {}", b2bPartnerDTO.getCtNum());
        PartnerDTO createdPartner = partnerService.createB2BPartner(b2bPartnerDTO);
        return new ResponseEntity<>(createdPartner, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update B2B partner", description = "Updates an existing B2B partner's information")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "B2B partner updated successfully", content = @Content(schema = @Schema(implementation = PartnerDTO.class))),
        @ApiResponse(responseCode = "404", description = "B2B partner not found", content = @Content),
        @ApiResponse(responseCode = "400", description = "Invalid input data", content = @Content)
    })
    public ResponseEntity<PartnerDTO> updateB2BPartner(
            @Parameter(description = "Partner ID", required = true) @PathVariable Long id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Updated B2B partner data", required = true, content = @Content(schema = @Schema(implementation = B2BPartnerDTO.class)))
            @Valid @RequestBody B2BPartnerDTO b2bPartnerDTO) {
        log.info("Updating B2B partner with ID: {}", id);
        PartnerDTO updatedPartner = partnerService.updateB2BPartner(id, b2bPartnerDTO);
        return ResponseEntity.ok(updatedPartner);
    }

    @GetMapping
    @Operation(summary = "Get all B2B partners", description = "Retrieves all B2B partners with pagination")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "B2B partners retrieved successfully", content = @Content(schema = @Schema(implementation = PartnerDTO.class)))
    })
    public ResponseEntity<Page<PartnerDTO>> getAllB2BPartners(
            @Parameter(description = "Pagination parameters") Pageable pageable) {
        log.debug("Fetching B2B partners with pagination: page {}", pageable.getPageNumber());
        Page<PartnerDTO> partners = partnerService.getB2BPartners(pageable);
        return ResponseEntity.ok(partners);
    }

    @GetMapping("/all")
    @Operation(summary = "Get all B2B partners (no pagination)", description = "Retrieves all B2B partners without pagination")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "B2B partners retrieved successfully", content = @Content(schema = @Schema(implementation = PartnerDTO.class)))
    })
    public ResponseEntity<List<PartnerDTO>> getAllB2BPartnersList() {
        log.debug("Fetching all B2B partners");
        List<PartnerDTO> partners = partnerService.getAllB2BPartners();
        return ResponseEntity.ok(partners);
    }

    // ========== Contract Management ==========

    @GetMapping("/expiring-contracts")
    @Operation(summary = "Get B2B partners with expiring contracts", description = "Retrieves B2B partners with contracts expiring within specified days")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Partners with expiring contracts retrieved successfully", content = @Content(schema = @Schema(implementation = PartnerDTO.class)))
    })
    public ResponseEntity<List<PartnerDTO>> getPartnersWithExpiringContracts(
            @Parameter(description = "Days threshold for expiring contracts", required = true) @RequestParam int daysThreshold) {
        log.debug("Fetching B2B partners with contracts expiring in {} days", daysThreshold);
        List<PartnerDTO> partners = partnerService.getPartnersWithExpiringContracts(daysThreshold);
        return ResponseEntity.ok(partners);
    }

    @PostMapping("/{id}/renew-contract")
    @Operation(summary = "Renew B2B partner contract", description = "Renews a B2B partner's contract with a new end date")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Contract renewed successfully", content = @Content(schema = @Schema(implementation = PartnerDTO.class))),
        @ApiResponse(responseCode = "404", description = "B2B partner not found", content = @Content)
    })
    public ResponseEntity<PartnerDTO> renewContract(
            @Parameter(description = "Partner ID", required = true) @PathVariable Long id,
            @Parameter(description = "New contract end date", required = true) @RequestParam ZonedDateTime newEndDate) {
        log.info("Renewing contract for B2B partner ID: {} with new end date: {}", id, newEndDate);
        PartnerDTO partner = partnerService.renewContract(id, newEndDate);
        return ResponseEntity.ok(partner);
    }

    @PostMapping("/{id}/terminate-contract")
    @Operation(summary = "Terminate B2B partner contract", description = "Terminates a B2B partner's contract")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Contract terminated successfully", content = @Content(schema = @Schema(implementation = PartnerDTO.class))),
        @ApiResponse(responseCode = "404", description = "B2B partner not found", content = @Content)
    })
    public ResponseEntity<PartnerDTO> terminateContract(
            @Parameter(description = "Partner ID", required = true) @PathVariable Long id,
            @Parameter(description = "Termination date", required = true) @RequestParam ZonedDateTime terminationDate,
            @Parameter(description = "Termination reason", required = true) @RequestParam String reason) {
        log.info("Terminating contract for B2B partner ID: {} with reason: {}", id, reason);
        PartnerDTO partner = partnerService.terminateContract(id, terminationDate, reason);
        return ResponseEntity.ok(partner);
    }

    @GetMapping("/{id}/contract-status")
    @Operation(summary = "Get B2B partner contract status", description = "Retrieves detailed contract status for a B2B partner")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Contract status retrieved successfully"),
        @ApiResponse(responseCode = "404", description = "B2B partner not found", content = @Content)
    })
    public ResponseEntity<Map<String, Object>> getContractStatus(
            @Parameter(description = "Partner ID", required = true) @PathVariable Long id) {
        log.debug("Getting contract status for B2B partner ID: {}", id);
        Map<String, Object> status = partnerService.getContractStatus(id);
        return ResponseEntity.ok(status);
    }

    // ========== Business Analytics ==========

    @GetMapping("/by-annual-turnover")
    @Operation(summary = "Get B2B partners by annual turnover range", description = "Retrieves B2B partners within specified annual turnover range")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "B2B partners retrieved successfully", content = @Content(schema = @Schema(implementation = PartnerDTO.class)))
    })
    public ResponseEntity<List<PartnerDTO>> getPartnersByAnnualTurnoverRange(
            @Parameter(description = "Minimum annual turnover", required = true) @RequestParam BigDecimal min,
            @Parameter(description = "Maximum annual turnover", required = true) @RequestParam BigDecimal max) {
        log.debug("Fetching B2B partners by annual turnover range: {} - {}", min, max);
        List<PartnerDTO> partners = partnerService.getPartnerByAnnualTurnoverRange(min, max);
        return ResponseEntity.ok(partners);
    }

    @GetMapping("/by-business-activity")
    @Operation(summary = "Get B2B partners by business activity", description = "Retrieves B2B partners filtered by business activity")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "B2B partners retrieved successfully", content = @Content(schema = @Schema(implementation = PartnerDTO.class)))
    })
    public ResponseEntity<List<PartnerDTO>> getPartnersByBusinessActivity(
            @Parameter(description = "Business activity to filter by", required = true) @RequestParam String activity) {
        log.debug("Fetching B2B partners by business activity: {}", activity);
        List<PartnerDTO> partners = partnerService.getPartnerByBusinessActivity(activity);
        return ResponseEntity.ok(partners);
    }

    @GetMapping("/overdue-payments")
    @Operation(summary = "Get B2B partners with overdue payments", description = "Retrieves B2B partners with outstanding payments")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "B2B partners with overdue payments retrieved successfully", content = @Content(schema = @Schema(implementation = PartnerDTO.class)))
    })
    public ResponseEntity<List<PartnerDTO>> getPartnersWithOverduePayments() {
        log.debug("Fetching B2B partners with overdue payments");
        List<PartnerDTO> partners = partnerService.getPartnerWithOverduePayments();
        return ResponseEntity.ok(partners);
    }

    // ========== Credit Management ==========

    @PostMapping("/{id}/process-payment")
    @Operation(summary = "Process payment for B2B partner", description = "Processes a payment for a B2B partner")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Payment processed successfully", content = @Content(schema = @Schema(implementation = PartnerDTO.class))),
        @ApiResponse(responseCode = "404", description = "B2B partner not found", content = @Content)
    })
    public ResponseEntity<PartnerDTO> processPayment(
            @Parameter(description = "Partner ID", required = true) @PathVariable Long id,
            @Parameter(description = "Payment amount", required = true) @RequestParam BigDecimal amount,
            @Parameter(description = "Payment method", required = true) @RequestParam String paymentMethod) {
        log.info("Processing payment for B2B partner ID: {} amount: {} method: {}", id, amount, paymentMethod);
        PartnerDTO partner = partnerService.processPayment(id, amount, paymentMethod);
        return ResponseEntity.ok(partner);
    }

    @GetMapping("/{id}/credit-summary")
    @Operation(summary = "Get B2B partner credit summary", description = "Retrieves comprehensive credit summary for a B2B partner")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Credit summary retrieved successfully"),
        @ApiResponse(responseCode = "404", description = "B2B partner not found", content = @Content)
    })
    public ResponseEntity<Map<String, Object>> getCreditSummary(
            @Parameter(description = "Partner ID", required = true) @PathVariable Long id) {
        log.debug("Getting credit summary for B2B partner ID: {}", id);
        Map<String, Object> summary = partnerService.getCreditSummary(id);
        return ResponseEntity.ok(summary);
    }

    // ========== Validation ==========

    @PostMapping("/{id}/validate-order")
    @Operation(summary = "Validate B2B partner order placement", description = "Validates if a B2B partner can place an order")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Order validation completed successfully"),
        @ApiResponse(responseCode = "404", description = "B2B partner not found", content = @Content)
    })
    public ResponseEntity<Map<String, Object>> validateOrderPlacement(
            @Parameter(description = "Partner ID", required = true) @PathVariable Long id,
            @Parameter(description = "Order amount", required = true) @RequestParam BigDecimal orderAmount) {
        log.debug("Validating order placement for B2B partner ID: {} amount: {}", id, orderAmount);
        Map<String, Object> validation = partnerService.validateOrderPlacement(id, orderAmount);
        return ResponseEntity.ok(validation);
    }

    @GetMapping("/{id}/validation-status")
    @Operation(summary = "Get B2B partner validation status", description = "Retrieves comprehensive validation status for a B2B partner")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Validation status retrieved successfully"),
        @ApiResponse(responseCode = "404", description = "B2B partner not found", content = @Content)
    })
    public ResponseEntity<Map<String, Object>> getValidationStatus(
            @Parameter(description = "Partner ID", required = true) @PathVariable Long id) {
        log.debug("Getting validation status for B2B partner ID: {}", id);
        Map<String, Object> status = partnerService.getValidationStatus(id);
        return ResponseEntity.ok(status);
    }

    // ========== Common Operations ==========

    @GetMapping("/{id}")
    @Operation(summary = "Get B2B partner by ID", description = "Retrieves a B2B partner by their unique identifier")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "B2B partner found", content = @Content(schema = @Schema(implementation = PartnerDTO.class))),
        @ApiResponse(responseCode = "404", description = "B2B partner not found", content = @Content)
    })
    public ResponseEntity<PartnerDTO> getB2BPartnerById(
            @Parameter(description = "Partner ID", required = true) @PathVariable Long id) {
        log.debug("Fetching B2B partner with ID: {}", id);
        PartnerDTO partner = partnerService.getPartnerById(id);
        return ResponseEntity.ok(partner);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete B2B partner", description = "Deletes a B2B partner (soft delete - sets active to false)")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "B2B partner deleted successfully"),
        @ApiResponse(responseCode = "404", description = "B2B partner not found", content = @Content)
    })
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteB2BPartner(
            @Parameter(description = "Partner ID", required = true) @PathVariable Long id) {
        log.info("Deleting B2B partner with ID: {}", id);
        partnerService.deletePartner(id);
    }

    @PostMapping("/{id}/activate")
    @Operation(summary = "Activate B2B partner", description = "Activates a B2B partner (sets active to true)")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "B2B partner activated successfully"),
        @ApiResponse(responseCode = "404", description = "B2B partner not found", content = @Content)
    })
    public ResponseEntity<Void> activateB2BPartner(
            @Parameter(description = "Partner ID", required = true) @PathVariable Long id) {
        log.info("Activating B2B partner with ID: {}", id);
        partnerService.activatePartner(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/deactivate")
    @Operation(summary = "Deactivate B2B partner", description = "Deactivates a B2B partner (sets active to false)")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "B2B partner deactivated successfully"),
        @ApiResponse(responseCode = "404", description = "B2B partner not found", content = @Content)
    })
    public ResponseEntity<Void> deactivateB2BPartner(
            @Parameter(description = "Partner ID", required = true) @PathVariable Long id) {
        log.info("Deactivating B2B partner with ID: {}", id);
        partnerService.deactivatePartner(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/loyalty-points")
    @Operation(summary = "Update B2B partner loyalty points", description = "Updates a B2B partner's loyalty points")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Loyalty points updated successfully", content = @Content(schema = @Schema(implementation = PartnerDTO.class))),
        @ApiResponse(responseCode = "404", description = "B2B partner not found", content = @Content)
    })
    public ResponseEntity<PartnerDTO> updateB2BLoyaltyPoints(
            @Parameter(description = "Partner ID", required = true) @PathVariable Long id,
            @Parameter(description = "Points to add/remove", required = true) @RequestParam int points) {
        log.info("Updating loyalty points for B2B partner ID: {} by {}", id, points);
        PartnerDTO partner = partnerService.updateLoyaltyPoints(id, points);
        return ResponseEntity.ok(partner);
    }

    @GetMapping("/{id}/loyalty-level")
    @Operation(summary = "Get B2B partner loyalty level", description = "Gets a B2B partner's loyalty level (0-5)")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Loyalty level retrieved successfully"),
        @ApiResponse(responseCode = "404", description = "B2B partner not found", content = @Content)
    })
    public ResponseEntity<Integer> getB2BLoyaltyLevel(
            @Parameter(description = "Partner ID", required = true) @PathVariable Long id) {
        log.debug("Getting loyalty level for B2B partner ID: {}", id);
        int level = partnerService.getPartnerLoyaltyLevel(id);
        return ResponseEntity.ok(level);
    }

    @PostMapping("/{id}/credit-limit")
    @Operation(summary = "Update B2B partner credit limit", description = "Updates a B2B partner's credit limit")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Credit limit updated successfully"),
        @ApiResponse(responseCode = "404", description = "B2B partner not found", content = @Content)
    })
    public ResponseEntity<Void> updateB2BCreditLimit(
            @Parameter(description = "Partner ID", required = true) @PathVariable Long id,
            @Parameter(description = "New credit limit", required = true) @RequestParam BigDecimal newLimit) {
        log.info("Updating credit limit for B2B partner ID: {} to {}", id, newLimit);
        partnerService.updatePartnerCreditLimit(id, newLimit);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}/total-spent")
    @Operation(summary = "Get B2B partner total spent", description = "Gets a B2B partner's total spent amount")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Total spent retrieved successfully"),
        @ApiResponse(responseCode = "404", description = "B2B partner not found", content = @Content)
    })
    public ResponseEntity<BigDecimal> getB2BTotalSpent(
            @Parameter(description = "Partner ID", required = true) @PathVariable Long id) {
        log.debug("Getting total spent for B2B partner ID: {}", id);
        BigDecimal totalSpent = partnerService.getPartnerTotalSpent(id);
        return ResponseEntity.ok(totalSpent);
    }
} 