package ma.foodplus.ordering.system.customer.repository;

import ma.foodplus.ordering.system.customer.model.ProductCustomer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public interface ProductCustomerRepository extends JpaRepository<ProductCustomer, Long> {
    
    Optional<ProductCustomer> findByCustomerIdAndProductId(Long customerId, Long productId);
    
    List<ProductCustomer> findByCustomerId(Long customerId);
    
    List<ProductCustomer> findByProductId(Long productId);
    
    List<ProductCustomer> findByCustomerIdAndCategory(Long customerId, String category);
    
    List<ProductCustomer> findByCustomerIdAndPrixTTCLessThanEqual(Long customerId, BigDecimal maxPrice);
    
    List<ProductCustomer> findByCustomerIdAndActiveTrue(Long customerId);
    
    List<ProductCustomer> findByProductIdAndActiveTrue(Long productId);
    
    List<ProductCustomer> findByCustomerIdAndPrixVenNouvIsNotNull(Long customerId);
    
    List<ProductCustomer> findByProductIdAndPrixVenNouvIsNotNull(Long productId);
    
    List<ProductCustomer> findByCustomerIdAndRemiseGreaterThan(Long customerId, BigDecimal minDiscount);
    
    List<ProductCustomer> findByCustomerIdAndRemiseLessThanEqual(Long customerId, BigDecimal maxDiscount);
    
    List<ProductCustomer> findByCustomerIdAndPrixTTCBetween(Long customerId, BigDecimal minPrice, BigDecimal maxPrice);
    
    @Query("SELECT pc FROM ProductCustomer pc WHERE pc.customer.id = :customerId AND pc.updatedAt >= :date")
    List<ProductCustomer> findRecentlyUpdatedByCustomerId(@Param("customerId") Long customerId, @Param("date") ZonedDateTime date);
    
    @Query("SELECT pc FROM ProductCustomer pc WHERE pc.customer.id = :customerId AND pc.prixVenNouv IS NOT NULL")
    List<ProductCustomer> findWithPriceChangesByCustomerId(@Param("customerId") Long customerId);
    
    @Query("SELECT pc FROM ProductCustomer pc WHERE pc.product.id = :productId AND pc.remise > 0")
    List<ProductCustomer> findWithActiveDiscountsByProductId(@Param("productId") Long productId);
    
    @Query("SELECT pc FROM ProductCustomer pc WHERE pc.product.id = :productId AND pc.prixVenNouv IS NOT NULL")
    List<ProductCustomer> findWithPendingPriceChangesByProductId(@Param("productId") Long productId);
    
    @Query("SELECT pc.category as category, AVG(pc.prixTTC) as averagePrice " +
           "FROM ProductCustomer pc " +
           "WHERE pc.customer.id = :customerId " +
           "GROUP BY pc.category")
    List<Map<String, Object>> findAveragePricesByCategory(@Param("customerId") Long customerId);
    
    @Query("SELECT pc.category as category, AVG(pc.remise) as averageDiscount " +
           "FROM ProductCustomer pc " +
           "WHERE pc.customer.id = :customerId AND pc.remise > 0 " +
           "GROUP BY pc.category")
    List<Map<String, Object>> findAverageDiscountsByCategory(@Param("customerId") Long customerId);
    
    @Query("SELECT pc.product.id as productId, pc.prixTTC as price " +
           "FROM ProductCustomer pc " +
           "WHERE pc.customer.id = :customerId AND pc.active = true")
    List<Map<String, Object>> findProductPricesForCustomer(@Param("customerId") Long customerId);
    
    @Query("SELECT pc FROM ProductCustomer pc " +
           "WHERE pc.customer.id = :customerId " +
           "AND (LOWER(pc.product.title) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
           "OR LOWER(pc.category) LIKE LOWER(CONCAT('%', :searchTerm, '%')))")
    List<ProductCustomer> searchByCustomerIdAndTerm(@Param("customerId") Long customerId, @Param("searchTerm") String searchTerm);
}