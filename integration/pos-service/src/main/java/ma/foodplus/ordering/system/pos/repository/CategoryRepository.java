package ma.foodplus.ordering.system.pos.repository;

import ma.foodplus.ordering.system.pos.domain.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    Optional<Category> findByCode(String code);
    List<Category> findByActiveTrue();
    List<Category> findByParentCategoryIsNull();
    List<Category> findByParentCategoryId(Long parentId);

    @Query("SELECT c FROM Category c WHERE c.active = true AND c.parentCategory IS NULL")
    List<Category> findActiveParentCategories();

    @Query("SELECT c FROM Category c WHERE c.active = true AND c.parentCategory.id = :parentId")
    List<Category> findActiveSubCategories(Long parentId);

    boolean existsByCode(String code);
}