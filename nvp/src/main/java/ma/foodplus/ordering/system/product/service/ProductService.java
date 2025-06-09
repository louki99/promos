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
import ma.foodplus.ordering.system.product.model.Product;
import ma.foodplus.ordering.system.product.repository.ProductRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.math.BigDecimal;

@Slf4j
@Service
public class ProductService implements ProductManagementUseCase {

    private final ProductRepository productRepository;
    private final ProductPersistenceMapper mapper;

    public ProductService(ProductRepository productRepository,ProductPersistenceMapper mapper) {
        this.productRepository=productRepository;
        this.mapper = mapper;
    }

    @Override
    @CacheEvict(value = {CacheConstants.PRODUCTS_CACHE, CacheConstants.PRODUCT_CACHE}, allEntries = true)
    public ProductId createProduct(CreateProductCommand command) {
        try {
            var productEntity = mapper.createCommandToEntity(command);
            var savedProduct = productRepository.save(productEntity);
            return mapper.entityToProductId(savedProduct);
        } catch (Exception e) {
            log.error("Error creating product: {}", e.getMessage(), e);
            throw new ProductCreationException("Failed to create product: " + e.getMessage());
        }
    }

    @Override
    @Cacheable(value = CacheConstants.PRODUCT_CACHE, key = "'product:' + #id.value")
    public ProductResponse getProduct(ProductId id) {
        return productRepository.findById(id.getValue())
                .map(mapper::entityToResponse)
                .orElseThrow(() -> new ProductNotFoundException("Product not found with id: " + id.getValue()));
    }

    @Override
    @Cacheable(value = CacheConstants.PRODUCT_CACHE, key = "'reference:' + #reference")
    public ProductResponse getProductByReference(String reference) {
        return productRepository.findByReference(reference)
                .map(mapper::entityToResponse)
                .orElseThrow(() -> new ProductNotFoundException("Product not found with reference: " + reference));
    }

    @Override
    @Cacheable(value = CacheConstants.PRODUCT_CACHE, key = "'barcode:' + #barcode")
    public ProductResponse getProductByBarcode(String barcode) {
        return productRepository.findByBarcode(barcode)
                .map(mapper::entityToResponse)
                .orElseThrow(() -> new ProductNotFoundException("Product not found with barcode: " + barcode));
    }

    @Override
    @Cacheable(value = CacheConstants.PRODUCTS_CACHE, key = "'family:' + #familyCode")
    public List<ProductResponse> getProductsByFamilyCode(String familyCode) {
        return productRepository.findByFamilyCode(familyCode).stream()
                .map(mapper::entityToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Cacheable(value = CacheConstants.PRODUCTS_CACHE, key = "'deliverable'")
    public List<ProductResponse> getDeliverableProducts() {
        return productRepository.findByDeliverableTrue().stream()
                .map(mapper::entityToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Cacheable(value = CacheConstants.PRODUCTS_CACHE, key = "'active'")
    public List<ProductResponse> getActiveProducts() {
        return productRepository.findByInactiveFalse().stream()
                .map(mapper::entityToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Cacheable(value = CacheConstants.PRODUCTS_CACHE, key = "'all'")
    public List<ProductResponse> getAllProducts() {
        return productRepository.findAll().stream()
                .map(mapper::entityToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @CacheEvict(value = {CacheConstants.PRODUCTS_CACHE, CacheConstants.PRODUCT_CACHE}, allEntries = true)
    public ProductId updateProduct(ProductId productId, UpdateProductCommand command) {
        Product existingProduct = productRepository.findById(productId.getValue())
                .orElseThrow(() -> new ProductNotFoundException("Product not found with id: " + productId.getValue()));
        
        mapper.updateEntityFromCommand(command, existingProduct);
        Product updatedProduct = productRepository.save(existingProduct);
        return mapper.entityToProductId(updatedProduct);
    }

    @Override
    @CacheEvict(value = {CacheConstants.PRODUCTS_CACHE, CacheConstants.PRODUCT_CACHE}, allEntries = true)
    public void deleteProduct(ProductId id) {
        if (! productRepository.existsById(id.getValue())) {
            throw new ProductNotFoundException("Product not found with id: " + id.getValue());
        }
        productRepository.deleteById(id.getValue());
    }

    @Override
    @Cacheable(value = CacheConstants.PRODUCT_CACHE, key = "'exists:reference:' + #reference")
    public boolean existsByReference(String reference) {
        return productRepository.existsByReference(reference);
    }

    @Override
    @Cacheable(value = CacheConstants.PRODUCT_CACHE, key = "'exists:barcode:' + #barcode")
    public boolean existsByBarcode(String barcode) {
        return productRepository.existsByBarcode(barcode);
    }

    @Override
    public ProductResponse getProductNameById(ProductId id){
        Optional<Product> product=productRepository.findById(id.getValue());
        if (product.isEmpty()) {
            throw new ProductNotFoundException("Product not found with id: " + id.getValue());
        }
        return mapper.entityToResponse(product.get());
    }

    @Override
    public List<String> getProductCategory(String productId){
        Product product = productRepository.findById(Long.valueOf(productId))
                .orElseThrow(() -> new ProductNotFoundException("Product not found for id: " + productId));

        List<String> categories = List.of(
                product.getCategory1(),
                product.getCategory2(),
                product.getCategory3(),
                product.getCategory4()
        );

        if (categories.stream().anyMatch(Objects::isNull)) {
            throw new ProductNotFoundException("Product categories not found for id: " + productId);
        }

        return categories;
    }


    @Override
    public double getProductPrice(String entityId){
        Optional<Product> product = productRepository.findById(Long.parseLong(entityId));
        if (product.isEmpty()) {
            throw new ProductNotFoundException("Product not found with id: " + entityId);
        }
        BigDecimal salePrice=product.get().getSalePrice();
        if (salePrice == null) {
            throw new ProductNotFoundException("Product sale price not found for id: " + entityId);
        }
        return salePrice.doubleValue();

    }
}
