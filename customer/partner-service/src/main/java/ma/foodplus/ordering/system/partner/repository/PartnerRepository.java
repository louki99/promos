package ma.foodplus.ordering.system.partner.repository;

import ma.foodplus.ordering.system.partner.domain.Partner;
import ma.foodplus.ordering.system.partner.domain.PartnerType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface PartnerRepository extends JpaRepository<Partner, Long> {
    
    // Basic queries
    boolean existsByCtNum(String ctNum);
    boolean existsByIce(String ice);
    Optional<Partner> findByCtNum(String ctNum);
    Optional<Partner> findByIce(String ice);
    
    // Type-based queries
    List<Partner> findByPartnerType(PartnerType type);
    long countByPartnerType(PartnerType type);
    
    // Status-based queries
    List<Partner> findByIsVipTrue();
    long countByIsVipTrue();
    List<Partner> findByActiveTrue();
    long countByActiveTrue();
    
    // Credit-related queries
    List<Partner> findByCreditRating(String creditRating);
    List<Partner> findByOutstandingBalanceGreaterThan(BigDecimal amount);
    
    // Contract-related queries
    List<Partner> findByContractEndDateBefore(ZonedDateTime date);
    
    // Business-related queries
    List<Partner> findByAnnualTurnoverBetween(BigDecimal min, BigDecimal max);
    List<Partner> findByBusinessActivityContainingIgnoreCase(String activity);
    
    // Category tariff query
    @Query("SELECT c FROM Partner c WHERE c.cateTarif = :categoryTarifId")
    List<Partner> findByCategoryTarifId(@Param("categoryTarifId") Long categoryTarifId);
    
    // Search query
    @Query("SELECT c FROM Partner c WHERE " +
           "LOWER(c.description) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(c.companyName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(c.ctNum) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(c.ice) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    Page<Partner> searchPartners(@Param("searchTerm") String searchTerm, Pageable pageable);
    
    // Top Partners query
    @Query("SELECT c FROM Partner c ORDER BY c.totalSpent DESC")
    List<Partner> findTopByOrderByTotalSpentDesc(int limit);
    
    // Product preferences query

    List<Partner> findByPartnerGroupsId(Long groupId);

    // Group management queries
    @Modifying
    @Query(value = "INSERT INTO partner_group_members (Partner_id, group_id) " +
           "SELECT :PartnerId, :groupId " +
           "WHERE NOT EXISTS (SELECT 1 FROM Partner_group_members " +
           "WHERE Partner_id = :PartnerId AND group_id = :groupId)", nativeQuery = true)
    void addPartnerToGroup(@Param("PartnerId") Long PartnerId, @Param("groupId") Long groupId);

    @Query(value = "SELECT COUNT(*) > 0 FROM partner_group_members " +
           "WHERE Partner_id = :PartnerId AND group_id = :groupId", nativeQuery = true)
    boolean isPartnerInGroup(@Param("PartnerId") Long PartnerId, @Param("groupId") Long groupId);

    @Modifying
    @Query(value = "DELETE FROM partner_group_members " +
           "WHERE Partner_id = :partnerId AND group_id = :groupId", nativeQuery = true)
    void removePartnerFromGroup(@Param("PartnerId") Long PartnerId, @Param("groupId") Long groupId);
} 