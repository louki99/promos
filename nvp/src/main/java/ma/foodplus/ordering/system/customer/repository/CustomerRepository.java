package ma.foodplus.ordering.system.customer.repository;

import ma.foodplus.ordering.system.customer.model.Customer;
import ma.foodplus.ordering.system.customer.model.CustomerType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    
    // Basic queries
    boolean existsByCtNum(String ctNum);
    boolean existsByIce(String ice);
    Optional<Customer> findByCtNum(String ctNum);
    Optional<Customer> findByIce(String ice);
    
    // Type-based queries
    List<Customer> findByCustomerType(CustomerType type);
    long countByCustomerType(CustomerType type);
    
    // Status-based queries
    List<Customer> findByIsVipTrue();
    long countByIsVipTrue();
    List<Customer> findByActiveTrue();
    long countByActiveTrue();
    
    // Credit-related queries
    List<Customer> findByCreditRating(String creditRating);
    List<Customer> findByOutstandingBalanceGreaterThan(BigDecimal amount);
    
    // Contract-related queries
    List<Customer> findByContractEndDateBefore(ZonedDateTime date);
    
    // Business-related queries
    List<Customer> findByAnnualTurnoverBetween(BigDecimal min, BigDecimal max);
    List<Customer> findByBusinessActivityContainingIgnoreCase(String activity);
    
    // Category tariff query
    @Query("SELECT c FROM Customer c WHERE c.cateTarif.id = :categoryTarifId")
    List<Customer> findByCategoryTarifId(@Param("categoryTarifId") Long categoryTarifId);
    
    // Search query
    @Query("SELECT c FROM Customer c WHERE " +
           "LOWER(c.description) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(c.companyName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(c.ctNum) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(c.ice) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    Page<Customer> searchCustomers(@Param("searchTerm") String searchTerm, Pageable pageable);
    
    // Top customers query
    @Query("SELECT c FROM Customer c ORDER BY c.totalSpent DESC")
    List<Customer> findTopByOrderByTotalSpentDesc(int limit);
    
    // Product preferences query
    @Query("SELECT DISTINCT c FROM Customer c " +
           "JOIN ProductCustomer pc ON pc.customer = c " +
           "WHERE pc.product.id = :productId")
    List<Customer> findByProductPreferences(@Param("productId") Long productId);

    List<Customer> findByCustomerGroupsId(Long groupId);

    // Group management queries
    @Modifying
    @Query(value = "INSERT INTO customer_group_members (customer_id, group_id) " +
           "SELECT :customerId, :groupId " +
           "WHERE NOT EXISTS (SELECT 1 FROM customer_group_members " +
           "WHERE customer_id = :customerId AND group_id = :groupId)", nativeQuery = true)
    void addCustomerToGroup(@Param("customerId") Long customerId, @Param("groupId") Long groupId);

    @Query(value = "SELECT COUNT(*) > 0 FROM customer_group_members " +
           "WHERE customer_id = :customerId AND group_id = :groupId", nativeQuery = true)
    boolean isCustomerInGroup(@Param("customerId") Long customerId, @Param("groupId") Long groupId);

    @Modifying
    @Query(value = "DELETE FROM customer_group_members " +
           "WHERE customer_id = :customerId AND group_id = :groupId", nativeQuery = true)
    void removeCustomerFromGroup(@Param("customerId") Long customerId, @Param("groupId") Long groupId);
} 