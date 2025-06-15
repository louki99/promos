package ma.foodplus.ordering.system.promos.repository;

import ma.foodplus.ordering.system.promos.model.Reward;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RewardRepository extends JpaRepository<Reward, Long> {
} 