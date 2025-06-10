package ma.foodplus.ordering.system.customer.service.impl;

import lombok.RequiredArgsConstructor;
import ma.foodplus.ordering.system.customer.dto.ProductCustomerDTO;
import ma.foodplus.ordering.system.customer.dto.ProductCustomerPriceHistoryDTO;
import ma.foodplus.ordering.system.customer.dto.ProductCustomerStatisticsDTO;
import ma.foodplus.ordering.system.customer.mapper.ProductCustomerMapper;
import ma.foodplus.ordering.system.customer.model.ProductCustomer;
import ma.foodplus.ordering.system.customer.repository.ProductCustomerRepository;
import ma.foodplus.ordering.system.customer.service.ProductCustomerService;
import ma.foodplus.ordering.system.customer.service.ProductCustomerValidationService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityNotFoundException;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductCustomerServiceImpl implements ProductCustomerService {

    private final ProductCustomerRepository productCustomerRepository;
    private final ProductCustomerMapper productCustomerMapper;
    private final ProductCustomerValidationService validationService;

    @Override
    public ProductCustomerDTO createProductCustomer(ProductCustomerDTO productCustomerDTO) {
        if (productCustomerRepository.findByCustomerIdAndProductId(
                productCustomerDTO.getCustomerId(), 
                productCustomerDTO.getProductId()).isPresent()) {
            throw new IllegalArgumentException("Product customer relationship already exists");
        }
        
        ProductCustomer productCustomer = productCustomerMapper.toEntity(productCustomerDTO);
        productCustomer = productCustomerRepository.save(productCustomer);
        return productCustomerMapper.toDTO(productCustomer);
    }

    @Override
    public ProductCustomerDTO updateProductCustomer(Long id, ProductCustomerDTO productCustomerDTO) {
        ProductCustomer productCustomer = productCustomerRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Product customer not found with id: " + id));
        
        productCustomerMapper.updateEntityFromDTO(productCustomerDTO, productCustomer);
        productCustomer = productCustomerRepository.save(productCustomer);
        return productCustomerMapper.toDTO(productCustomer);
    }

    @Override
    public void deleteProductCustomer(Long id) {
        if (!productCustomerRepository.existsById(id)) {
            throw new EntityNotFoundException("Product customer not found with id: " + id);
        }
        productCustomerRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public ProductCustomerDTO getProductCustomerById(Long id) {
        return productCustomerRepository.findById(id)
                .map(productCustomerMapper::toDTO)
                .orElseThrow(() -> new EntityNotFoundException("Product customer not found with id: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductCustomerDTO> getProductCustomersByCustomerId(Long customerId) {
        return productCustomerRepository.findByCustomerId(customerId).stream()
                .map(productCustomerMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductCustomerDTO> getProductCustomersByProductId(Long productId) {
        return productCustomerRepository.findByProductId(productId).stream()
                .map(productCustomerMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public ProductCustomerDTO getProductCustomerByCustomerIdAndProductId(Long customerId, Long productId) {
        return productCustomerRepository.findByCustomerIdAndProductId(customerId, productId)
                .map(productCustomerMapper::toDTO)
                .orElseThrow(() -> new EntityNotFoundException(
                    "Product customer not found for customer: " + customerId + " and product: " + productId));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductCustomerDTO> getProductCustomersByCustomerIdAndCategory(Long customerId, String category) {
        return productCustomerRepository.findByCustomerIdAndCategory(customerId, category).stream()
                .map(productCustomerMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductCustomerDTO> getProductCustomersByCustomerIdAndMaxPrice(Long customerId, Double maxPrice) {
        return productCustomerRepository.findByCustomerIdAndPrixTTCLessThanEqual(customerId, BigDecimal.valueOf(maxPrice))
                .stream()
                .map(productCustomerMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public void updateProductCustomerPrice(Long id, Double newPrice) {
        ProductCustomer productCustomer = productCustomerRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Product customer not found with id: " + id));
        
        productCustomer.setPrixTTC(BigDecimal.valueOf(newPrice));
        productCustomerRepository.save(productCustomer);
    }

    @Override
    public void updateProductCustomerDiscount(Long id, Double newDiscount) {
        ProductCustomer productCustomer = productCustomerRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Product customer not found with id: " + id));
        
        productCustomer.setRemise(BigDecimal.valueOf(newDiscount));
        productCustomerRepository.save(productCustomer);
    }

    @Override
    public boolean validateProductCustomer(Long id) {
        ProductCustomer productCustomer = productCustomerRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Product customer not found with id: " + id));
        return validationService.validateProductCustomer(productCustomer).isEmpty();
    }

    @Override
    public List<ProductCustomerDTO> getCustomerPreferredProducts(Long customerId) {
        return productCustomerRepository.findByCustomerIdAndActiveTrue(customerId).stream()
                .map(productCustomerMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public boolean isDiscountChangeValid(Long id, BigDecimal newDiscount) {
        ProductCustomer productCustomer = productCustomerRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Product customer not found with id: " + id));
        return validationService.isDiscountChangeValid(productCustomer, newDiscount);
    }

    @Override
    public List<ProductCustomerDTO> getCustomerProductsWithPriceChanges(Long customerId) {
        return productCustomerRepository.findWithPriceChangesByCustomerId(customerId).stream()
                .map(productCustomerMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductCustomerPriceHistoryDTO> getPriceHistory(Long id) {
        // Implementation would depend on your price history tracking system
        return Collections.emptyList();
    }

    @Override
    public ProductCustomerStatisticsDTO getProductCustomerStatistics(Long customerId) {
        // Implementation would depend on your statistics calculation logic
        return new ProductCustomerStatisticsDTO();
    }

    @Override
    public List<ProductCustomerDTO> getProductCustomersByDiscountRange(BigDecimal minDiscount, BigDecimal maxDiscount) {
        return productCustomerRepository.findByCustomerIdAndRemiseGreaterThan(1L, minDiscount).stream()
                .filter(pc -> pc.getRemise().compareTo(maxDiscount) <= 0)
                .map(productCustomerMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductCustomerDTO> searchProductCustomers(String searchTerm) {
        return productCustomerRepository.searchByCustomerIdAndTerm(1L, searchTerm).stream()
                .map(productCustomerMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductCustomerDTO> updateBulkPrices(List<Long> ids, Double newPrice) {
        List<ProductCustomer> productCustomers = productCustomerRepository.findAllById(ids);
        productCustomers.forEach(pc -> pc.setPrixTTC(BigDecimal.valueOf(newPrice)));
        return productCustomerRepository.saveAll(productCustomers).stream()
                .map(productCustomerMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Map<Long, BigDecimal> getProductPricesForCustomer(Long customerId) {
        return productCustomerRepository.findProductPricesForCustomer(customerId).stream()
                .collect(Collectors.toMap(
                    map -> (Long) map.get("productId"),
                    map -> (BigDecimal) map.get("price")
                ));
    }

    @Override
    public Map<String, BigDecimal> getAveragePricesByCategory(Long customerId) {
        return productCustomerRepository.findAveragePricesByCategory(customerId).stream()
                .collect(Collectors.toMap(
                    map -> (String) map.get("category"),
                    map -> (BigDecimal) map.get("averagePrice")
                ));
    }

    @Override
    public List<ProductCustomerDTO> getProductCustomersWithPendingPriceChanges(Long productId) {
        return productCustomerRepository.findWithPendingPriceChangesByProductId(productId).stream()
                .map(productCustomerMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductCustomerDTO> getCustomerRecentlyUpdatedProducts(Long customerId) {
        ZonedDateTime thirtyDaysAgo = ZonedDateTime.now().minusDays(30);
        return productCustomerRepository.findRecentlyUpdatedByCustomerId(customerId, thirtyDaysAgo).stream()
                .map(productCustomerMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductCustomerDTO> updateBulkDiscounts(List<Long> ids, Double newDiscount) {
        List<ProductCustomer> productCustomers = productCustomerRepository.findAllById(ids);
        productCustomers.forEach(pc -> pc.setRemise(BigDecimal.valueOf(newDiscount)));
        return productCustomerRepository.saveAll(productCustomers).stream()
                .map(productCustomerMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Map<String, BigDecimal> getAverageDiscountsByCategory(Long customerId) {
        return productCustomerRepository.findAverageDiscountsByCategory(customerId).stream()
                .collect(Collectors.toMap(
                    map -> (String) map.get("category"),
                    map -> (BigDecimal) map.get("averageDiscount")
                ));
    }

    @Override
    public List<ProductCustomerDTO> getProductCustomersByPriceRange(BigDecimal minPrice, BigDecimal maxPrice) {
        return productCustomerRepository.findByCustomerIdAndPrixTTCBetween(1L, minPrice, maxPrice).stream()
                .map(productCustomerMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public void deactivateProductCustomers(List<Long> ids) {
        List<ProductCustomer> productCustomers = productCustomerRepository.findAllById(ids);
        productCustomers.forEach(pc -> pc.setActive(false));
        productCustomerRepository.saveAll(productCustomers);
    }

    @Override
    public void revertNewPrices(Long id) {
        ProductCustomer productCustomer = productCustomerRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Product customer not found with id: " + id));
        productCustomer.setPrixVenNouv(null);
        productCustomerRepository.save(productCustomer);
    }

    @Override
    public boolean isPriceChangeValid(Long id, BigDecimal newPrice) {
        ProductCustomer productCustomer = productCustomerRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Product customer not found with id: " + id));
        return validationService.isPriceChangeValid(productCustomer, newPrice);
    }

    @Override
    public void applyNewPrices(Long id) {
        ProductCustomer productCustomer = productCustomerRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Product customer not found with id: " + id));
        if (productCustomer.getPrixVenNouv() != null) {
            productCustomer.setPrixTTC(productCustomer.getPrixVenNouv());
            productCustomer.setPrixVenNouv(null);
            productCustomerRepository.save(productCustomer);
        }
    }

    @Override
    public List<ProductCustomerDTO> getProductCustomersWithActiveDiscounts(Long productId) {
        return productCustomerRepository.findWithActiveDiscountsByProductId(productId).stream()
                .map(productCustomerMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public void activateProductCustomers(List<Long> ids) {
        List<ProductCustomer> productCustomers = productCustomerRepository.findAllById(ids);
        productCustomers.forEach(pc -> pc.setActive(true));
        productCustomerRepository.saveAll(productCustomers);
    }
} 