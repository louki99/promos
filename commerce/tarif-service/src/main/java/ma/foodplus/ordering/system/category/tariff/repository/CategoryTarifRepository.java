package ma.foodplus.ordering.system.category.tariff.repository;

import ma.foodplus.ordering.system.category.tariff.domain.CategoryTarif;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CategoryTarifRepository extends JpaRepository<CategoryTarif, Long>, JpaSpecificationExecutor<CategoryTarif>{

    @Query ("select c from CategoryTarif c where c.active = true")
    Optional<CategoryTarif> getListCategoryTarifActive();

    Optional<CategoryTarif> findByCode(String code);
} 