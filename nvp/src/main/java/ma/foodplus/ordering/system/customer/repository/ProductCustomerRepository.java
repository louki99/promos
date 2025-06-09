package ma.foodplus.ordering.system.customer.repository;

import ma.foodplus.ordering.system.customer.model.ProductCustomer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductCustomerRepository extends JpaRepository<ProductCustomer, Long> {
    
    @Query("SELECT pc FROM ProductCustomer pc WHERE pc.customer.id = :customerId")
    List<ProductCustomer> findByCustomerId(Long customerId);
    
    @Query("SELECT pc FROM ProductCustomer pc WHERE pc.product.id = :productId")
    List<ProductCustomer> findByProductId(Long productId);
    
    @Query("SELECT pc FROM ProductCustomer pc WHERE pc.customer.id = :customerId AND pc.product.id = :productId")
    Optional<ProductCustomer> findByCustomerIdAndProductId(Long customerId, Long productId);
    
    @Query("SELECT pc FROM ProductCustomer pc WHERE pc.customer.id = :customerId AND pc.category = :category")
    List<ProductCustomer> findByCustomerIdAndCategory(Long customerId, String category);
    
    @Query("SELECT pc FROM ProductCustomer pc WHERE pc.customer.id = :customerId AND pc.prixTTC <= :maxPrice")
    List<ProductCustomer> findByCustomerIdAndMaxPrice(Long customerId, Double maxPrice);
} 