package ma.foodplus.ordering.system.promos.repository;

import ma.foodplus.ordering.system.promos.model.PromotionTier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PromotionTierRepository extends JpaRepository<PromotionTier, Long> {
} 