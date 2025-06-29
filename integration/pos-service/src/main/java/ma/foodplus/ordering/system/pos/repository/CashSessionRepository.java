package ma.foodplus.ordering.system.pos.repository;

import ma.foodplus.ordering.system.pos.domain.CashSession;
import ma.foodplus.ordering.system.pos.enums.CashSessionStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CashSessionRepository extends JpaRepository<CashSession, UUID> {
    
    // Find open session for a specific cashier
    Optional<CashSession> findByCashierIdAndStatus(Long cashierId, CashSessionStatus status);
    
    // Find open session for a specific cashier and store
    Optional<CashSession> findByCashierIdAndStoreIdAndStatus(Long cashierId, Long storeId, CashSessionStatus status);
    
    // Find open session for a specific cashier, store, and terminal
    Optional<CashSession> findByCashierIdAndStoreIdAndTerminalIdAndStatus(
        Long cashierId, Long storeId, Long terminalId, CashSessionStatus status);
    
    // Find all sessions for a cashier
    List<CashSession> findByCashierIdOrderByOpenedAtDesc(Long cashierId);
    
    // Find all sessions for a store
    List<CashSession> findByStoreIdOrderByOpenedAtDesc(Long storeId);
    
    // Find all sessions for a terminal
    List<CashSession> findByTerminalIdOrderByOpenedAtDesc(Long terminalId);
    
    // Find sessions by date range
    @Query("SELECT cs FROM CashSession cs WHERE cs.openedAt BETWEEN :startDate AND :endDate")
    List<CashSession> findByDateRange(@Param("startDate") LocalDateTime startDate, 
                                     @Param("endDate") LocalDateTime endDate);
    
    // Find sessions by store and date range
    @Query("SELECT cs FROM CashSession cs WHERE cs.store.id = :storeId AND cs.openedAt BETWEEN :startDate AND :endDate")
    List<CashSession> findByStoreAndDateRange(@Param("storeId") Long storeId,
                                             @Param("startDate") LocalDateTime startDate,
                                             @Param("endDate") LocalDateTime endDate);
    
    // Find all open sessions
    List<CashSession> findByStatus(CashSessionStatus status);
    
    // Check if cashier has open session
    boolean existsByCashierIdAndStatus(Long cashierId, CashSessionStatus status);
    
    // Check if terminal has open session
    boolean existsByTerminalIdAndStatus(Long terminalId, CashSessionStatus status);
} 