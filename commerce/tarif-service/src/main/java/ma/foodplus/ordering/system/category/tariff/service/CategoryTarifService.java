package ma.foodplus.ordering.system.category.tariff.service;

import ma.foodplus.ordering.system.category.tariff.domain.CategoryTarif;
import org.springframework.data.jpa.domain.Specification;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public interface CategoryTarifService {
    Optional<CategoryTarif> findById(Long id);
    Optional<CategoryTarif> findByCode(String code);
    Optional<CategoryTarif> findActiveCategoryTarif();
    CategoryTarif save(CategoryTarif categoryTarif);
    void deleteById(Long id);
    CategoryTarif update(Long id, CategoryTarif categoryTarif);

    CategoryTarif toggle(Long id);

    List<CategoryTarif> search(Specification<CategoryTarif> categoryTarifSpecification);

    List<CategoryTarif> findAll();
}