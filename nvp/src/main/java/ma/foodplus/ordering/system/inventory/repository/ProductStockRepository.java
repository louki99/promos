package ma.foodplus.ordering.system.inventory.repository;

import ma.foodplus.ordering.system.inventory.model.ProductStock;
import ma.foodplus.ordering.system.inventory.model.ProductStock.QualityStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProductStockRepository extends JpaRepository<ProductStock, Long> {
    
    @Query("SELECT ps FROM ProductStock ps WHERE ps.productId = :productId")
    List<ProductStock> findByProductId(Long productId);
    
    @Query("SELECT ps FROM ProductStock ps WHERE ps.depot.id = :depotId")
    List<ProductStock> findByDepotId(Long depotId);
    
    @Query("SELECT ps FROM ProductStock ps WHERE ps.productId = :productId AND ps.depot.id = :depotId")
    Optional<ProductStock> findByProductIdAndDepotId(Long productId, Long depotId);
    
    @Query("SELECT ps FROM ProductStock ps WHERE ps.quantity <= ps.minStockLevel")
    List<ProductStock> findLowStockItems();
    
    @Query("SELECT ps FROM ProductStock ps WHERE ps.expiryDate <= :date AND ps.qualityStatus != 'EXPIRED'")
    List<ProductStock> findExpiringItems(ZonedDateTime date);
    
    @Query("SELECT ps FROM ProductStock ps WHERE ps.qualityStatus = :status")
    List<ProductStock> findByQualityStatus(QualityStatus status);
    
    @Query("SELECT ps FROM ProductStock ps WHERE ps.batchNumber = :batchNumber")
    List<ProductStock> findByBatchNumber(String batchNumber);
    
    @Query("SELECT ps FROM ProductStock ps WHERE ps.locationCode = :locationCode")
    List<ProductStock> findByLocationCode(String locationCode);
    
    @Query("SELECT ps FROM ProductStock ps WHERE ps.daysOfStock <= :days")
    List<ProductStock> findItemsWithLowDaysOfStock(Integer days);
    
    @Query("SELECT ps FROM ProductStock ps WHERE ps.quantity * ps.unitCost >= :minValue")
    List<ProductStock> findItemsByMinValue(BigDecimal minValue);
    
    @Query("SELECT SUM(ps.quantity * ps.unitCost) FROM ProductStock ps WHERE ps.depot.id = :depotId")
    BigDecimal calculateTotalValueByDepot(Long depotId);
    
    @Query("SELECT COUNT(ps) FROM ProductStock ps WHERE ps.qualityStatus = :status")
    long countByQualityStatus(QualityStatus status);
    
    @Query("SELECT ps FROM ProductStock ps WHERE ps.lastPurchaseDate <= :date")
    List<ProductStock> findItemsNotPurchasedSince(ZonedDateTime date);
    
    @Query("SELECT ps FROM ProductStock ps WHERE ps.lastSaleDate <= :date")
    List<ProductStock> findItemsNotSoldSince(ZonedDateTime date);
    
    @Query("SELECT ps FROM ProductStock ps WHERE ps.quantity > 0 AND ps.qualityStatus = 'GOOD'")
    List<ProductStock> findAvailableGoodQualityItems();
    
    @Query("SELECT ps FROM ProductStock ps WHERE ps.reservedQuantity > 0")
    List<ProductStock> findReservedItems();
    
    @Query("SELECT ps FROM ProductStock ps WHERE ps.storageConditions = :condition")
    List<ProductStock> findByStorageCondition(String condition);
    
    @Query("SELECT ps FROM ProductStock ps WHERE ps.qualityStatus = :status AND ps.depot.id = :depotId")
    List<ProductStock> findByQualityStatusAndDepotId(@Param("status") QualityStatus status, @Param("depotId") Long depotId);
    
    @Query("SELECT ps FROM ProductStock ps WHERE ps.qualityStatus = :status AND ps.productId = :productId")
    List<ProductStock> findByQualityStatusAndProductId(@Param("status") QualityStatus status, @Param("productId") Long productId);
    
    @Query("SELECT ps FROM ProductStock ps WHERE ps.expiryDate < :date")
    List<ProductStock> findProductsExpiringBefore(@Param("date") LocalDate date);
    
    @Query("SELECT ps FROM ProductStock ps WHERE " +
           "LOWER(ps.notes) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(ps.qualityNotes) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    List<ProductStock> searchProductStocks(@Param("searchTerm") String searchTerm);

    /**
     * Find all stock entries for a product with quantity greater than the specified value
     */
    List<ProductStock> findByProductIdAndQuantityGreaterThan(Long productId, BigDecimal minQuantity);

    /**
     * Find the first stock entry for a product ordered by quantity ascending
     */
    ProductStock findFirstByProductIdOrderByQuantityAsc(Long productId);

    /**
     * Find the first stock entry for a product ordered by last updated descending
     */
    ProductStock findFirstByProductIdOrderByUpdatedAtDesc(Long productId);
} 