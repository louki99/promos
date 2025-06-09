package ma.foodplus.ordering.system.product.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import ma.foodplus.ordering.system.domain.valueobject.ProductId;
import ma.foodplus.ordering.system.product.configuration.CacheConstants;
import ma.foodplus.ordering.system.product.dto.create.CreateProductCommand;
import ma.foodplus.ordering.system.product.dto.response.ProductResponse;
import ma.foodplus.ordering.system.product.dto.update.UpdateProductCommand;
import ma.foodplus.ordering.system.product.service.ProductManagementUseCase;
import ma.foodplus.ordering.system.product.service.ProductService;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
@Tag(name = "Product Management", description = "APIs for managing product")
public class ProductController {

    private final ProductService productService;
    private final ProductManagementUseCase productManagementUseCase;

    @GetMapping
    @Operation(summary = "Get all product")
    public ResponseEntity<List<ProductResponse>> getAllProducts() {
        return ResponseEntity.ok(productService.getAllProducts());
    }

    @GetMapping("/{productId}")
    @Operation(summary = "Get a product by ID")
    public ResponseEntity<ProductResponse> getProductById(@PathVariable Long productId) {
        return ResponseEntity.ok(productService.getProduct(
                new ProductId(productId)));
    }

    @PostMapping
    @Operation(summary = "Create a new product")
    @CacheEvict(value = {CacheConstants.PRODUCTS_CACHE, CacheConstants.PRODUCT_CACHE}, allEntries = true)
    public ResponseEntity<ProductId> createProduct(@RequestBody CreateProductCommand productDTO) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(productService.createProduct(productDTO));
    }

    @PutMapping("/{productId}")
    @Operation(summary = "Update a product")
    @CacheEvict(value = {CacheConstants.PRODUCTS_CACHE, CacheConstants.PRODUCT_CACHE}, allEntries = true)
    public ResponseEntity<ProductId> updateProduct(@PathVariable Long productId, @RequestBody UpdateProductCommand updateProductCommand) {
        return ResponseEntity.ok(
                productService.updateProduct(new ProductId(productId), updateProductCommand));
    }

    @DeleteMapping("/{productId}")
    @Operation(summary = "Delete a product")
    @CacheEvict(value = {CacheConstants.PRODUCTS_CACHE, CacheConstants.PRODUCT_CACHE}, allEntries = true)
    public ResponseEntity<Void> deleteProduct(@PathVariable Long productId) {
        productService.deleteProduct(new ProductId(productId));
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/reference/{reference}")
    @Operation(summary = "Get a product by reference")
    public ResponseEntity<ProductResponse> getProductByReference(@PathVariable String reference) {
        return ResponseEntity.ok(productManagementUseCase.getProductByReference(reference));
    }

    @GetMapping("/barcode/{barcode}")
    @Operation(summary = "Get a product by barcode")
    public ResponseEntity<ProductResponse> getProductByBarcode(@PathVariable String barcode) {
        return ResponseEntity.ok(productManagementUseCase.getProductByBarcode(barcode));
    }

    @GetMapping("/family/{familyCode}")
    @Operation(summary = "Get product by family code")
    public ResponseEntity<List<ProductResponse>> getProductsByFamilyCode(@PathVariable String familyCode) {
        return ResponseEntity.ok(productManagementUseCase.getProductsByFamilyCode(familyCode));
    }

    @GetMapping("/deliverable")
    @Operation(summary = "Get all deliverable product")
    public ResponseEntity<List<ProductResponse>> getDeliverableProducts() {
        return ResponseEntity.ok(productManagementUseCase.getDeliverableProducts());
    }

    @GetMapping("/active")
    @Operation(summary = "Get all active product")
    public ResponseEntity<List<ProductResponse>> getActiveProducts() {
        return ResponseEntity.ok(productManagementUseCase.getActiveProducts());
    }

    @GetMapping("/check/reference/{reference}")
    @Operation(summary = "Check if a product exists by reference")
    public ResponseEntity<Boolean> existsByReference(@PathVariable String reference) {
        return ResponseEntity.ok(productManagementUseCase.existsByReference(reference));
    }

    @GetMapping("/check/barcode/{barcode}")
    @Operation(summary = "Check if a product exists by barcode")
    public ResponseEntity<Boolean> existsByBarcode(@PathVariable String barcode) {
        return ResponseEntity.ok(productManagementUseCase.existsByBarcode(barcode));
    }
} 