package ma.foodplus.ordering.system.pos.repository;

import ma.foodplus.ordering.system.pos.domain.Partner;
import ma.foodplus.ordering.system.pos.enums.PartnerType;
import ma.foodplus.ordering.system.pos.enums.LoyaltyTier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PartnerRepository extends JpaRepository<Partner, Long> {
    Optional<Partner> findByEmail(String email);
    Optional<Partner> findByPhone(String phone);
    List<Partner> findByActiveTrue();
    List<Partner> findByPartnerType(PartnerType partnerType);
    List<Partner> findByLoyaltyTier(LoyaltyTier loyaltyTier);

    @Query("SELECT c FROM Partner c WHERE c.active = true AND " +
            "(LOWER(c.firstName) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "LOWER(c.lastName) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "c.phone LIKE CONCAT('%', :search, '%') OR " +
            "LOWER(c.email) LIKE LOWER(CONCAT('%', :search, '%')))")
    Page<Partner> searchActiveCustomers(@Param("search") String search,Pageable pageable);

    @Query("SELECT c FROM Partner c WHERE c.loyaltyPoints >= :minPoints")
    List<Partner> findByLoyaltyPointsGreaterThanEqual(@Param("minPoints") Integer minPoints);
}