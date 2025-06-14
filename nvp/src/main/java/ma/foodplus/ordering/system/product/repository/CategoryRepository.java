package ma.foodplus.ordering.system.product.repository;

import ma.foodplus.ordering.system.product.model.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    
    Optional<Category> findByCode(String code);
    
    boolean existsByCode(String code);
    
    List<Category> findByParentIsNull();
    
    List<Category> findByLevel(Integer level);
    
    List<Category> findByIsActiveTrue();
    
    long countByIsActiveTrue();
    
    long countByParentIsNull();
    
    Page<Category> findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(
        String name, String description, Pageable pageable);
    
    @Query("SELECT c FROM Category c JOIN c.products p WHERE c = :category")
    Page<Category> findProductsByCategory(Category category, Pageable pageable);
} 