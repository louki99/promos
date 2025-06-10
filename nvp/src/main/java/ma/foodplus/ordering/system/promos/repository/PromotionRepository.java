package ma.foodplus.ordering.system.promos.repository;

import ma.foodplus.ordering.system.promos.model.Promotion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface PromotionRepository extends JpaRepository<Promotion, Integer> {
    
    /**
     * Finds all active promotions at the given time, sorted by priority.
     */
    @Query("SELECT p FROM Promotion p WHERE p.startDate <= :now AND (p.endDate IS NULL OR p.endDate > :now) ORDER BY p.priority DESC")
    List<Promotion> findActivePromotions(@Param("now") ZonedDateTime now);

    /**
     * Finds a promotion by its code.
     */
    Optional<Promotion> findByPromoCode(String promoCode);
    
    /**
     * Finds all active promotions that are eligible for a given customer.
     */
    @Query("SELECT p FROM Promotion p WHERE p.startDate <= :now AND (p.endDate IS NULL OR p.endDate > :now) " +
           "AND (p.customerGroup IS NULL OR p.customerGroup = :customerGroup) " +
           "ORDER BY p.priority DESC")
    List<Promotion> findActivePromotionsForCustomer(@Param("now") ZonedDateTime now, @Param("customerGroup") String customerGroup);
}
