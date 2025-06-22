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
    @Query("SELECT p FROM Partner p WHERE TYPE(p) = :type")
    List<Partner> findByPartnerType(@Param("type") Class<? extends Partner> type);

    @Query("SELECT p FROM Partner p WHERE TYPE(p) = :type")
    Page<Partner> findByPartnerType(@Param("type") Class<? extends Partner> type, Pageable pageable);

    @Query("SELECT COUNT(p) FROM Partner p WHERE TYPE(p) = :type")
    long countByPartnerType(@Param("type") Class<? extends Partner> type);
    
    // Status-based queries - Updated to use flattened columns
    @Query("SELECT p FROM Partner p WHERE p.isVip = true")
    List<Partner> findByIsVipTrue();
    
    @Query("SELECT COUNT(p) FROM Partner p WHERE p.isVip = true")
    long countByIsVipTrue();
    
    @Query("SELECT p FROM Partner p WHERE p.isActive = true")
    List<Partner> findByActiveTrue();
    
    @Query("SELECT COUNT(p) FROM Partner p WHERE p.isActive = true")
    long countByActiveTrue();
    
    // Credit-related queries - Updated to use flattened columns
    @Query("SELECT p FROM Partner p WHERE p.creditRating = :creditRating")
    List<Partner> findByCreditRating(@Param("creditRating") String creditRating);
    
    @Query("SELECT p FROM Partner p WHERE p.outstandingBalance > :amount")
    List<Partner> findByOutstandingBalanceGreaterThan(@Param("amount") BigDecimal amount);
    
    // Contract-related queries - Updated to use flattened columns
    @Query("SELECT p FROM Partner p WHERE TYPE(p) IN (B2BPartner, SupplierPartner) AND p.contractEndDate < :date")
    List<Partner> findByContractEndDateBefore(@Param("date") ZonedDateTime date);
    
    // Business-related queries - Updated to use flattened columns
    @Query("SELECT p FROM Partner p WHERE TYPE(p) = B2BPartner AND p.annualTurnover BETWEEN :min AND :max")
    List<Partner> findByAnnualTurnoverBetween(@Param("min") BigDecimal min, @Param("max") BigDecimal max);
    
    @Query("SELECT p FROM Partner p WHERE TYPE(p) = B2BPartner AND LOWER(p.businessActivity) LIKE LOWER(CONCAT('%', :activity, '%'))")
    List<Partner> findByBusinessActivityContainingIgnoreCase(@Param("activity") String activity);
    
    // Category tariff query
    @Query("SELECT p FROM Partner p WHERE p.cateTarif = :categoryTarifId")
    List<Partner> findByCategoryTarifId(@Param("categoryTarifId") Long categoryTarifId);
    
    // Search query - Updated to use flattened columns
    @Query("SELECT p FROM Partner p WHERE " +
           "LOWER(p.description) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(COALESCE(p.companyName, '')) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(p.ctNum) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(p.ice) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    Page<Partner> searchPartners(@Param("searchTerm") String searchTerm, Pageable pageable);
    
    // Top Partners query - Updated to use flattened columns
    @Query("SELECT p FROM Partner p ORDER BY p.totalSpent DESC")
    List<Partner> findTopByOrderByTotalSpentDesc(int limit);

    List<Partner> findByPartnerGroupsId(Long groupId);

    // Group management queries
    @Modifying
    @Query(value = "INSERT INTO partner_group_members (partner_id, group_id) " +
           "SELECT :partnerId, :groupId " +
           "WHERE NOT EXISTS (SELECT 1 FROM partner_group_members " +
           "WHERE partner_id = :partnerId AND group_id = :groupId)", nativeQuery = true)
    void addPartnerToGroup(@Param("partnerId") Long partnerId, @Param("groupId") Long groupId);

    @Query(value = "SELECT COUNT(*) > 0 FROM partner_group_members " +
           "WHERE partner_id = :partnerId AND group_id = :groupId", nativeQuery = true)
    boolean isPartnerInGroup(@Param("partnerId") Long partnerId, @Param("groupId") Long groupId);

    @Modifying
    @Query(value = "DELETE FROM partner_group_members " +
           "WHERE partner_id = :partnerId AND group_id = :groupId", nativeQuery = true)
    void removePartnerFromGroup(@Param("partnerId") Long partnerId, @Param("groupId") Long groupId);
} 