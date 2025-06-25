package ma.foodplus.ordering.system.pos.service;

import ma.foodplus.ordering.system.pos.domain.Product;
import ma.foodplus.ordering.system.pos.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public List<Product> getActiveProducts() {
        return productRepository.findByActiveTrue();
    }

    public Optional<Product> getProductById(Long id) {
        return productRepository.findById(id);
    }

    public Optional<Product> getProductBySku(String sku) {
        return productRepository.findBySku(sku);
    }

    public Optional<Product> getProductByBarcode(String barcode) {
        return productRepository.findByBarcode(barcode);
    }

    public List<Product> getProductsByCategory(Long categoryId) {
        return productRepository.findByActiveTrueAndCategoryId(categoryId);
    }

    public Page<Product> searchProducts(String search, Pageable pageable) {
        return productRepository.searchActiveProducts(search, pageable);
    }

    public List<Product> getProductsByPriceRange(BigDecimal minPrice, BigDecimal maxPrice) {
        return productRepository.findByPriceRange(minPrice, maxPrice);
    }

    public List<Product> getAvailableProductsByStore(Long storeId) {
        return productRepository.findAvailableProductsByStore(storeId);
    }

    public Product createProduct(Product product) {
        validateProduct(product);
        product.setCreatedAt(LocalDateTime.now());
        product.setUpdatedAt(LocalDateTime.now());
        return productRepository.save(product);
    }

    public Product updateProduct(Long id, Product productDetails) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        product.setName(productDetails.getName());
        product.setDescription(productDetails.getDescription());
        product.setCategory(productDetails.getCategory());
        product.setCostPrice(productDetails.getCostPrice());
        product.setSellingPrice(productDetails.getSellingPrice());
        product.setWholesalePrice(productDetails.getWholesalePrice());
        product.setTax(productDetails.getTax());
        product.setUnit(productDetails.getUnit());
        product.setMinStockLevel(productDetails.getMinStockLevel());
        product.setMaxStockLevel(productDetails.getMaxStockLevel());
        product.setTrackInventory(productDetails.isTrackInventory());
        product.setActive(productDetails.isActive());
        product.setImageUrl(productDetails.getImageUrl());
        product.setUpdatedAt(LocalDateTime.now());

        return productRepository.save(product);
    }

    public void deactivateProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        product.setActive(false);
        product.setUpdatedAt(LocalDateTime.now());
        productRepository.save(product);
    }

    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }

    private void validateProduct(Product product) {
        if (productRepository.existsBySku(product.getSku())) {
            throw new RuntimeException("SKU already exists");
        }
        if (productRepository.existsByBarcode(product.getBarcode())) {
            throw new RuntimeException("Barcode already exists");
        }
    }
}
