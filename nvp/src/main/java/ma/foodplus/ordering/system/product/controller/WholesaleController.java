package ma.foodplus.ordering.system.product.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import ma.foodplus.ordering.system.domain.valueobject.ProductId;
import ma.foodplus.ordering.system.product.dto.BulkOrderRequestDto;
import ma.foodplus.ordering.system.product.dto.WholesalePriceRequestDto;
import ma.foodplus.ordering.system.product.dto.WholesalePriceResponseDto;
import ma.foodplus.ordering.system.product.dto.response.ProductResponse;
import ma.foodplus.ordering.system.product.mapper.ProductResponseMapper;
import ma.foodplus.ordering.system.product.model.Product;
import ma.foodplus.ordering.system.product.service.ProductService;
import ma.foodplus.ordering.system.product.service.WholesaleOrderValidationService;
import ma.foodplus.ordering.system.product.service.WholesalePriceCalculationService;
import ma.foodplus.ordering.system.inventory.service.BulkInventoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/v1/wholesale")
@RequiredArgsConstructor
public class WholesaleController {

    private final ProductService productService;
    private final ProductResponseMapper productResponseMapper;
    private final WholesalePriceCalculationService wholesalePriceCalculationService;
    private final WholesaleOrderValidationService wholesaleOrderValidationService;
    private final BulkInventoryService bulkInventoryService;

    /**
     * Calculate wholesale price for a product
     */
    @PostMapping("/{productId}/calculate-price")
    public ResponseEntity<BigDecimal> calculateWholesalePrice(
            @PathVariable Long productId,
            @RequestParam Integer quantity) {
        Product product = productResponseMapper.toProduct(productService.getProduct(new ProductId(productId)));
        return ResponseEntity.ok(wholesalePriceCalculationService.calculatePrice(product, quantity));
    }

    /**
     * Validate a bulk order request
     */
    @PostMapping("/{productId}/validate-order")
    public ResponseEntity<Boolean> validateBulkOrder(
            @PathVariable Long productId,
            @RequestParam Integer quantity) {
        Product product = productResponseMapper.toProduct(productService.getProduct(new ProductId(productId)));
        return ResponseEntity.ok(wholesaleOrderValidationService.validateOrder(product, quantity));
    }

    /**
     * Check bulk order availability
     */
    @GetMapping("/{productId}/check-availability")
    public ResponseEntity<Boolean> checkBulkAvailability(
            @PathVariable Long productId,
            @RequestParam Integer quantity) {
        Product product = productResponseMapper.toProduct(productService.getProduct(new ProductId(productId)));
        return ResponseEntity.ok(bulkInventoryService.checkAvailability(product, quantity));
    }

    /**
     * Get bulk order recommendations
     */
    @GetMapping("/{productId}/recommendations")
    public ResponseEntity<List<Integer>> getBulkRecommendations(
            @PathVariable Long productId) {
        Product product = productResponseMapper.toProduct(productService.getProduct(new ProductId(productId)));
        return ResponseEntity.ok(bulkInventoryService.getRecommendedQuantities(product));
    }

    /**
     * Reserve stock for a bulk order
     */
    @PostMapping("/{productId}/reserve-stock")
    public ResponseEntity<Void> reserveBulkStock(
            @PathVariable Long productId,
            @RequestParam Integer quantity) {
        Product product = productResponseMapper.toProduct(productService.getProduct(new ProductId(productId)));
        bulkInventoryService.reserveStock(product, quantity);
        return ResponseEntity.ok().build();
    }

    /**
     * Release reserved stock
     */
    @PostMapping("/{productId}/release-stock")
    public ResponseEntity<Void> releaseBulkStock(
            @PathVariable Long productId,
            @RequestParam Integer quantity) {
        Product product = productResponseMapper.toProduct(productService.getProduct(new ProductId(productId)));
        bulkInventoryService.releaseStock(product, quantity);
        return ResponseEntity.ok().build();
    }
} 