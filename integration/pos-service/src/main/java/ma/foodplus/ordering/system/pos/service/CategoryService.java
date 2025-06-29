package ma.foodplus.ordering.system.pos.service;

import ma.foodplus.ordering.system.pos.domain.Category;
import ma.foodplus.ordering.system.pos.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CategoryService{

    @Autowired
    private CategoryRepository categoryRepository;

    public List<Category> getAllCategories(){
        return categoryRepository.findAll();
    }

    public List<Category> getActiveCategories(){
        return categoryRepository.findByActiveTrue();
    }

    public Optional<Category> getCategoryById(Long id){
        return categoryRepository.findById(id);
    }

    public Optional<Category> getCategoryByCode(String code){
        return categoryRepository.findByCode(code);
    }

    public List<Category> getParentCategories(){
        return categoryRepository.findActiveParentCategories();
    }

    public Category createCategory(Category category) {
        if (categoryRepository.existsByCode(category.getCode())) {
            throw new RuntimeException("Category code already exists");
        }
        category.setCreatedAt(java.time.LocalDateTime.now());
        category.setUpdatedAt(java.time.LocalDateTime.now());
        return categoryRepository.save(category);
    }

    public Category updateCategory(Long id, Category categoryDetails) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found"));
        
        // Check if code is being changed and if it already exists
        if (!category.getCode().equals(categoryDetails.getCode()) && 
            categoryRepository.existsByCode(categoryDetails.getCode())) {
            throw new RuntimeException("Category code already exists");
        }
        
        category.setName(categoryDetails.getName());
        category.setCode(categoryDetails.getCode());
        category.setDescription(categoryDetails.getDescription());
        category.setParentCategory(categoryDetails.getParentCategory());
        category.setActive(categoryDetails.isActive());
        category.setUpdatedAt(java.time.LocalDateTime.now());
        
        return categoryRepository.save(category);
    }

    public void deleteCategory(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found"));
        
        // Check if category has subcategories
        List<Category> subCategories = categoryRepository.findActiveSubCategories(id);
        if (!subCategories.isEmpty()) {
            throw new RuntimeException("Cannot delete category with subcategories");
        }
        
        categoryRepository.deleteById(id);
    }
}