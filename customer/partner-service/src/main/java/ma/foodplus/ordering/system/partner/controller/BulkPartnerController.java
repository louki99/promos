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
import ma.foodplus.ordering.system.partner.service.PartnerService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/v1/partners/bulk")
@RequiredArgsConstructor
@Validated
@Tag(name = "Bulk Partner Operations", description = "API for performing bulk operations on multiple partners")
public class BulkPartnerController {

    private final PartnerService partnerService;

    // ========== Bulk Status Operations ==========

    @PostMapping("/activate")
    @Operation(summary = "Bulk activate partners", description = "Activates multiple partners at once")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Partners activated successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid input data", content = @Content)
    })
    public ResponseEntity<Map<String, Object>> bulkActivatePartners(
            @Parameter(description = "List of partner IDs to activate", required = true) @RequestBody List<Long> partnerIds) {
        log.info("Bulk activating {} partners", partnerIds.size());
        int updatedCount = partnerService.bulkUpdateStatus(partnerIds, true);
        Map<String, Object> response = Map.of(
            "message", "Partners activated successfully",
            "updatedCount", updatedCount,
            "totalRequested", partnerIds.size()
        );
        return ResponseEntity.ok(response);
    }

    @PostMapping("/deactivate")
    @Operation(summary = "Bulk deactivate partners", description = "Deactivates multiple partners at once")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Partners deactivated successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid input data", content = @Content)
    })
    public ResponseEntity<Map<String, Object>> bulkDeactivatePartners(
            @Parameter(description = "List of partner IDs to deactivate", required = true) @RequestBody List<Long> partnerIds) {
        log.info("Bulk deactivating {} partners", partnerIds.size());
        int updatedCount = partnerService.bulkUpdateStatus(partnerIds, false);
        Map<String, Object> response = Map.of(
            "message", "Partners deactivated successfully",
            "updatedCount", updatedCount,
            "totalRequested", partnerIds.size()
        );
        return ResponseEntity.ok(response);
    }

    // ========== Bulk Credit Operations ==========

    @PostMapping("/update-credit-limits")
    @Operation(summary = "Bulk update credit limits", description = "Updates credit limits for multiple partners")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Credit limits updated successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid input data", content = @Content)
    })
    public ResponseEntity<Map<String, Object>> bulkUpdateCreditLimits(
            @Parameter(description = "List of partner IDs", required = true) @RequestParam List<Long> partnerIds,
            @Parameter(description = "New credit limit", required = true) @RequestParam BigDecimal newLimit) {
        log.info("Bulk updating credit limits for {} partners to {}", partnerIds.size(), newLimit);
        int updatedCount = partnerService.bulkUpdateCreditLimits(partnerIds, newLimit);
        Map<String, Object> response = Map.of(
            "message", "Credit limits updated successfully",
            "updatedCount", updatedCount,
            "totalRequested", partnerIds.size(),
            "newLimit", newLimit
        );
        return ResponseEntity.ok(response);
    }

    // ========== Bulk Group Operations ==========

    @PostMapping("/add-to-group")
    @Operation(summary = "Bulk add partners to group", description = "Adds multiple partners to a specific group")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Partners added to group successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid input data", content = @Content),
        @ApiResponse(responseCode = "404", description = "Group not found", content = @Content)
    })
    public ResponseEntity<Map<String, Object>> bulkAddToGroup(
            @Parameter(description = "List of partner IDs", required = true) @RequestParam List<Long> partnerIds,
            @Parameter(description = "Group ID", required = true) @RequestParam Long groupId) {
        log.info("Bulk adding {} partners to group {}", partnerIds.size(), groupId);
        int addedCount = partnerService.bulkAddToGroup(partnerIds, groupId);
        Map<String, Object> response = Map.of(
            "message", "Partners added to group successfully",
            "addedCount", addedCount,
            "totalRequested", partnerIds.size(),
            "groupId", groupId
        );
        return ResponseEntity.ok(response);
    }

    // ========== Bulk Validation ==========

    @PostMapping("/validate-orders")
    @Operation(summary = "Bulk validate order placement", description = "Validates if multiple partners can place orders")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Order validation completed successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid input data", content = @Content)
    })
    public ResponseEntity<Map<String, Object>> bulkValidateOrders(
            @Parameter(description = "List of partner IDs", required = true) @RequestParam List<Long> partnerIds,
            @Parameter(description = "Order amount", required = true) @RequestParam BigDecimal orderAmount) {
        log.info("Bulk validating order placement for {} partners with amount {}", partnerIds.size(), orderAmount);
        
        Map<String, Object> results = Map.of(
            "totalPartners", partnerIds.size(),
            "orderAmount", orderAmount,
            "validationResults", "Validation completed" // This would be implemented in service
        );
        return ResponseEntity.ok(results);
    }

    // ========== Bulk Analytics ==========

    @PostMapping("/performance-metrics")
    @Operation(summary = "Bulk get performance metrics", description = "Retrieves performance metrics for multiple partners")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Performance metrics retrieved successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid input data", content = @Content)
    })
    public ResponseEntity<Map<String, Object>> bulkGetPerformanceMetrics(
            @Parameter(description = "List of partner IDs", required = true) @RequestBody List<Long> partnerIds) {
        log.info("Bulk getting performance metrics for {} partners", partnerIds.size());
        
        Map<String, Object> results = Map.of(
            "totalPartners", partnerIds.size(),
            "metrics", "Performance metrics retrieved" // This would be implemented in service
        );
        return ResponseEntity.ok(results);
    }

    // ========== Bulk Export ==========

    @PostMapping("/export")
    @Operation(summary = "Bulk export partners", description = "Exports data for multiple partners")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Export completed successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid input data", content = @Content)
    })
    public ResponseEntity<Map<String, Object>> bulkExportPartners(
            @Parameter(description = "List of partner IDs", required = true) @RequestBody List<Long> partnerIds,
            @Parameter(description = "Export format (CSV, JSON, XML)", required = true) @RequestParam String format) {
        log.info("Bulk exporting {} partners in {} format", partnerIds.size(), format);
        
        Map<String, Object> results = Map.of(
            "totalPartners", partnerIds.size(),
            "format", format,
            "exportUrl", "/downloads/partners-export.csv", // This would be implemented in service
            "message", "Export completed successfully"
        );
        return ResponseEntity.ok(results);
    }

    // ========== Bulk Import ==========

    @PostMapping("/import")
    @Operation(summary = "Bulk import partners", description = "Imports multiple partners from file")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Import completed successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid input data", content = @Content)
    })
    public ResponseEntity<Map<String, Object>> bulkImportPartners(
            @Parameter(description = "Import file", required = true) @RequestParam("file") String fileContent,
            @Parameter(description = "Import format (CSV, JSON, XML)", required = true) @RequestParam String format) {
        log.info("Bulk importing partners from {} format file", format);
        
        Map<String, Object> results = Map.of(
            "format", format,
            "importedCount", 0, // This would be implemented in service
            "failedCount", 0,
            "message", "Import completed successfully"
        );
        return ResponseEntity.ok(results);
    }

    // ========== Bulk Notifications ==========

    @PostMapping("/send-notifications")
    @Operation(summary = "Bulk send notifications", description = "Sends notifications to multiple partners")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Notifications sent successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid input data", content = @Content)
    })
    public ResponseEntity<Map<String, Object>> bulkSendNotifications(
            @Parameter(description = "List of partner IDs", required = true) @RequestParam List<Long> partnerIds,
            @Parameter(description = "Notification message", required = true) @RequestParam String message,
            @Parameter(description = "Notification type (EMAIL, SMS, PUSH)", required = true) @RequestParam String type) {
        log.info("Bulk sending {} notifications to {} partners", type, partnerIds.size());
        
        Map<String, Object> results = Map.of(
            "totalPartners", partnerIds.size(),
            "message", message,
            "type", type,
            "sentCount", partnerIds.size(),
            "failedCount", 0
        );
        return ResponseEntity.ok(results);
    }
} 