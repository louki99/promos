package ma.foodplus.ordering.system.pos.repository;

import ma.foodplus.ordering.system.pos.domain.Sale;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface SaleRepository extends JpaRepository<Sale, Long> {
    Optional<Sale> findBySaleNumber(String saleNumber);
    List<Sale> findByStatus(SaleStatus status);
    List<Sale> findByStoreId(Long storeId);
    List<Sale> findByCashierId(Long cashierId);
    List<Sale> findByCustomerId(Long customerId);

    @Query("SELECT s FROM Sale s WHERE s.saleDate BETWEEN :startDate AND :endDate")
    List<Sale> findBySaleDateBetween(@Param("startDate") LocalDateTime startDate,
                                     @Param("endDate") LocalDateTime endDate);

    @Query("SELECT s FROM Sale s WHERE s.store.id = :storeId AND " +
            "s.saleDate BETWEEN :startDate AND :endDate")
    List<Sale> findByStoreAndDateRange(@Param("storeId") Long storeId,
                                       @Param("startDate") LocalDateTime startDate,
                                       @Param("endDate") LocalDateTime endDate);

    @Query("SELECT SUM(s.totalAmount) FROM Sale s WHERE s.store.id = :storeId AND " +
            "s.status = 'COMPLETED' AND s.saleDate BETWEEN :startDate AND :endDate")
    BigDecimal getTotalSalesByStoreAndDateRange(@Param("storeId") Long storeId,
                                                @Param("startDate") LocalDateTime startDate,
                                                @Param("endDate") LocalDateTime endDate);

    @Query("SELECT COUNT(s) FROM Sale s WHERE s.store.id = :storeId AND " +
            "s.status = 'COMPLETED' AND s.saleDate BETWEEN :startDate AND :endDate")
    Long getTransactionCountByStoreAndDateRange(@Param("storeId") Long storeId,
                                                @Param("startDate") LocalDateTime startDate,
                                                @Param("endDate") LocalDateTime endDate);

    Page<Sale> findByStoreIdOrderBySaleDateDesc(Long storeId, Pageable pageable);
}