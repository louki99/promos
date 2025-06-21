package ma.foodplus.ordering.system.partner.repository;

import ma.foodplus.ordering.system.partner.domain.PartnerGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PartnerGroupRepository extends JpaRepository<PartnerGroup, Long> {
    Optional<PartnerGroup> findByName(String name);
    boolean existsByName(String name);
} 