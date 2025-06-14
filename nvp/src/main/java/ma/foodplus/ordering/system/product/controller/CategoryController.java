package ma.foodplus.ordering.system.product.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ma.foodplus.ordering.system.common.dto.ErrorResponse;
import ma.foodplus.ordering.system.product.dto.category.CategoryDTO;
import ma.foodplus.ordering.system.product.dto.category.CreateCategoryCommand;
import ma.foodplus.ordering.system.product.dto.category.UpdateCategoryCommand;
import ma.foodplus.ordering.system.product.service.CategoryService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
@Tag(name = "Category Management", description = "APIs for managing product categories")
public class CategoryController {

    private final CategoryService categoryService;

    @PostMapping
    @Operation(summary = "Create a new category", description = "Creates a new product category.")
    @ApiResponse(responseCode = "201", description = "Category created successfully", content = @Content(schema = @Schema(implementation = CategoryDTO.class)))
    @ApiResponse(responseCode = "400", description = "Invalid category data", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    public ResponseEntity<CategoryDTO> createCategory(@Valid @RequestBody CreateCategoryCommand command) {
        log.info("Creating new category: {}", command);
        return new ResponseEntity<>(categoryService.createCategory(command), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a category", description = "Updates an existing category by ID.")
    @ApiResponse(responseCode = "200", description = "Category updated successfully", content = @Content(schema = @Schema(implementation = CategoryDTO.class)))
    @ApiResponse(responseCode = "404", description = "Category not found", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    public ResponseEntity<CategoryDTO> updateCategory(
            @Parameter(description = "Category ID", required = true) @PathVariable Long id,
            @Valid @RequestBody UpdateCategoryCommand command) {
        log.info("Updating category with id {}: {}", id, command);
        return ResponseEntity.ok(categoryService.updateCategory(id, command));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a category", description = "Deletes a category by ID.")
    @ApiResponse(responseCode = "204", description = "Category deleted successfully")
    @ApiResponse(responseCode = "404", description = "Category not found", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    public ResponseEntity<Void> deleteCategory(@Parameter(description = "Category ID", required = true) @PathVariable Long id) {
        log.info("Deleting category with id: {}", id);
        categoryService.deleteCategory(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a category by ID", description = "Retrieves a category by its ID.")
    @ApiResponse(responseCode = "200", description = "Category found", content = @Content(schema = @Schema(implementation = CategoryDTO.class)))
    @ApiResponse(responseCode = "404", description = "Category not found", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    public ResponseEntity<CategoryDTO> getCategoryById(@Parameter(description = "Category ID", required = true) @PathVariable Long id) {
        log.info("Getting category with id: {}", id);
        return ResponseEntity.ok(categoryService.getCategoryById(id));
    }

    @GetMapping
    @Operation(summary = "Get all categories", description = "Retrieves all categories with pagination.")
    @ApiResponse(responseCode = "200", description = "Categories retrieved successfully", content = @Content(schema = @Schema(implementation = CategoryDTO.class)))
    public ResponseEntity<Page<CategoryDTO>> getAllCategories(Pageable pageable) {
        log.info("Getting all categories with pagination");
        return ResponseEntity.ok(categoryService.getAllCategories(pageable));
    }

    @GetMapping("/tree")
    @Operation(summary = "Get category tree", description = "Retrieves the complete category hierarchy.")
    @ApiResponse(responseCode = "200", description = "Category tree retrieved successfully", content = @Content(schema = @Schema(implementation = CategoryDTO.class)))
    public ResponseEntity<List<CategoryDTO>> getCategoryTree() {
        log.info("Getting category tree");
        return ResponseEntity.ok(categoryService.getCategoryTree());
    }

    @GetMapping("/code/{code}")
    @Operation(summary = "Get category by code", description = "Retrieves a category by its unique code.")
    @ApiResponse(responseCode = "200", description = "Category found", content = @Content(schema = @Schema(implementation = CategoryDTO.class)))
    @ApiResponse(responseCode = "404", description = "Category not found", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    public ResponseEntity<CategoryDTO> getCategoryByCode(@Parameter(description = "Category code", required = true) @PathVariable String code) {
        log.info("Getting category with code: {}", code);
        return ResponseEntity.ok(categoryService.getCategoryByCode(code));
    }

    @GetMapping("/parent/{parentId}")
    @Operation(summary = "Get subcategories", description = "Retrieves all subcategories of a parent category.")
    @ApiResponse(responseCode = "200", description = "Subcategories retrieved successfully", content = @Content(schema = @Schema(implementation = CategoryDTO.class)))
    public ResponseEntity<List<CategoryDTO>> getSubcategories(@Parameter(description = "Parent category ID", required = true) @PathVariable Long parentId) {
        log.info("Getting subcategories for parent id: {}", parentId);
        return ResponseEntity.ok(categoryService.getSubcategories(parentId));
    }

    @GetMapping("/level/{level}")
    @Operation(summary = "Get categories by level", description = "Retrieves all categories at a specific level in the hierarchy.")
    @ApiResponse(responseCode = "200", description = "Categories retrieved successfully", content = @Content(schema = @Schema(implementation = CategoryDTO.class)))
    public ResponseEntity<List<CategoryDTO>> getCategoriesByLevel(@Parameter(description = "Category level", required = true) @PathVariable Integer level) {
        log.info("Getting categories at level: {}", level);
        return ResponseEntity.ok(categoryService.getCategoriesByLevel(level));
    }

    @GetMapping("/active")
    @Operation(summary = "Get active categories", description = "Retrieves all active categories.")
    @ApiResponse(responseCode = "200", description = "Active categories retrieved successfully", content = @Content(schema = @Schema(implementation = CategoryDTO.class)))
    public ResponseEntity<List<CategoryDTO>> getActiveCategories() {
        log.info("Getting active categories");
        return ResponseEntity.ok(categoryService.getActiveCategories());
    }

    @PatchMapping("/{id}/activate")
    @Operation(summary = "Activate category", description = "Activates a category by ID.")
    @ApiResponse(responseCode = "200", description = "Category activated successfully")
    @ApiResponse(responseCode = "404", description = "Category not found", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    public ResponseEntity<Void> activateCategory(@Parameter(description = "Category ID", required = true) @PathVariable Long id) {
        log.info("Activating category with id: {}", id);
        categoryService.activateCategory(id);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{id}/deactivate")
    @Operation(summary = "Deactivate category", description = "Deactivates a category by ID.")
    @ApiResponse(responseCode = "200", description = "Category deactivated successfully")
    @ApiResponse(responseCode = "404", description = "Category not found", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    public ResponseEntity<Void> deactivateCategory(@Parameter(description = "Category ID", required = true) @PathVariable Long id) {
        log.info("Deactivating category with id: {}", id);
        categoryService.deactivateCategory(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/search")
    @Operation(summary = "Search categories", description = "Searches categories by name or description.")
    @ApiResponse(responseCode = "200", description = "Search results retrieved successfully", content = @Content(schema = @Schema(implementation = CategoryDTO.class)))
    public ResponseEntity<Page<CategoryDTO>> searchCategories(
            @Parameter(description = "Search term", required = true) @RequestParam String searchTerm,
            Pageable pageable) {
        log.info("Searching categories with term: {}", searchTerm);
        return ResponseEntity.ok(categoryService.searchCategories(searchTerm, pageable));
    }

    @GetMapping("/{id}/products")
    @Operation(summary = "Get category products", description = "Retrieves all products in a category.")
    @ApiResponse(responseCode = "200", description = "Products retrieved successfully", content = @Content(schema = @Schema(implementation = CategoryDTO.class)))
    public ResponseEntity<Page<CategoryDTO>> getCategoryProducts(
            @Parameter(description = "Category ID", required = true) @PathVariable Long id,
            Pageable pageable) {
        log.info("Getting products for category id: {}", id);
        return ResponseEntity.ok(categoryService.getCategoryProducts(id, pageable));
    }

    @GetMapping("/statistics")
    @Operation(summary = "Get category statistics", description = "Retrieves statistics about categories.")
    @ApiResponse(responseCode = "200", description = "Statistics retrieved successfully")
    public ResponseEntity<Map<String, Object>> getCategoryStatistics() {
        log.info("Getting category statistics");
        return ResponseEntity.ok(categoryService.getCategoryStatistics());
    }
} 