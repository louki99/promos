package ma.foodplus.ordering.system.pos.controller;

import ma.foodplus.ordering.system.pos.domain.Category;
import ma.foodplus.ordering.system.pos.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@Tag(name = "Category Management", description = "Endpoints for managing product categories.")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @GetMapping
    @Operation(summary = "Get all categories", description = "Retrieve a list of all categories.")
    public List<Category> getAllCategories() {
        return categoryService.getAllCategories();
    }

    @GetMapping("/active")
    @Operation(summary = "Get active categories", description = "Retrieve a list of all active categories.")
    public List<Category> getActiveCategories() {
        return categoryService.getActiveCategories();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get category by ID", description = "Retrieve a category by its unique ID.")
    public ResponseEntity<Category> getCategoryById(@PathVariable Long id) {
        return categoryService.getCategoryById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/code/{code}")
    @Operation(summary = "Get category by code", description = "Retrieve a category by its unique code.")
    public ResponseEntity<Category> getCategoryByCode(@PathVariable String code) {
        return categoryService.getCategoryByCode(code)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/parent")
    @Operation(summary = "Get parent categories", description = "Retrieve all parent categories (categories without parent).")
    public List<Category> getParentCategories() {
        return categoryService.getParentCategories();
    }

    @PostMapping
    @Operation(summary = "Create category", description = "Create a new category.")
    public Category createCategory(@RequestBody Category category) {
        return categoryService.createCategory(category);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update category", description = "Update an existing category by ID.")
    public Category updateCategory(@PathVariable Long id, @RequestBody Category category) {
        return categoryService.updateCategory(id, category);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete category", description = "Delete a category by ID.")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.noContent().build();
    }
} 