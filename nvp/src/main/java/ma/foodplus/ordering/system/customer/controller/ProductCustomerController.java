package ma.foodplus.ordering.system.customer.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import ma.foodplus.ordering.system.customer.dto.ProductCustomerDTO;
import ma.foodplus.ordering.system.customer.dto.ProductCustomerPriceHistoryDTO;
import ma.foodplus.ordering.system.customer.dto.ProductCustomerStatisticsDTO;
import ma.foodplus.ordering.system.customer.service.ProductCustomerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/product-customers")
@RequiredArgsConstructor
@Tag(name = "Product Customer Management", description = "APIs for managing product-customer relationships")
public class ProductCustomerController {

    private final ProductCustomerService productCustomerService;

    // Basic CRUD operations
    @PostMapping
    @Operation(summary = "Create a new product-customer relationship")
    public ResponseEntity<ProductCustomerDTO> createProductCustomer(
            @Valid @RequestBody ProductCustomerDTO productCustomerDTO) {
        return new ResponseEntity<>(productCustomerService.createProductCustomer(productCustomerDTO), 
                HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an existing product-customer relationship")
    public ResponseEntity<ProductCustomerDTO> updateProductCustomer(
            @PathVariable Long id,
            @Valid @RequestBody ProductCustomerDTO productCustomerDTO) {
        return ResponseEntity.ok(productCustomerService.updateProductCustomer(id, productCustomerDTO));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a product-customer relationship")
    public ResponseEntity<Void> deleteProductCustomer(@PathVariable Long id) {
        productCustomerService.deleteProductCustomer(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a product-customer relationship by ID")
    public ResponseEntity<ProductCustomerDTO> getProductCustomerById(@PathVariable Long id) {
        return ResponseEntity.ok(productCustomerService.getProductCustomerById(id));
    }

    // Query operations
    @GetMapping("/customer/{customerId}")
    @Operation(summary = "Get all product-customer relationships for a customer")
    public ResponseEntity<List<ProductCustomerDTO>> getProductCustomersByCustomerId(
            @PathVariable Long customerId) {
        return ResponseEntity.ok(productCustomerService.getProductCustomersByCustomerId(customerId));
    }

    @GetMapping("/product/{productId}")
    @Operation(summary = "Get all product-customer relationships for a product")
    public ResponseEntity<List<ProductCustomerDTO>> getProductCustomersByProductId(
            @PathVariable Long productId) {
        return ResponseEntity.ok(productCustomerService.getProductCustomersByProductId(productId));
    }

    @GetMapping("/customer/{customerId}/product/{productId}")
    public ResponseEntity<ProductCustomerDTO> getProductCustomerByCustomerIdAndProductId(
            @PathVariable Long customerId,
            @PathVariable Long productId) {
        return ResponseEntity.ok(productCustomerService.getProductCustomerByCustomerIdAndProductId(customerId, productId));
    }

    @GetMapping("/customer/{customerId}/category/{category}")
    public ResponseEntity<List<ProductCustomerDTO>> getProductCustomersByCustomerIdAndCategory(
            @PathVariable Long customerId,
            @PathVariable String category) {
        return ResponseEntity.ok(productCustomerService.getProductCustomersByCustomerIdAndCategory(customerId, category));
    }

    @GetMapping("/customer/{customerId}/max-price/{maxPrice}")
    public ResponseEntity<List<ProductCustomerDTO>> getProductCustomersByCustomerIdAndMaxPrice(
            @PathVariable Long customerId,
            @PathVariable Double maxPrice) {
        return ResponseEntity.ok(productCustomerService.getProductCustomersByCustomerIdAndMaxPrice(customerId, maxPrice));
    }

    // Price and discount management
    @PatchMapping("/{id}/price")
    @Operation(summary = "Update the price of a product-customer relationship")
    public ResponseEntity<Void> updateProductCustomerPrice(
            @PathVariable Long id,
            @RequestParam Double newPrice) {
        productCustomerService.updateProductCustomerPrice(id, newPrice);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{id}/discount")
    @Operation(summary = "Update the discount of a product-customer relationship")
    public ResponseEntity<Void> updateProductCustomerDiscount(
            @PathVariable Long id,
            @RequestParam Double newDiscount) {
        productCustomerService.updateProductCustomerDiscount(id, newDiscount);
        return ResponseEntity.ok().build();
    }

    // New endpoints for price management
    @PostMapping("/{id}/apply-new-prices")
    @Operation(summary = "Apply pending price changes")
    public ResponseEntity<Void> applyNewPrices(@PathVariable Long id) {
        productCustomerService.applyNewPrices(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/revert-new-prices")
    @Operation(summary = "Revert pending price changes")
    public ResponseEntity<Void> revertNewPrices(@PathVariable Long id) {
        productCustomerService.revertNewPrices(id);
        return ResponseEntity.ok().build();
    }

    // Bulk operations
    @PatchMapping("/bulk/prices")
    @Operation(summary = "Update prices for multiple product-customer relationships")
    public ResponseEntity<List<ProductCustomerDTO>> updateBulkPrices(
            @RequestParam List<Long> ids,
            @RequestParam Double newPrice) {
        return ResponseEntity.ok(productCustomerService.updateBulkPrices(ids, newPrice));
    }

    @PatchMapping("/bulk/discounts")
    @Operation(summary = "Update discounts for multiple product-customer relationships")
    public ResponseEntity<List<ProductCustomerDTO>> updateBulkDiscounts(
            @RequestParam List<Long> ids,
            @RequestParam Double newDiscount) {
        return ResponseEntity.ok(productCustomerService.updateBulkDiscounts(ids, newDiscount));
    }

    @PutMapping("/bulk/deactivate")
    public ResponseEntity<Void> deactivateProductCustomers(@RequestParam List<Long> ids) {
        productCustomerService.deactivateProductCustomers(ids);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/bulk/activate")
    public ResponseEntity<Void> activateProductCustomers(@RequestParam List<Long> ids) {
        productCustomerService.activateProductCustomers(ids);
        return ResponseEntity.ok().build();
    }

    // Statistics and history
    @GetMapping("/{id}/price-history")
    @Operation(summary = "Get price history for a product-customer relationship")
    public ResponseEntity<List<ProductCustomerPriceHistoryDTO>> getPriceHistory(@PathVariable Long id) {
        return ResponseEntity.ok(productCustomerService.getPriceHistory(id));
    }

    @GetMapping("/customer/{customerId}/statistics")
    @Operation(summary = "Get statistics for a customer's product relationships")
    public ResponseEntity<ProductCustomerStatisticsDTO> getProductCustomerStatistics(
            @PathVariable Long customerId) {
        return ResponseEntity.ok(productCustomerService.getProductCustomerStatistics(customerId));
    }

    @GetMapping("/customer/{customerId}/average-prices")
    public ResponseEntity<Map<String, BigDecimal>> getAveragePricesByCategory(@PathVariable Long customerId) {
        return ResponseEntity.ok(productCustomerService.getAveragePricesByCategory(customerId));
    }

    @GetMapping("/customer/{customerId}/average-discounts")
    public ResponseEntity<Map<String, BigDecimal>> getAverageDiscountsByCategory(@PathVariable Long customerId) {
        return ResponseEntity.ok(productCustomerService.getAverageDiscountsByCategory(customerId));
    }

    // Search and filtering
    @GetMapping("/search")
    @Operation(summary = "Search product-customer relationships")
    public ResponseEntity<List<ProductCustomerDTO>> searchProductCustomers(
            @RequestParam String searchTerm) {
        return ResponseEntity.ok(productCustomerService.searchProductCustomers(searchTerm));
    }

    @GetMapping("/price-range")
    @Operation(summary = "Get product-customer relationships within a price range")
    public ResponseEntity<List<ProductCustomerDTO>> getProductCustomersByPriceRange(
            @RequestParam BigDecimal minPrice,
            @RequestParam BigDecimal maxPrice) {
        return ResponseEntity.ok(productCustomerService.getProductCustomersByPriceRange(minPrice, maxPrice));
    }

    @GetMapping("/discount-range")
    public ResponseEntity<List<ProductCustomerDTO>> getProductCustomersByDiscountRange(
            @RequestParam BigDecimal minDiscount,
            @RequestParam BigDecimal maxDiscount) {
        return ResponseEntity.ok(productCustomerService.getProductCustomersByDiscountRange(minDiscount, maxDiscount));
    }

    // Customer-specific operations
    @GetMapping("/customer/{customerId}/preferred")
    @Operation(summary = "Get customer's preferred products")
    public ResponseEntity<List<ProductCustomerDTO>> getCustomerPreferredProducts(
            @PathVariable Long customerId) {
        return ResponseEntity.ok(productCustomerService.getCustomerPreferredProducts(customerId));
    }

    @GetMapping("/customer/{customerId}/recently-updated")
    public ResponseEntity<List<ProductCustomerDTO>> getCustomerRecentlyUpdatedProducts(
            @PathVariable Long customerId) {
        return ResponseEntity.ok(productCustomerService.getCustomerRecentlyUpdatedProducts(customerId));
    }

    @GetMapping("/customer/{customerId}/prices")
    public ResponseEntity<Map<Long, BigDecimal>> getProductPricesForCustomer(@PathVariable Long customerId) {
        return ResponseEntity.ok(productCustomerService.getProductPricesForCustomer(customerId));
    }

    // Product-specific operations
    @GetMapping("/product/{productId}/active-discounts")
    @Operation(summary = "Get product-customer relationships with active discounts")
    public ResponseEntity<List<ProductCustomerDTO>> getProductCustomersWithActiveDiscounts(
            @PathVariable Long productId) {
        return ResponseEntity.ok(productCustomerService.getProductCustomersWithActiveDiscounts(productId));
    }

    @GetMapping("/product/{productId}/pending-price-changes")
    public ResponseEntity<List<ProductCustomerDTO>> getProductCustomersWithPendingPriceChanges(
            @PathVariable Long productId) {
        return ResponseEntity.ok(productCustomerService.getProductCustomersWithPendingPriceChanges(productId));
    }

    // Validation
    @GetMapping("/{id}/validate")
    @Operation(summary = "Validate a product-customer relationship")
    public ResponseEntity<Boolean> validateProductCustomer(@PathVariable Long id) {
        return ResponseEntity.ok(productCustomerService.validateProductCustomer(id));
    }

    @GetMapping("/{id}/validate-price")
    @Operation(summary = "Validate a price change")
    public ResponseEntity<Boolean> validatePriceChange(
            @PathVariable Long id,
            @RequestParam BigDecimal newPrice) {
        return ResponseEntity.ok(productCustomerService.isPriceChangeValid(id, newPrice));
    }

    @GetMapping("/{id}/validate-discount")
    @Operation(summary = "Validate a discount change")
    public ResponseEntity<Boolean> validateDiscountChange(
            @PathVariable Long id,
            @RequestParam BigDecimal newDiscount) {
        return ResponseEntity.ok(productCustomerService.isDiscountChangeValid(id, newDiscount));
    }
}