package ma.foodplus.ordering.system.pos.repository;

import ma.foodplus.ordering.system.pos.domain.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InventoryRepository extends JpaRepository<Inventory, Long> {
    Optional<Inventory> findByProductIdAndStoreId(Long productId, Long storeId);
    List<Inventory> findByStoreId(Long storeId);
    List<Inventory> findByProductId(Long productId);

    @Query("SELECT i FROM Inventory i WHERE i.store.id = :storeId AND " +
            "i.product.minStockLevel IS NOT NULL AND i.quantity <= i.product.minStockLevel")
    List<Inventory> findLowStockItems(@Param("storeId") Long storeId);

    @Query("SELECT i FROM Inventory i WHERE i.store.id = :storeId AND " +
            "i.product.maxStockLevel IS NOT NULL AND i.quantity >= i.product.maxStockLevel")
    List<Inventory> findOverStockItems(@Param("storeId") Long storeId);

    @Query("SELECT i FROM Inventory i WHERE i.store.id = :storeId AND i.quantity > 0")
    List<Inventory> findAvailableInventoryByStore(@Param("storeId") Long storeId);
}