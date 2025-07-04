package ma.foodplus.ordering.system.promos.repository;

import ma.foodplus.ordering.system.promos.model.Condition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConditionRepository extends JpaRepository<Condition, Long> {
} 