package ma.foodplus.ordering.system.product.service;

import ma.foodplus.ordering.system.product.dto.category.CategoryDTO;
import ma.foodplus.ordering.system.product.dto.category.CreateCategoryCommand;
import ma.foodplus.ordering.system.product.dto.category.UpdateCategoryCommand;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

public interface CategoryService {
    CategoryDTO createCategory(CreateCategoryCommand command);
    CategoryDTO updateCategory(Long id, UpdateCategoryCommand command);
    void deleteCategory(Long id);
    CategoryDTO getCategoryById(Long id);
    Page<CategoryDTO> getAllCategories(Pageable pageable);
    List<CategoryDTO> getCategoryTree();
    CategoryDTO getCategoryByCode(String code);
    List<CategoryDTO> getSubcategories(Long parentId);
    List<CategoryDTO> getCategoriesByLevel(Integer level);
    List<CategoryDTO> getActiveCategories();
    void activateCategory(Long id);
    void deactivateCategory(Long id);
    Page<CategoryDTO> searchCategories(String searchTerm, Pageable pageable);
    Page<CategoryDTO> getCategoryProducts(Long id, Pageable pageable);
    Map<String, Object> getCategoryStatistics();
} 