package ma.foodplus.ordering.system.promos.repository;

import ma.foodplus.ordering.system.promos.model.Promotion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface PromotionRepository extends JpaRepository<Promotion,Long>{

    List<Promotion> findActivePromotionsSortedByPriority(LocalDate date);
}
