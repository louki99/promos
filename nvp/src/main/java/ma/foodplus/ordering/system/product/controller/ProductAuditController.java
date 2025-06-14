package ma.foodplus.ordering.system.product.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import ma.foodplus.ordering.system.product.model.ProductAudit;
import ma.foodplus.ordering.system.product.service.ProductAuditService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.ZonedDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/products/audit")
@RequiredArgsConstructor
@Tag(name = "Product Audit", description = "APIs for tracking product changes and audit history")
public class ProductAuditController {
    
    private final ProductAuditService auditService;

    @GetMapping("/{productId}")
    @Operation(summary = "Get audit history for a product")
    public ResponseEntity<List<ProductAudit>> getProductAuditHistory(@PathVariable Long productId) {
        return ResponseEntity.ok(auditService.findByProductId(productId));
    }

    @GetMapping("/{productId}/paginated")
    @Operation(summary = "Get paginated audit history for a product")
    public ResponseEntity<Page<ProductAudit>> getProductAuditHistoryPaginated(
            @PathVariable Long productId,
            Pageable pageable) {
        return ResponseEntity.ok(auditService.findByProductId(productId, pageable));
    }

    @GetMapping("/user/{userId}")
    @Operation(summary = "Get audit history by user")
    public ResponseEntity<List<ProductAudit>> getUserAuditHistory(@PathVariable String userId) {
        return ResponseEntity.ok(auditService.findByUserId(userId));
    }

    @GetMapping("/user/{userId}/paginated")
    @Operation(summary = "Get paginated audit history by user")
    public ResponseEntity<Page<ProductAudit>> getUserAuditHistoryPaginated(
            @PathVariable String userId,
            Pageable pageable) {
        return ResponseEntity.ok(auditService.findByUserId(userId, pageable));
    }

    @GetMapping("/action/{action}")
    @Operation(summary = "Get audit history by action type")
    public ResponseEntity<List<ProductAudit>> getAuditHistoryByAction(
            @PathVariable ProductAuditService.AuditAction action,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) ZonedDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) ZonedDateTime endDate) {
        return ResponseEntity.ok(auditService.findByActionAndTimestampBetween(action, startDate, endDate));
    }

    @GetMapping("/{productId}/action/{action}")
    @Operation(summary = "Get product audit history by action type")
    public ResponseEntity<List<ProductAudit>> getProductAuditHistoryByAction(
            @PathVariable Long productId,
            @PathVariable ProductAuditService.AuditAction action) {
        return ResponseEntity.ok(auditService.findByProductIdAndAction(productId, action));
    }

    @GetMapping("/{productId}/date-range")
    @Operation(summary = "Get product audit history within a date range")
    public ResponseEntity<List<ProductAudit>> getProductAuditHistoryByDateRange(
            @PathVariable Long productId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) ZonedDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) ZonedDateTime endDate) {
        return ResponseEntity.ok(auditService.findByProductIdAndTimestampBetween(productId, startDate, endDate));
    }
} 