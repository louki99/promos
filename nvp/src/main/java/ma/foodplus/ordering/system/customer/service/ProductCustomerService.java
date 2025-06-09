package ma.foodplus.ordering.system.customer.service;

import ma.foodplus.ordering.system.customer.dto.ProductCustomerDTO;
import java.util.List;

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
} 