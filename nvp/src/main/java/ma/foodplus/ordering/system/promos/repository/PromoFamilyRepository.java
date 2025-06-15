package ma.foodplus.ordering.system.promos.repository;

import ma.foodplus.ordering.system.promos.model.PromoFamily;
import ma.foodplus.ordering.system.promos.model.PromoFamily.PromoFamilyType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PromoFamilyRepository extends JpaRepository<PromoFamily, Long> {
    
    Optional<PromoFamily> findByCode(String code);
    
    boolean existsByCode(String code);
    
    List<PromoFamily> findByType(PromoFamilyType type);
    
    List<PromoFamily> findByTypeAndIsActiveTrue(PromoFamilyType type);
    
    @Query("SELECT pf FROM PromoFamily pf WHERE pf.type = :type AND :memberCode MEMBER OF pf.memberCodes")
    List<PromoFamily> findByTypeAndMemberCode(@Param("type") PromoFamilyType type, @Param("memberCode") String memberCode);
    
    @Query("SELECT pf FROM PromoFamily pf WHERE pf.isActive = true AND :memberCode MEMBER OF pf.memberCodes")
    List<PromoFamily> findActiveByMemberCode(@Param("memberCode") String memberCode);
} 