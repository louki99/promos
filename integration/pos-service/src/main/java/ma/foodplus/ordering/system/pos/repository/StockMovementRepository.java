package ma.foodplus.ordering.system.pos.repository;

import ma.foodplus.ordering.system.pos.domain.StockMovement;
import ma.foodplus.ordering.system.pos.enums.StockMovementType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StockMovementRepository extends JpaRepository<StockMovement, Long> {
    List<StockMovement> findByProductId(Long productId);
    List<StockMovement> findByStoreId(Long storeId);
    List<StockMovement> findByMovementType(StockMovementType movementType);
} 