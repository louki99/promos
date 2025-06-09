package ma.foodplus.ordering.system.common.repository;

import ma.foodplus.ordering.system.common.model.CategoryTarif;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CategoryTarifRepository extends JpaRepository<CategoryTarif, Long> {
    Optional<CategoryTarif> findByDescription(String description);
    boolean existsByDescription(String description);
} 