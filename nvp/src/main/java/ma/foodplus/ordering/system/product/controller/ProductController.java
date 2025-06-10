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
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.Parameter;
import ma.foodplus.ordering.system.common.dto.ErrorResponse;

import java.util.List;


@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
@Tag(name = "Product Management", description = "APIs for managing products, including creation, update, deletion, and product lookups.")
public class ProductController {

    private final ProductService productService;
    private final ProductManagementUseCase productManagementUseCase;

    @GetMapping
    @Operation(summary = "Get all products", description = "Retrieves all products in the system.")
    @ApiResponse(responseCode = "200", description = "Products retrieved", content = @Content(schema = @Schema(implementation = ProductResponse.class)))
    public ResponseEntity<List<ProductResponse>> getAllProducts() {
        return ResponseEntity.ok(productService.getAllProducts());
    }

    @GetMapping("/{productId}")
    @Operation(summary = "Get a product by ID", description = "Retrieves a product by its unique ID.")
    @ApiResponse(responseCode = "200", description = "Product found", content = @Content(schema = @Schema(implementation = ProductResponse.class)))
    @ApiResponse(responseCode = "404", description = "Product not found", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    public ResponseEntity<ProductResponse> getProductById(@Parameter(description = "Product ID", required = true) @PathVariable Long productId) {
        return ResponseEntity.ok(productService.getProduct(
                new ProductId(productId)));
    }

    @PostMapping
    @Operation(summary = "Create a new product", description = "Creates a new product in the system.")
    @ApiResponse(responseCode = "201", description = "Product created successfully", content = @Content(schema = @Schema(implementation = ProductId.class)))
    @ApiResponse(responseCode = "400", description = "Invalid product data", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @CacheEvict(value = {CacheConstants.PRODUCTS_CACHE, CacheConstants.PRODUCT_CACHE}, allEntries = true)
    public ResponseEntity<ProductId> createProduct(@io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Product to create", required = true, content = @Content(schema = @Schema(implementation = CreateProductCommand.class))) @RequestBody CreateProductCommand productDTO) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(productService.createProduct(productDTO));
    }

    @PutMapping("/{productId}")
    @Operation(summary = "Update a product", description = "Updates an existing product by ID.")
    @ApiResponse(responseCode = "200", description = "Product updated successfully", content = @Content(schema = @Schema(implementation = ProductId.class)))
    @ApiResponse(responseCode = "404", description = "Product not found", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @CacheEvict(value = {CacheConstants.PRODUCTS_CACHE, CacheConstants.PRODUCT_CACHE}, allEntries = true)
    public ResponseEntity<ProductId> updateProduct(@Parameter(description = "Product ID", required = true) @PathVariable Long productId, @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Product update data", required = true, content = @Content(schema = @Schema(implementation = UpdateProductCommand.class))) @RequestBody UpdateProductCommand updateProductCommand) {
        return ResponseEntity.ok(
                productService.updateProduct(new ProductId(productId), updateProductCommand));
    }

    @DeleteMapping("/{productId}")
    @Operation(summary = "Delete a product", description = "Deletes a product by its ID.")
    @ApiResponse(responseCode = "204", description = "Product deleted successfully")
    @ApiResponse(responseCode = "404", description = "Product not found", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @CacheEvict(value = {CacheConstants.PRODUCTS_CACHE, CacheConstants.PRODUCT_CACHE}, allEntries = true)
    public ResponseEntity<Void> deleteProduct(@Parameter(description = "Product ID", required = true) @PathVariable Long productId) {
        productService.deleteProduct(new ProductId(productId));
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/reference/{reference}")
    @Operation(summary = "Get a product by reference", description = "Retrieves a product by its reference.")
    @ApiResponse(responseCode = "200", description = "Product found", content = @Content(schema = @Schema(implementation = ProductResponse.class)))
    @ApiResponse(responseCode = "404", description = "Product not found", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    public ResponseEntity<ProductResponse> getProductByReference(@Parameter(description = "Product reference", required = true) @PathVariable String reference) {
        return ResponseEntity.ok(productManagementUseCase.getProductByReference(reference));
    }

    @GetMapping("/barcode/{barcode}")
    @Operation(summary = "Get a product by barcode", description = "Retrieves a product by its barcode.")
    @ApiResponse(responseCode = "200", description = "Product found", content = @Content(schema = @Schema(implementation = ProductResponse.class)))
    @ApiResponse(responseCode = "404", description = "Product not found", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    public ResponseEntity<ProductResponse> getProductByBarcode(@Parameter(description = "Product barcode", required = true) @PathVariable String barcode) {
        return ResponseEntity.ok(productManagementUseCase.getProductByBarcode(barcode));
    }

    @GetMapping("/family/{familyCode}")
    @Operation(summary = "Get products by family code", description = "Retrieves products by their family code.")
    @ApiResponse(responseCode = "200", description = "Products found", content = @Content(schema = @Schema(implementation = ProductResponse.class)))
    public ResponseEntity<List<ProductResponse>> getProductsByFamilyCode(@Parameter(description = "Family code", required = true) @PathVariable String familyCode) {
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