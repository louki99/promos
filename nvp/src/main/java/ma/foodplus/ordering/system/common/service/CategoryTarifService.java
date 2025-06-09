package ma.foodplus.ordering.system.common.service;

import ma.foodplus.ordering.system.common.dto.CategoryTarifDTO;
import java.util.List;

public interface CategoryTarifService {
    CategoryTarifDTO createCategoryTarif(CategoryTarifDTO categoryTarifDTO);
    CategoryTarifDTO updateCategoryTarif(Long id, CategoryTarifDTO categoryTarifDTO);
    void deleteCategoryTarif(Long id);
    CategoryTarifDTO getCategoryTarifById(Long id);
    List<CategoryTarifDTO> getAllCategoryTarifs();
    CategoryTarifDTO getCategoryTarifByDescription(String description);
} 