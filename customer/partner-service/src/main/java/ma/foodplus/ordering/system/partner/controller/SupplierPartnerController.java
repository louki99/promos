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
import ma.foodplus.ordering.system.partner.dto.SupplierPartnerDTO;
import ma.foodplus.ordering.system.partner.dto.PartnerDTO;
import ma.foodplus.ordering.system.partner.service.PartnerService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;

/**
 * REST Controller for Supplier Partner management.
 * 
 * <p>This controller provides comprehensive endpoints for managing supplier partners,
 * including CRUD operations, performance monitoring, risk assessment, and audit management.</p>
 * 
 * @author FoodPlus Development Team
 * @version 3.0.0
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/supplier-partners")
@RequiredArgsConstructor
@Validated
@Tag(name = "Supplier Partner Management", description = "APIs for managing supplier partners")
public class SupplierPartnerController {
    
    private final PartnerService partnerService;
    
    // ========== CRUD Operations ==========
    
    @PostMapping
    @Operation(summary = "Create supplier partner", description = "Creates a new supplier partner")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Supplier partner created successfully", 
                    content = @Content(schema = @Schema(implementation = PartnerDTO.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input data", content = @Content),
        @ApiResponse(responseCode = "409", description = "Supplier code already exists", content = @Content)
    })
    @PreAuthorize("hasRole('SUPPLIER_MANAGER') or hasRole('PARTNER_MANAGER') or hasRole('ADMIN')")
    public ResponseEntity<PartnerDTO> createSupplierPartner(
            @Parameter(description = "Supplier partner data", required = true)
            @Valid @RequestBody SupplierPartnerDTO supplierPartnerDTO) {
        log.info("Creating supplier partner with code: {}", supplierPartnerDTO.getSupplierCode());
        PartnerDTO createdPartner = partnerService.createSupplierPartner(supplierPartnerDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdPartner);
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "Get supplier partner by ID", description = "Retrieves a supplier partner by their ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Supplier partner found", 
                    content = @Content(schema = @Schema(implementation = PartnerDTO.class))),
        @ApiResponse(responseCode = "404", description = "Supplier partner not found", content = @Content)
    })
    @PreAuthorize("hasRole('SUPPLIER_MANAGER') or hasRole('PARTNER_MANAGER') or hasRole('ADMIN')")
    public ResponseEntity<PartnerDTO> getSupplierPartner(
            @Parameter(description = "Supplier partner ID", required = true)
            @PathVariable Long id) {
        log.info("Retrieving supplier partner with ID: {}", id);
        PartnerDTO partner = partnerService.getSupplierPartnerById(id);
        return ResponseEntity.ok(partner);
    }
    
    @GetMapping
    @Operation(summary = "Get all supplier partners", description = "Retrieves all supplier partners with pagination")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Supplier partners retrieved successfully", 
                    content = @Content(schema = @Schema(implementation = Page.class)))
    })
    @PreAuthorize("hasRole('SUPPLIER_MANAGER') or hasRole('PARTNER_MANAGER') or hasRole('ADMIN')")
    public ResponseEntity<Page<PartnerDTO>> getAllSupplierPartners(
            @PageableDefault(size = 20) Pageable pageable) {
        log.info("Retrieving all supplier partners with pagination");
        Page<PartnerDTO> partners = partnerService.getSupplierPartners(pageable);
        return ResponseEntity.ok(partners);
    }
    
    @PutMapping("/{id}")
    @Operation(summary = "Update supplier partner", description = "Updates an existing supplier partner")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Supplier partner updated successfully", 
                    content = @Content(schema = @Schema(implementation = PartnerDTO.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input data", content = @Content),
        @ApiResponse(responseCode = "404", description = "Supplier partner not found", content = @Content)
    })
    @PreAuthorize("hasRole('SUPPLIER_MANAGER') or hasRole('PARTNER_MANAGER') or hasRole('ADMIN')")
    public ResponseEntity<PartnerDTO> updateSupplierPartner(
            @Parameter(description = "Supplier partner ID", required = true)
            @PathVariable Long id,
            @Parameter(description = "Updated supplier partner data", required = true)
            @Valid @RequestBody SupplierPartnerDTO supplierPartnerDTO) {
        log.info("Updating supplier partner with ID: {}", id);
        PartnerDTO updatedPartner = partnerService.updateSupplierPartner(id, supplierPartnerDTO);
        return ResponseEntity.ok(updatedPartner);
    }
    
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete supplier partner", description = "Soft deletes a supplier partner")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Supplier partner deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Supplier partner not found", content = @Content)
    })
    @PreAuthorize("hasRole('SUPPLIER_MANAGER') or hasRole('ADMIN')")
    public ResponseEntity<Void> deleteSupplierPartner(
            @Parameter(description = "Supplier partner ID", required = true)
            @PathVariable Long id) {
        log.info("Deleting supplier partner with ID: {}", id);
        partnerService.deletePartner(id);
        return ResponseEntity.noContent().build();
    }
    
    // ========== Search and Filter Operations ==========
    
    @GetMapping("/search")
    @Operation(summary = "Search supplier partners", description = "Searches supplier partners by various criteria")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Search completed successfully", 
                    content = @Content(schema = @Schema(implementation = Page.class)))
    })
    @PreAuthorize("hasRole('SUPPLIER_MANAGER') or hasRole('PARTNER_MANAGER') or hasRole('ADMIN')")
    public ResponseEntity<Page<PartnerDTO>> searchSupplierPartners(
            @Parameter(description = "Search term")
            @RequestParam(required = false) String searchTerm,
            @Parameter(description = "Supplier category filter")
            @RequestParam(required = false) String category,
            @Parameter(description = "Supplier status filter")
            @RequestParam(required = false) String status,
            @Parameter(description = "Risk level filter")
            @RequestParam(required = false) String riskLevel,
            @Parameter(description = "Supplier rating filter")
            @RequestParam(required = false) String rating,
            @PageableDefault(size = 20) Pageable pageable) {
        log.info("Searching supplier partners with criteria: term={}, category={}, status={}, riskLevel={}, rating={}", 
                searchTerm, category, status, riskLevel, rating);
        Page<PartnerDTO> partners = partnerService.searchSupplierPartners(searchTerm, category, status, riskLevel, rating, pageable);
        return ResponseEntity.ok(partners);
    }
    
    @GetMapping("/by-category/{category}")
    @Operation(summary = "Get suppliers by category", description = "Retrieves suppliers by category")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Suppliers retrieved successfully", 
                    content = @Content(schema = @Schema(implementation = List.class)))
    })
    @PreAuthorize("hasRole('SUPPLIER_MANAGER') or hasRole('PARTNER_MANAGER') or hasRole('ADMIN')")
    public ResponseEntity<List<PartnerDTO>> getSuppliersByCategory(
            @Parameter(description = "Supplier category", required = true)
            @PathVariable String category) {
        log.info("Retrieving suppliers by category: {}", category);
        List<PartnerDTO> partners = partnerService.getSuppliersByCategory(category);
        return ResponseEntity.ok(partners);
    }
    
    @GetMapping("/by-status/{status}")
    @Operation(summary = "Get suppliers by status", description = "Retrieves suppliers by status")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Suppliers retrieved successfully", 
                    content = @Content(schema = @Schema(implementation = List.class)))
    })
    @PreAuthorize("hasRole('SUPPLIER_MANAGER') or hasRole('PARTNER_MANAGER') or hasRole('ADMIN')")
    public ResponseEntity<List<PartnerDTO>> getSuppliersByStatus(
            @Parameter(description = "Supplier status", required = true)
            @PathVariable String status) {
        log.info("Retrieving suppliers by status: {}", status);
        List<PartnerDTO> partners = partnerService.getSuppliersByStatus(status);
        return ResponseEntity.ok(partners);
    }
    
    // ========== Performance Management ==========
    
    @PutMapping("/{id}/performance-scores")
    @Operation(summary = "Update performance scores", description = "Updates supplier performance scores")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Performance scores updated successfully", 
                    content = @Content(schema = @Schema(implementation = PartnerDTO.class))),
        @ApiResponse(responseCode = "400", description = "Invalid performance scores", content = @Content),
        @ApiResponse(responseCode = "404", description = "Supplier partner not found", content = @Content)
    })
    @PreAuthorize("hasRole('SUPPLIER_MANAGER') or hasRole('ADMIN')")
    public ResponseEntity<PartnerDTO> updatePerformanceScores(
            @Parameter(description = "Supplier partner ID", required = true)
            @PathVariable Long id,
            @Parameter(description = "Delivery performance score (0-100)")
            @RequestParam(required = false) @DecimalMin("0.0") @DecimalMax("100.0") BigDecimal deliveryScore,
            @Parameter(description = "Quality score (0-100)")
            @RequestParam(required = false) @DecimalMin("0.0") @DecimalMax("100.0") BigDecimal qualityScore,
            @Parameter(description = "Price competitiveness score (0-100)")
            @RequestParam(required = false) @DecimalMin("0.0") @DecimalMax("100.0") BigDecimal priceScore) {
        log.info("Updating performance scores for supplier ID: {} - delivery: {}, quality: {}, price: {}", 
                id, deliveryScore, qualityScore, priceScore);
        PartnerDTO updatedPartner = partnerService.updateSupplierPerformanceScores(id, deliveryScore, qualityScore, priceScore);
        return ResponseEntity.ok(updatedPartner);
    }
    
    @GetMapping("/{id}/performance")
    @Operation(summary = "Get supplier performance", description = "Retrieves supplier performance metrics")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Performance metrics retrieved successfully", 
                    content = @Content(schema = @Schema(implementation = Map.class))),
        @ApiResponse(responseCode = "404", description = "Supplier partner not found", content = @Content)
    })
    @PreAuthorize("hasRole('SUPPLIER_MANAGER') or hasRole('PARTNER_MANAGER') or hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> getSupplierPerformance(
            @Parameter(description = "Supplier partner ID", required = true)
            @PathVariable Long id) {
        log.info("Retrieving performance metrics for supplier ID: {}", id);
        Map<String, Object> performance = partnerService.getSupplierPerformance(id);
        return ResponseEntity.ok(performance);
    }
    
    // ========== Risk Management ==========
    
    @PutMapping("/{id}/risk-assessment")
    @Operation(summary = "Update risk assessment", description = "Updates supplier risk assessment")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Risk assessment updated successfully", 
                    content = @Content(schema = @Schema(implementation = PartnerDTO.class))),
        @ApiResponse(responseCode = "400", description = "Invalid risk level", content = @Content),
        @ApiResponse(responseCode = "404", description = "Supplier partner not found", content = @Content)
    })
    @PreAuthorize("hasRole('SUPPLIER_MANAGER') or hasRole('ADMIN')")
    public ResponseEntity<PartnerDTO> updateRiskAssessment(
            @Parameter(description = "Supplier partner ID", required = true)
            @PathVariable Long id,
            @Parameter(description = "Risk level", required = true)
            @RequestParam @Pattern(regexp = "^(LOW|MEDIUM|HIGH|CRITICAL)$") String riskLevel,
            @Parameter(description = "Risk assessment notes")
            @RequestParam(required = false) String notes) {
        log.info("Updating risk assessment for supplier ID: {} - risk level: {}", id, riskLevel);
        PartnerDTO updatedPartner = partnerService.updateSupplierRiskAssessment(id, riskLevel, notes);
        return ResponseEntity.ok(updatedPartner);
    }
    
    @GetMapping("/risk-assessment")
    @Operation(summary = "Get suppliers by risk level", description = "Retrieves suppliers grouped by risk level")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Risk assessment retrieved successfully", 
                    content = @Content(schema = @Schema(implementation = Map.class)))
    })
    @PreAuthorize("hasRole('SUPPLIER_MANAGER') or hasRole('PARTNER_MANAGER') or hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> getRiskAssessment() {
        log.info("Retrieving supplier risk assessment");
        Map<String, Object> riskAssessment = partnerService.getSupplierRiskAssessment();
        return ResponseEntity.ok(riskAssessment);
    }
    
    // ========== Audit Management ==========
    
    @PutMapping("/{id}/audit-schedule")
    @Operation(summary = "Schedule supplier audit", description = "Schedules or updates supplier audit")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Audit scheduled successfully", 
                    content = @Content(schema = @Schema(implementation = PartnerDTO.class))),
        @ApiResponse(responseCode = "400", description = "Invalid audit date", content = @Content),
        @ApiResponse(responseCode = "404", description = "Supplier partner not found", content = @Content)
    })
    @PreAuthorize("hasRole('SUPPLIER_MANAGER') or hasRole('ADMIN')")
    public ResponseEntity<PartnerDTO> scheduleAudit(
            @Parameter(description = "Supplier partner ID", required = true)
            @PathVariable Long id,
            @Parameter(description = "Next audit date", required = true)
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) ZonedDateTime nextAuditDate,
            @Parameter(description = "Audit notes")
            @RequestParam(required = false) String notes) {
        log.info("Scheduling audit for supplier ID: {} - next audit date: {}", id, nextAuditDate);
        PartnerDTO updatedPartner = partnerService.scheduleSupplierAudit(id, nextAuditDate, notes);
        return ResponseEntity.ok(updatedPartner);
    }
    
    @PutMapping("/{id}/audit-complete")
    @Operation(summary = "Complete supplier audit", description = "Marks supplier audit as completed")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Audit completed successfully", 
                    content = @Content(schema = @Schema(implementation = PartnerDTO.class))),
        @ApiResponse(responseCode = "404", description = "Supplier partner not found", content = @Content)
    })
    @PreAuthorize("hasRole('SUPPLIER_MANAGER') or hasRole('ADMIN')")
    public ResponseEntity<PartnerDTO> completeAudit(
            @Parameter(description = "Supplier partner ID", required = true)
            @PathVariable Long id,
            @Parameter(description = "Audit completion date")
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) ZonedDateTime auditDate,
            @Parameter(description = "Audit results")
            @RequestParam(required = false) String results) {
        log.info("Completing audit for supplier ID: {} - audit date: {}", id, auditDate);
        PartnerDTO updatedPartner = partnerService.completeSupplierAudit(id, auditDate, results);
        return ResponseEntity.ok(updatedPartner);
    }
    
    @GetMapping("/audit-overdue")
    @Operation(summary = "Get overdue audits", description = "Retrieves suppliers with overdue audits")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Overdue audits retrieved successfully", 
                    content = @Content(schema = @Schema(implementation = List.class)))
    })
    @PreAuthorize("hasRole('SUPPLIER_MANAGER') or hasRole('PARTNER_MANAGER') or hasRole('ADMIN')")
    public ResponseEntity<List<PartnerDTO>> getOverdueAudits() {
        log.info("Retrieving suppliers with overdue audits");
        List<PartnerDTO> partners = partnerService.getSuppliersWithOverdueAudits();
        return ResponseEntity.ok(partners);
    }
    
    @GetMapping("/audit-due-soon")
    @Operation(summary = "Get audits due soon", description = "Retrieves suppliers with audits due within 30 days")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Audits due soon retrieved successfully", 
                    content = @Content(schema = @Schema(implementation = List.class)))
    })
    @PreAuthorize("hasRole('SUPPLIER_MANAGER') or hasRole('PARTNER_MANAGER') or hasRole('ADMIN')")
    public ResponseEntity<List<PartnerDTO>> getAuditsDueSoon(
            @Parameter(description = "Days threshold (default: 30)")
            @RequestParam(defaultValue = "30") @Min(1) @Max(365) int daysThreshold) {
        log.info("Retrieving suppliers with audits due within {} days", daysThreshold);
        List<PartnerDTO> partners = partnerService.getSuppliersWithAuditsDueSoon(daysThreshold);
        return ResponseEntity.ok(partners);
    }
    
    // ========== Status Management ==========
    
    @PutMapping("/{id}/status")
    @Operation(summary = "Update supplier status", description = "Updates supplier status")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Status updated successfully", 
                    content = @Content(schema = @Schema(implementation = PartnerDTO.class))),
        @ApiResponse(responseCode = "400", description = "Invalid status", content = @Content),
        @ApiResponse(responseCode = "404", description = "Supplier partner not found", content = @Content)
    })
    @PreAuthorize("hasRole('SUPPLIER_MANAGER') or hasRole('ADMIN')")
    public ResponseEntity<PartnerDTO> updateStatus(
            @Parameter(description = "Supplier partner ID", required = true)
            @PathVariable Long id,
            @Parameter(description = "New status", required = true)
            @RequestParam @Pattern(regexp = "^(ACTIVE|SUSPENDED|BLACKLISTED|PENDING_APPROVAL)$") String status,
            @Parameter(description = "Status change reason")
            @RequestParam(required = false) String reason) {
        log.info("Updating status for supplier ID: {} - new status: {} - reason: {}", id, status, reason);
        PartnerDTO updatedPartner = partnerService.updateSupplierStatus(id, status, reason);
        return ResponseEntity.ok(updatedPartner);
    }
    
    // ========== Analytics and Reporting ==========
    
    @GetMapping("/analytics/performance")
    @Operation(summary = "Get performance analytics", description = "Retrieves supplier performance analytics")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Performance analytics retrieved successfully", 
                    content = @Content(schema = @Schema(implementation = Map.class)))
    })
    @PreAuthorize("hasRole('SUPPLIER_MANAGER') or hasRole('ANALYST') or hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> getPerformanceAnalytics() {
        log.info("Retrieving supplier performance analytics");
        Map<String, Object> analytics = partnerService.getSupplierPerformanceAnalytics();
        return ResponseEntity.ok(analytics);
    }
    
    @GetMapping("/analytics/risk")
    @Operation(summary = "Get risk analytics", description = "Retrieves supplier risk analytics")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Risk analytics retrieved successfully", 
                    content = @Content(schema = @Schema(implementation = Map.class)))
    })
    @PreAuthorize("hasRole('SUPPLIER_MANAGER') or hasRole('ANALYST') or hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> getRiskAnalytics() {
        log.info("Retrieving supplier risk analytics");
        Map<String, Object> analytics = partnerService.getSupplierRiskAnalytics();
        return ResponseEntity.ok(analytics);
    }
    
    @GetMapping("/analytics/category-distribution")
    @Operation(summary = "Get category distribution", description = "Retrieves supplier distribution by category")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Category distribution retrieved successfully", 
                    content = @Content(schema = @Schema(implementation = Map.class)))
    })
    @PreAuthorize("hasRole('SUPPLIER_MANAGER') or hasRole('ANALYST') or hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> getCategoryDistribution() {
        log.info("Retrieving supplier category distribution");
        Map<String, Object> distribution = partnerService.getSupplierCategoryDistribution();
        return ResponseEntity.ok(distribution);
    }
} 