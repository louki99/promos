package ma.foodplus.ordering.system.product.service;

import lombok.extern.slf4j.Slf4j;
import ma.foodplus.ordering.system.domain.valueobject.ProductId;
import ma.foodplus.ordering.system.product.configuration.CacheConstants;
import ma.foodplus.ordering.system.product.dto.create.CreateProductCommand;
import ma.foodplus.ordering.system.product.dto.response.ProductResponse;
import ma.foodplus.ordering.system.product.dto.update.UpdateProductCommand;
import ma.foodplus.ordering.system.product.exception.ProductCreationException;
import ma.foodplus.ordering.system.product.exception.ProductNotFoundException;
import ma.foodplus.ordering.system.product.mapper.ProductPersistenceMapper;
import ma.foodplus.ordering.system.product.mapper.ProductResponseMapper;
import ma.foodplus.ordering.system.product.model.Category;
import ma.foodplus.ordering.system.product.model.Product;
import ma.foodplus.ordering.system.product.repository.ProductRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.math.BigDecimal;

@Slf4j
@Service
public class ProductService implements ProductManagementUseCase {

    private final ProductRepository productRepository;
    private final ProductPersistenceMapper persistenceMapper;
    private final ProductResponseMapper responseMapper;

    public ProductService(ProductRepository productRepository, 
                         ProductPersistenceMapper persistenceMapper,
                         ProductResponseMapper responseMapper) {
        this.productRepository = productRepository;
        this.persistenceMapper = persistenceMapper;
        this.responseMapper = responseMapper;
    }

    @Override
    @Transactional
    @CacheEvict(value = {CacheConstants.PRODUCTS_CACHE, CacheConstants.PRODUCT_CACHE}, allEntries = true)
    public ProductId createProduct(CreateProductCommand command) {
        try {
            var productEntity = persistenceMapper.toEntity(command);
            var savedProduct = productRepository.save(productEntity);
            return new ProductId(savedProduct.getId());
        } catch (Exception e) {
            log.error("Error creating product: {}", e.getMessage(), e);
            throw new ProductCreationException("Failed to create product: " + e.getMessage());
        }
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = CacheConstants.PRODUCT_CACHE, key = "'product:' + #id.value")
    public ProductResponse getProduct(ProductId id) {
        return productRepository.findById(id.getValue())
                .map(responseMapper::toResponse)
                .orElseThrow(() -> new ProductNotFoundException("Product not found with id: " + id.getValue()));
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = CacheConstants.PRODUCT_CACHE, key = "'reference:' + #reference")
    public ProductResponse getProductByReference(String reference) {
        return productRepository.findByReference(reference)
                .map(responseMapper::toResponse)
                .orElseThrow(() -> new ProductNotFoundException("Product not found with reference: " + reference));
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = CacheConstants.PRODUCT_CACHE, key = "'barcode:' + #barcode")
    public ProductResponse getProductByBarcode(String barcode) {
        return productRepository.findByBarcode(barcode)
                .map(responseMapper::toResponse)
                .orElseThrow(() -> new ProductNotFoundException("Product not found with barcode: " + barcode));
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = CacheConstants.PRODUCTS_CACHE, key = "'family:' + #familyCode")
    public List<ProductResponse> getProductsByFamilyCode(String familyCode) {
        return productRepository.findByProductFamilyCode(familyCode).stream()
                .map(responseMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = CacheConstants.PRODUCTS_CACHE, key = "'deliverable'")
    public List<ProductResponse> getDeliverableProducts() {
        return productRepository.findByDeliverableTrue().stream()
                .map(responseMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = CacheConstants.PRODUCTS_CACHE, key = "'active'")
    public List<ProductResponse> getActiveProducts() {
        return productRepository.findByInactiveFalse().stream()
                .map(responseMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductResponse> getAllProducts() {
        log.info("Getting all products");
        return productRepository.findAll().stream()
                .map(product -> {
                    // Force initialization of categories collection
                    product.getCategories().size();
                    return responseMapper.toResponse(product);
                })
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    @CacheEvict(value = {CacheConstants.PRODUCTS_CACHE, CacheConstants.PRODUCT_CACHE}, allEntries = true)
    public ProductId updateProduct(ProductId productId, UpdateProductCommand command) {
        Product existingProduct = productRepository.findById(productId.getValue())
                .orElseThrow(() -> new ProductNotFoundException("Product not found with id: " + productId.getValue()));
        
        persistenceMapper.updateEntity(existingProduct, command);
        Product updatedProduct = productRepository.save(existingProduct);
        return new ProductId(updatedProduct.getId());
    }

    @Override
    @Transactional
    @CacheEvict(value = {CacheConstants.PRODUCTS_CACHE, CacheConstants.PRODUCT_CACHE}, allEntries = true)
    public void deleteProduct(ProductId id) {
        if (!productRepository.existsById(id.getValue())) {
            throw new ProductNotFoundException("Product not found with id: " + id.getValue());
        }
        productRepository.deleteById(id.getValue());
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = CacheConstants.PRODUCT_CACHE, key = "'exists:reference:' + #reference")
    public boolean existsByReference(String reference) {
        return productRepository.existsByReference(reference);
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = CacheConstants.PRODUCT_CACHE, key = "'exists:barcode:' + #barcode")
    public boolean existsByBarcode(String barcode) {
        return productRepository.existsByBarcode(barcode);
    }

    @Override
    @Transactional(readOnly = true)
    public ProductResponse getProductNameById(ProductId id) {
        Optional<Product> product = productRepository.findById(id.getValue());
        if (product.isEmpty()) {
            throw new ProductNotFoundException("Product not found with id: " + id.getValue());
        }
        return responseMapper.toResponse(product.get());
    }

    @Override
    @Transactional(readOnly = true)
    public List<String> getProductCategory(String productId) {
        Product product = productRepository.findById(Long.valueOf(productId))
                .orElseThrow(() -> new ProductNotFoundException("Product not found for id: " + productId));

        if (product.getCategories() == null || product.getCategories().isEmpty()) {
            throw new ProductNotFoundException("Product categories not found for id: " + productId);
        }

        return product.getCategories().stream()
                .map(Category::getName)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public double getProductPrice(String entityId) {
        Optional<Product> product = productRepository.findById(Long.parseLong(entityId));
        if (product.isEmpty()) {
            throw new ProductNotFoundException("Product not found with id: " + entityId);
        }
        BigDecimal salePrice = product.get().getSalePrice();
        if (salePrice == null) {
            throw new ProductNotFoundException("Product sale price not found for id: " + entityId);
        }
        return salePrice.doubleValue();
    }
}
