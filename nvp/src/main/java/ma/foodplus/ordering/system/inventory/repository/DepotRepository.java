package ma.foodplus.ordering.system.inventory.repository;

import ma.foodplus.ordering.system.inventory.model.Depot;
import ma.foodplus.ordering.system.inventory.model.Depot.DepotType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DepotRepository extends JpaRepository<Depot, Long> {
    
    Optional<Depot> findByDepotCode(String depotCode);
    
    boolean existsByDepotCode(String depotCode);
    
    List<Depot> findByIsActive(boolean isActive);
    
    List<Depot> findByDepotType(DepotType depotType);
    
    @Query("SELECT d FROM Depot d WHERE d.site.id = :siteId AND d.isActive = true")
    List<Depot> findActiveDepotsBySiteId(Long siteId);
    
    @Query("SELECT d FROM Depot d WHERE d.isRefrigerated = true AND d.isActive = true")
    List<Depot> findActiveRefrigeratedDepots();
    
    @Query("SELECT d FROM Depot d WHERE d.capacityCubicMeters >= :minCapacity AND d.isActive = true")
    List<Depot> findActiveDepotsByMinCapacity(Double minCapacity);
    
    @Query("SELECT d FROM Depot d WHERE d.securityLevel >= :minSecurityLevel AND d.isActive = true")
    List<Depot> findActiveDepotsByMinSecurityLevel(Integer minSecurityLevel);
    
    @Query("SELECT d FROM Depot d WHERE LOWER(d.description) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    List<Depot> searchDepotsByDescription(String searchTerm);
    
    @Query("SELECT COUNT(d) FROM Depot d WHERE d.isActive = true")
    long countActiveDepots();
    
    @Query("SELECT COUNT(d) FROM Depot d WHERE d.depotType = :depotType AND d.isActive = true")
    long countActiveDepotsByType(DepotType depotType);
} 