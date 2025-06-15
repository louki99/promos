package ma.foodplus.ordering.system.promos.repository;

import ma.foodplus.ordering.system.promos.model.PromotionRule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PromotionRuleRepository extends JpaRepository<PromotionRule, Long> {
} 