package ma.foodplus.ordering.system.customer.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import ma.foodplus.ordering.system.customer.dto.ProductCustomerDTO;
import ma.foodplus.ordering.system.customer.service.ProductCustomerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/product-customers")
@RequiredArgsConstructor
@Tag(name = "Product Customer Management", description = "APIs for managing product-customer relationships")
public class ProductCustomerController {

    private final ProductCustomerService productCustomerService;

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
    @Operation(summary = "Get a product-customer relationship by customer and product IDs")
    public ResponseEntity<ProductCustomerDTO> getProductCustomerByCustomerIdAndProductId(
            @PathVariable Long customerId,
            @PathVariable Long productId) {
        return ResponseEntity.ok(productCustomerService.getProductCustomerByCustomerIdAndProductId(
                customerId, productId));
    }

    @GetMapping("/customer/{customerId}/category/{category}")
    @Operation(summary = "Get product-customer relationships by customer ID and category")
    public ResponseEntity<List<ProductCustomerDTO>> getProductCustomersByCustomerIdAndCategory(
            @PathVariable Long customerId,
            @PathVariable String category) {
        return ResponseEntity.ok(productCustomerService.getProductCustomersByCustomerIdAndCategory(
                customerId, category));
    }

    @GetMapping("/customer/{customerId}/max-price/{maxPrice}")
    @Operation(summary = "Get product-customer relationships by customer ID and maximum price")
    public ResponseEntity<List<ProductCustomerDTO>> getProductCustomersByCustomerIdAndMaxPrice(
            @PathVariable Long customerId,
            @PathVariable Double maxPrice) {
        return ResponseEntity.ok(productCustomerService.getProductCustomersByCustomerIdAndMaxPrice(
                customerId, maxPrice));
    }

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
} 