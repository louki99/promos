package ma.foodplus.ordering.system.product.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ma.foodplus.ordering.system.product.dto.category.CategoryDTO;
import ma.foodplus.ordering.system.product.dto.category.CreateCategoryCommand;
import ma.foodplus.ordering.system.product.dto.category.UpdateCategoryCommand;
import ma.foodplus.ordering.system.product.mapper.CategoryMapper;
import ma.foodplus.ordering.system.product.model.Category;
import ma.foodplus.ordering.system.product.repository.CategoryRepository;
import ma.foodplus.ordering.system.product.service.CategoryService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @Override
    public CategoryDTO createCategory(CreateCategoryCommand command) {
        log.info("Creating new category with command: {}", command);

        validateCategoryCode(command.code());

        Category category = new Category();
        category.setCode(command.code());
        category.setName(command.name());
        category.setDescription(command.description());

        if (command.parentId() != null) {
            Category parent = categoryRepository.findById(command.parentId())
                    .orElseThrow(() -> new RuntimeException("Parent category not found"));
            category.setParent(parent);
            category.setLevel(parent.getLevel() + 1); // calculate level based on parent
        } else {
            category.setLevel(0); // set level to 0 for root category
        }

        category.setActive(true);
        category = categoryRepository.save(category);
        return categoryMapper.toDTO(category);
    }

    @Override
    public CategoryDTO updateCategory(Long id, UpdateCategoryCommand command) {
        log.info("Updating category {} with command: {}", id, command);
        
        Category category = categoryRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Category not found"));
        
        if (command.name() != null) {
            category.setName(command.name());
        }
        if (command.description() != null) {
            category.setDescription(command.description());
        }
        if (command.level() != null) {
            category.setLevel(command.level());
        }
        if (command.parentId() != null) {
            Category parent = categoryRepository.findById(command.parentId())
                .orElseThrow(() -> new RuntimeException("Parent category not found"));
            category.setParent(parent);
        }
        
        category = categoryRepository.save(category);
        return categoryMapper.toDTO(category);
    }

    @Override
    public void deleteCategory(Long id) {
        log.info("Deleting category: {}", id);
        Category category = categoryRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Category not found"));
        
        if (!category.getChildren().isEmpty()) {
            throw new RuntimeException("Cannot delete category with subcategories");
        }
        if (!category.getProducts().isEmpty()) {
            throw new RuntimeException("Cannot delete category with associated products");
        }
        
        categoryRepository.delete(category);
    }

    @Override
    @Transactional(readOnly = true)
    public CategoryDTO getCategoryById(Long id) {
        log.info("Getting category by id: {}", id);
        Category category = categoryRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Category not found"));
        return categoryMapper.toDTO(category);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CategoryDTO> getAllCategories(Pageable pageable) {
        log.info("Getting all categories with pagination");
        return categoryRepository.findAll(pageable)
            .map(categoryMapper::toDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CategoryDTO> getCategoryTree() {
        log.info("Getting category tree");
        List<Category> rootCategories = categoryRepository.findByParentIsNull();
        return rootCategories.stream()
            .map(this::buildCategoryTree)
            .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public CategoryDTO getCategoryByCode(String code) {
        log.info("Getting category by code: {}", code);
        Category category = categoryRepository.findByCode(code)
            .orElseThrow(() -> new RuntimeException("Category not found"));
        return categoryMapper.toDTO(category);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CategoryDTO> getSubcategories(Long parentId) {
        log.info("Getting subcategories for parent: {}", parentId);
        Category parent = categoryRepository.findById(parentId)
            .orElseThrow(() -> new RuntimeException("Parent category not found"));
        return parent.getChildren().stream()
            .map(categoryMapper::toDTO)
            .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<CategoryDTO> getCategoriesByLevel(Integer level) {
        log.info("Getting categories by level: {}", level);
        return categoryRepository.findByLevel(level).stream()
            .map(categoryMapper::toDTO)
            .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<CategoryDTO> getActiveCategories() {
        log.info("Getting active categories");
        return categoryRepository.findByIsActiveTrue().stream()
            .map(categoryMapper::toDTO)
            .collect(Collectors.toList());
    }

    @Override
    public void activateCategory(Long id) {
        log.info("Activating category: {}", id);
        Category category = categoryRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Category not found"));
        category.setActive(true);
        categoryRepository.save(category);
    }

    @Override
    public void deactivateCategory(Long id) {
        log.info("Deactivating category: {}", id);
        Category category = categoryRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Category not found"));
        category.setActive(false);
        categoryRepository.save(category);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CategoryDTO> searchCategories(String searchTerm, Pageable pageable) {
        log.info("Searching categories with term: {}", searchTerm);
        return categoryRepository.findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(
                searchTerm, searchTerm, pageable)
            .map(categoryMapper::toDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CategoryDTO> getCategoryProducts(Long id, Pageable pageable) {
        log.info("Getting products for category: {}", id);
        Category category = categoryRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Category not found"));
        return categoryRepository.findProductsByCategory(category, pageable)
            .map(categoryMapper::toDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public Map<String, Object> getCategoryStatistics() {
        log.info("Getting category statistics");
        Map<String, Object> statistics = new HashMap<>();
        
        statistics.put("totalCategories", categoryRepository.count());
        statistics.put("activeCategories", categoryRepository.countByIsActiveTrue());
        statistics.put("rootCategories", categoryRepository.countByParentIsNull());
        
        Map<Integer, Long> categoriesByLevel = categoryRepository.findAll().stream()
            .collect(Collectors.groupingBy(
                Category::getLevel,
                Collectors.counting()
            ));
        statistics.put("categoriesByLevel", categoriesByLevel);
        
        Map<Long, Long> productsPerCategory = categoryRepository.findAll().stream()
            .collect(Collectors.toMap(
                Category::getId,
                category -> (long) category.getProducts().size()
            ));
        statistics.put("productsPerCategory", productsPerCategory);
        
        return statistics;
    }

    private CategoryDTO buildCategoryTree(Category category) {
        CategoryDTO dto = categoryMapper.toDTO(category);
        if (!category.getChildren().isEmpty()) {
            List<CategoryDTO> children = category.getChildren().stream()
                .map(this::buildCategoryTree)
                .collect(Collectors.toList());
            // Since CategoryDTO is a record, we need to create a new instance with children
            return new CategoryDTO(
                dto.id(), dto.code(), dto.name(), dto.description(),
                dto.level(), dto.parentId(), children, dto.isActive(),
                dto.createdAt(), dto.updatedAt()
            );
        }
        return dto;
    }

    private void validateCategoryCode(String code) {
        if (categoryRepository.existsByCode(code)) {
            throw new RuntimeException("Category code already exists");
        }
    }
} 