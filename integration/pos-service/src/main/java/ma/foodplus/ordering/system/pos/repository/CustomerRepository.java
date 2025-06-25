package ma.foodplus.ordering.system.pos.repository;

import ma.foodplus.ordering.system.pos.domain.Customer;
import ma.foodplus.ordering.system.pos.enums.CustomerType;
import ma.foodplus.ordering.system.pos.enums.LoyaltyTier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    Optional<Customer> findByEmail(String email);
    Optional<Customer> findByPhone(String phone);
    List<Customer> findByActiveTrue();
    List<Customer> findByCustomerType(CustomerType customerType);
    List<Customer> findByLoyaltyTier(LoyaltyTier loyaltyTier);

    @Query("SELECT c FROM Customer c WHERE c.active = true AND " +
            "(LOWER(c.firstName) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "LOWER(c.lastName) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "c.phone LIKE CONCAT('%', :search, '%') OR " +
            "LOWER(c.email) LIKE LOWER(CONCAT('%', :search, '%')))")
    Page<Customer> searchActiveCustomers(@Param("search") String search, Pageable pageable);

    @Query("SELECT c FROM Customer c WHERE c.loyaltyPoints >= :minPoints")
    List<Customer> findByLoyaltyPointsGreaterThanEqual(@Param("minPoints") Integer minPoints);
}