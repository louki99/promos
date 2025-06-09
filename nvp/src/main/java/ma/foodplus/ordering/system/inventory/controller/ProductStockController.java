package ma.foodplus.ordering.system.inventory.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import ma.foodplus.ordering.system.inventory.dto.request.*;
import ma.foodplus.ordering.system.inventory.dto.response.*;
import ma.foodplus.ordering.system.inventory.model.ProductStock.QualityStatus;
import ma.foodplus.ordering.system.inventory.service.ProductStockService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/inventory/product-stocks")
@RequiredArgsConstructor
@Tag(name = "Product Stock Management", description = "APIs for managing product stock inventory")
public class ProductStockController extends InventoryBaseController {

    private final ProductStockService productStockService;

    @PostMapping
    @Operation(summary = "Create a new product stock entry")
    public ResponseEntity<ProductStockResponse> createProductStock(
            @Valid @RequestBody ProductStockRequest request) {
        return new ResponseEntity<>(productStockService.createProductStock(request), HttpStatus.CREATED);
    }

    @PostMapping("/bulk")
    @Operation(summary = "Perform bulk operations on product stocks")
    public ResponseEntity<BulkProductStockResponse> bulkOperation(
            @Valid @RequestBody BulkProductStockRequest request) {
        return ResponseEntity.ok(productStockService.processBulkOperation(request));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an existing product stock entry")
    public ResponseEntity<ProductStockResponse> updateProductStock(
            @PathVariable Long id,
            @Valid @RequestBody ProductStockRequest request) {
        return ResponseEntity.ok(productStockService.updateProductStock(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a product stock entry")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteProductStock(@PathVariable Long id) {
        productStockService.deleteProductStock(id);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a product stock entry by ID")
    public ResponseEntity<ProductStockResponse> getProductStockById(@PathVariable Long id) {
        return ResponseEntity.ok(productStockService.getProductStockById(id));
    }

    @GetMapping
    @Operation(summary = "Get all product stock entries")
    public ResponseEntity<List<ProductStockResponse>> getAllProductStocks() {
        return ResponseEntity.ok(productStockService.getAllProductStocks());
    }

    @GetMapping("/product/{productId}")
    @Operation(summary = "Get all stock entries for a specific product")
    public ResponseEntity<List<ProductStockResponse>> getProductStocksByProductId(@PathVariable Long productId) {
        return ResponseEntity.ok(productStockService.getProductStocksByProductId(productId));
    }

    @GetMapping("/depot/{depotId}")
    @Operation(summary = "Get all stock entries for a specific depot")
    public ResponseEntity<List<ProductStockResponse>> getProductStocksByDepotId(@PathVariable Long depotId) {
        return ResponseEntity.ok(productStockService.getProductStocksByDepotId(depotId));
    }

    @GetMapping("/low-stock")
    @Operation(summary = "Get all products with low stock levels")
    public ResponseEntity<List<ProductStockResponse>> getLowStockProducts() {
        return ResponseEntity.ok(productStockService.getLowStockProducts());
    }

    @GetMapping("/expired")
    @Operation(summary = "Get all expired products")
    public ResponseEntity<List<ProductStockResponse>> getExpiredProducts() {
        return ResponseEntity.ok(productStockService.getExpiredProducts());
    }

    @GetMapping("/expiring-before")
    @Operation(summary = "Get products expiring before a specific date")
    public ResponseEntity<List<ProductStockResponse>> getProductsExpiringBefore(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return ResponseEntity.ok(productStockService.getProductsExpiringBefore(date));
    }

    @GetMapping("/quality-status/{status}")
    @Operation(summary = "Get products by quality status")
    public ResponseEntity<List<ProductStockResponse>> getProductsByQualityStatus(
            @PathVariable QualityStatus status) {
        return ResponseEntity.ok(productStockService.getProductsByQualityStatus(status));
    }

    @GetMapping("/quarantine")
    @Operation(summary = "Get all quarantined products")
    public ResponseEntity<List<ProductStockResponse>> getProductsInQuarantine() {
        return ResponseEntity.ok(productStockService.getProductsInQuarantine());
    }

    @GetMapping("/inspection-required")
    @Operation(summary = "Get all products requiring inspection")
    public ResponseEntity<List<ProductStockResponse>> getProductsRequiringInspection() {
        return ResponseEntity.ok(productStockService.getProductsRequiringInspection());
    }

    @GetMapping("/damaged")
    @Operation(summary = "Get all damaged products")
    public ResponseEntity<List<ProductStockResponse>> getDamagedProducts() {
        return ResponseEntity.ok(productStockService.getDamagedProducts());
    }

    @GetMapping("/recalled")
    @Operation(summary = "Get all recalled products")
    public ResponseEntity<List<ProductStockResponse>> getRecalledProducts() {
        return ResponseEntity.ok(productStockService.getRecalledProducts());
    }

    @PutMapping("/{id}/quality-status")
    @Operation(summary = "Update product quality status")
    public ResponseEntity<ProductStockResponse> updateQualityStatus(
            @PathVariable Long id,
            @RequestParam QualityStatus newStatus) {
        return ResponseEntity.ok(productStockService.updateQualityStatus(id, newStatus));
    }

    @PutMapping("/{id}/quarantine")
    @Operation(summary = "Quarantine a product")
    public ResponseEntity<ProductStockResponse> quarantineProduct(
            @PathVariable Long id,
            @RequestParam String reason) {
        return ResponseEntity.ok(productStockService.quarantineProduct(id, reason));
    }

    @PutMapping("/{id}/release-quarantine")
    @Operation(summary = "Release a product from quarantine")
    public ResponseEntity<ProductStockResponse> releaseFromQuarantine(@PathVariable Long id) {
        return ResponseEntity.ok(productStockService.releaseFromQuarantine(id));
    }

    @PutMapping("/{id}/mark-inspection")
    @Operation(summary = "Mark a product for inspection")
    public ResponseEntity<ProductStockResponse> markForInspection(@PathVariable Long id) {
        return ResponseEntity.ok(productStockService.markForInspection(id));
    }

    @PutMapping("/{id}/mark-damaged")
    @Operation(summary = "Mark a product as damaged")
    public ResponseEntity<ProductStockResponse> markAsDamaged(
            @PathVariable Long id,
            @RequestParam String damageDescription) {
        return ResponseEntity.ok(productStockService.markAsDamaged(id, damageDescription));
    }

    @PutMapping("/{id}/recall")
    @Operation(summary = "Recall a product")
    public ResponseEntity<ProductStockResponse> recallProduct(
            @PathVariable Long id,
            @RequestParam String recallReason) {
        return ResponseEntity.ok(productStockService.recallProduct(id, recallReason));
    }

    @GetMapping("/stats/quality-status/{status}/count")
    @Operation(summary = "Count products by quality status")
    public ResponseEntity<Long> countProductsByQualityStatus(@PathVariable QualityStatus status) {
        return ResponseEntity.ok(productStockService.countProductsByQualityStatus(status));
    }

    @GetMapping("/stats/low-stock/count")
    @Operation(summary = "Count low stock products")
    public ResponseEntity<Long> countLowStockProducts() {
        return ResponseEntity.ok(productStockService.countLowStockProducts());
    }

    @GetMapping("/stats/expired/count")
    @Operation(summary = "Count expired products")
    public ResponseEntity<Long> countExpiredProducts() {
        return ResponseEntity.ok(productStockService.countExpiredProducts());
    }

    @PutMapping("/{id}/quantity")
    @Operation(summary = "Update stock quantity")
    public ResponseEntity<ProductStockResponse> updateStockQuantity(
            @PathVariable Long id,
            @RequestParam BigDecimal newQuantity) {
        return ResponseEntity.ok(productStockService.updateStockQuantity(id, newQuantity));
    }

    @PutMapping("/{id}/unit-cost")
    @Operation(summary = "Update unit cost")
    public ResponseEntity<ProductStockResponse> updateUnitCost(
            @PathVariable Long id,
            @RequestParam BigDecimal newUnitCost) {
        return ResponseEntity.ok(productStockService.updateUnitCost(id, newUnitCost));
    }

    @PutMapping("/{id}/expiry-date")
    @Operation(summary = "Update expiry date")
    public ResponseEntity<ProductStockResponse> updateExpiryDate(
            @PathVariable Long id,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate newExpiryDate) {
        return ResponseEntity.ok(productStockService.updateExpiryDate(id, newExpiryDate));
    }

    @PutMapping("/{id}/reserve")
    @Operation(summary = "Reserve stock quantity")
    public ResponseEntity<Void> reserveStock(
            @PathVariable Long id,
            @RequestParam Double quantity) {
        productStockService.reserveStock(id, quantity);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}/release-reserved")
    @Operation(summary = "Release reserved stock quantity")
    public ResponseEntity<Void> releaseReservedStock(
            @PathVariable Long id,
            @RequestParam Double quantity) {
        productStockService.releaseReservedStock(id, quantity);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/search")
    @Operation(summary = "Search product stocks")
    public ResponseEntity<List<ProductStockResponse>> searchProductStocks(
            @RequestParam String searchTerm) {
        return ResponseEntity.ok(productStockService.searchProductStocks(searchTerm));
    }

    @GetMapping("/stats/summary")
    @Operation(summary = "Get inventory statistics summary")
    public ResponseEntity<InventorySummaryResponse> getInventorySummary() {
        return ResponseEntity.ok(productStockService.getInventorySummary());
    }

    @GetMapping("/movement-history/{id}")
    @Operation(summary = "Get stock movement history")
    public ResponseEntity<List<StockMovementResponse>> getStockMovementHistory(
            @PathVariable Long id,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return ResponseEntity.ok(productStockService.getStockMovementHistory(id, startDate, endDate));
    }

    @PostMapping("/{id}/transfer")
    @Operation(summary = "Transfer stock between depots")
    public ResponseEntity<StockTransferResponse> transferStock(
            @PathVariable Long id,
            @Valid @RequestBody StockTransferRequest request) {
        return ResponseEntity.ok(productStockService.transferStock(id, request));
    }

    @GetMapping("/alerts")
    @Operation(summary = "Get inventory alerts")
    public ResponseEntity<List<InventoryAlertResponse>> getInventoryAlerts(
            @RequestParam(required = false) InventoryAlertResponse.AlertType alertType) {
        return ResponseEntity.ok(productStockService.getInventoryAlerts(alertType));
    }
}