package ma.foodplus.ordering.system.customer.service;

import ma.foodplus.ordering.system.customer.dto.ProductCustomerDTO;
import ma.foodplus.ordering.system.customer.dto.ProductCustomerPriceHistoryDTO;
import ma.foodplus.ordering.system.customer.dto.ProductCustomerStatisticsDTO;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface ProductCustomerService {
    ProductCustomerDTO createProductCustomer(ProductCustomerDTO productCustomerDTO);
    ProductCustomerDTO updateProductCustomer(Long id, ProductCustomerDTO productCustomerDTO);
    void deleteProductCustomer(Long id);
    ProductCustomerDTO getProductCustomerById(Long id);
    List<ProductCustomerDTO> getProductCustomersByCustomerId(Long customerId);
    List<ProductCustomerDTO> getProductCustomersByProductId(Long productId);
    ProductCustomerDTO getProductCustomerByCustomerIdAndProductId(Long customerId, Long productId);
    List<ProductCustomerDTO> getProductCustomersByCustomerIdAndCategory(Long customerId, String category);
    List<ProductCustomerDTO> getProductCustomersByCustomerIdAndMaxPrice(Long customerId, Double maxPrice);
    void updateProductCustomerPrice(Long id, Double newPrice);
    void updateProductCustomerDiscount(Long id, Double newDiscount);
    void applyNewPrices(Long id);
    void revertNewPrices(Long id);
    List<ProductCustomerDTO> updateBulkPrices(List<Long> ids, Double newPrice);
    List<ProductCustomerDTO> updateBulkDiscounts(List<Long> ids, Double newDiscount);
    void deactivateProductCustomers(List<Long> ids);
    void activateProductCustomers(List<Long> ids);
    List<ProductCustomerPriceHistoryDTO> getPriceHistory(Long id);
    ProductCustomerStatisticsDTO getProductCustomerStatistics(Long customerId);
    Map<String, BigDecimal> getAveragePricesByCategory(Long customerId);
    Map<String, BigDecimal> getAverageDiscountsByCategory(Long customerId);
    boolean validateProductCustomer(Long id);
    boolean isPriceChangeValid(Long id, BigDecimal newPrice);
    boolean isDiscountChangeValid(Long id, BigDecimal newDiscount);
    List<ProductCustomerDTO> searchProductCustomers(String searchTerm);
    List<ProductCustomerDTO> getProductCustomersByPriceRange(BigDecimal minPrice, BigDecimal maxPrice);
    List<ProductCustomerDTO> getProductCustomersByDiscountRange(BigDecimal minDiscount, BigDecimal maxDiscount);
    List<ProductCustomerDTO> getCustomerPreferredProducts(Long customerId);
    List<ProductCustomerDTO> getCustomerRecentlyUpdatedProducts(Long customerId);
    List<ProductCustomerDTO> getCustomerProductsWithPriceChanges(Long customerId);
    List<ProductCustomerDTO> getProductCustomersWithActiveDiscounts(Long productId);
    List<ProductCustomerDTO> getProductCustomersWithPendingPriceChanges(Long productId);
    Map<Long, BigDecimal> getProductPricesForCustomer(Long customerId);
} 