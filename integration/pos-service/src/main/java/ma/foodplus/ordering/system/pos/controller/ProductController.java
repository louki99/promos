package ma.foodplus.ordering.system.pos.controller;

import ma.foodplus.ordering.system.pos.domain.Product;
import ma.foodplus.ordering.system.pos.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/products")
@Tag(name = "Product Management", description = "Endpoints for managing products and inventory.")
public class ProductController {
    @Autowired
    private ProductService productService;

    @GetMapping
    @Operation(summary = "Get all products", description = "Retrieve a list of all products.")
    public List<Product> getAllProducts() {
        return productService.getAllProducts();
    }

    @GetMapping("/active")
    @Operation(summary = "Get active products", description = "Retrieve a list of all active products.")
    public List<Product> getActiveProducts() {
        return productService.getActiveProducts();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get product by ID", description = "Retrieve a product by its unique ID.")
    public ResponseEntity<Product> getProductById(@PathVariable Long id) {
        return productService.getProductById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/sku/{sku}")
    @Operation(summary = "Get product by SKU", description = "Retrieve a product by its unique SKU.")
    public ResponseEntity<Product> getProductBySku(@PathVariable String sku) {
        return productService.getProductBySku(sku)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/barcode/{barcode}")
    @Operation(summary = "Get product by barcode", description = "Retrieve a product by its barcode.")
    public ResponseEntity<Product> getProductByBarcode(@PathVariable String barcode) {
        return productService.getProductByBarcode(barcode)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/category/{categoryId}")
    @Operation(summary = "Get products by category", description = "Retrieve all products in a specific category.")
    public List<Product> getProductsByCategory(@PathVariable Long categoryId) {
        return productService.getProductsByCategory(categoryId);
    }

    @GetMapping("/search")
    @Operation(summary = "Search products", description = "Search products by name or description with pagination.")
    public Page<Product> searchProducts(@RequestParam String search, Pageable pageable) {
        return productService.searchProducts(search, pageable);
    }

    @GetMapping("/price-range")
    @Operation(summary = "Get products by price range", description = "Retrieve products within a specific price range.")
    public List<Product> getProductsByPriceRange(@RequestParam BigDecimal min, @RequestParam BigDecimal max) {
        return productService.getProductsByPriceRange(min, max);
    }

    @GetMapping("/available/store/{storeId}")
    @Operation(summary = "Get available products by store", description = "Retrieve products available in a specific store.")
    public List<Product> getAvailableProductsByStore(@PathVariable Long storeId) {
        return productService.getAvailableProductsByStore(storeId);
    }

    @PostMapping
    @Operation(summary = "Create product", description = "Create a new product.")
    public Product createProduct(@RequestBody Product product) {
        return productService.createProduct(product);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update product", description = "Update an existing product by ID.")
    public Product updateProduct(@PathVariable Long id, @RequestBody Product product) {
        return productService.updateProduct(id, product);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete product", description = "Delete a product by ID.")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }
} 