package ma.foodplus.ordering.system.common.service;

import ma.foodplus.ordering.system.common.model.CategoryTarif;
import java.util.List;
import java.util.Optional;

public interface CategoryTarifService {
    List<CategoryTarif> findAll();
    Optional<CategoryTarif> findById(Long id);
    CategoryTarif save(CategoryTarif categoryTarif);
    void deleteById(Long id);
    CategoryTarif update(Long id, CategoryTarif categoryTarif);
} 