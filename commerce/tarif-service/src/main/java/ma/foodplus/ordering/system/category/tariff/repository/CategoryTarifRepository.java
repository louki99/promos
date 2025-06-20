package ma.foodplus.ordering.system.category.tariff.repository;

import ma.foodplus.ordering.system.category.tariff.domain.CategoryTarif;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryTarifRepository extends JpaRepository<CategoryTarif, Long> {

    @Query ("select c from CategoryTarif c where c.active = true")
    Optional<CategoryTarif> getListCategoryTarifActive();

    Optional<CategoryTarif> findByCode(String code);

    @Query("SELECT c FROM CategoryTarif c WHERE " +
            "LOWER(c.name) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(c.description) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(c.code) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<CategoryTarif> searchByKeyword(@Param ("keyword") String keyword);
} 