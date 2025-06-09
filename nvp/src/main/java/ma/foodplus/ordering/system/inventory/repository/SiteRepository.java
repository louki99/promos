package ma.foodplus.ordering.system.inventory.repository;

import ma.foodplus.ordering.system.inventory.model.Site;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SiteRepository extends JpaRepository<Site, Long> {
    
    Optional<Site> findBySiteCode(String siteCode);
    
    boolean existsBySiteCode(String siteCode);
    
    List<Site> findByIsActive(boolean isActive);
    
    @Query("SELECT s FROM Site s WHERE s.city = :city AND s.isActive = true")
    List<Site> findActiveSitesByCity(String city);
    
    @Query("SELECT s FROM Site s WHERE s.country = :country AND s.isActive = true")
    List<Site> findActiveSitesByCountry(String country);
    
    @Query("SELECT s FROM Site s WHERE s.capacitySqm >= :minCapacity AND s.isActive = true")
    List<Site> findActiveSitesByMinCapacity(Double minCapacity);
    
    @Query("SELECT s FROM Site s WHERE LOWER(s.description) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    List<Site> searchSitesByDescription(String searchTerm);
    
    @Query("SELECT COUNT(s) FROM Site s WHERE s.isActive = true")
    long countActiveSites();
} 