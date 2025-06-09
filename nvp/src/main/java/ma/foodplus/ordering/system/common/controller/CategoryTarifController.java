package ma.foodplus.ordering.system.common.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import ma.foodplus.ordering.system.common.dto.CategoryTarifDTO;
import ma.foodplus.ordering.system.common.service.CategoryTarifService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/category-tarifs")
@RequiredArgsConstructor
@Tag(name = "Category Tariff Management", description = "APIs for managing category tariffs (hotels, restaurants, cafes)")
public class CategoryTarifController {

    private final CategoryTarifService categoryTarifService;

    @PostMapping
    @Operation(summary = "Create a new category tariff")
    public ResponseEntity<CategoryTarifDTO> createCategoryTarif(@Valid @RequestBody CategoryTarifDTO categoryTarifDTO) {
        return new ResponseEntity<>(categoryTarifService.createCategoryTarif(categoryTarifDTO), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an existing category tariff")
    public ResponseEntity<CategoryTarifDTO> updateCategoryTarif(
            @PathVariable Long id,
            @Valid @RequestBody CategoryTarifDTO categoryTarifDTO) {
        return ResponseEntity.ok(categoryTarifService.updateCategoryTarif(id, categoryTarifDTO));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a category tariff")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCategoryTarif(@PathVariable Long id) {
        categoryTarifService.deleteCategoryTarif(id);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a category tariff by ID")
    public ResponseEntity<CategoryTarifDTO> getCategoryTarifById(@PathVariable Long id) {
        return ResponseEntity.ok(categoryTarifService.getCategoryTarifById(id));
    }

    @GetMapping
    @Operation(summary = "Get all category tariffs")
    public ResponseEntity<List<CategoryTarifDTO>> getAllCategoryTarifs() {
        return ResponseEntity.ok(categoryTarifService.getAllCategoryTarifs());
    }

    @GetMapping("/by-description/{description}")
    @Operation(summary = "Get a category tariff by description")
    public ResponseEntity<CategoryTarifDTO> getCategoryTarifByDescription(@PathVariable String description) {
        return ResponseEntity.ok(categoryTarifService.getCategoryTarifByDescription(description));
    }
} 