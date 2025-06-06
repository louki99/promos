package ma.foodplus.ordering.system.product.service.domain.ports.output.repository;

import ma.foodplus.ordering.system.product.service.domain.entity.Category;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CategoryRepository{

    Category createCategory(Category category);

    Optional<Category> findCategoryById(UUID categoryId);

    void deleteCategory(Category category);

    List<Category> getListCategory(int page, int size, String sortBy, String sortDirection);
}
