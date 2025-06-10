package ma.foodplus.ordering.system.common.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import ma.foodplus.ordering.system.common.dto.CategoryTarifDTO;
import ma.foodplus.ordering.system.common.mapper.CategoryTarifMapper;
import ma.foodplus.ordering.system.common.model.CategoryTarif;
import ma.foodplus.ordering.system.common.service.CategoryTarifService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/category-tarifs")
@RequiredArgsConstructor
@Tag(name = "Category Tariff Management", description = "APIs for managing category tariffs (hotels, restaurants, cafes)")
public class CategoryTarifController {

    private final CategoryTarifService categoryTarifService;
    private final CategoryTarifMapper categoryTarifMapper;

    @PostMapping
    @Operation(summary = "Create a new category tariff")
    public ResponseEntity<CategoryTarifDTO> createCategoryTarif(@Valid @RequestBody CategoryTarifDTO categoryTarifDTO) {
        CategoryTarif categoryTarif = categoryTarifMapper.toEntity(categoryTarifDTO);
        CategoryTarif savedCategoryTarif = categoryTarifService.save(categoryTarif);
        return new ResponseEntity<>(categoryTarifMapper.toDTO(savedCategoryTarif), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an existing category tariff")
    public ResponseEntity<CategoryTarifDTO> updateCategoryTarif(
            @PathVariable Long id,
            @Valid @RequestBody CategoryTarifDTO categoryTarifDTO) {
        CategoryTarif categoryTarif = categoryTarifMapper.toEntity(categoryTarifDTO);
        CategoryTarif updatedCategoryTarif = categoryTarifService.update(id, categoryTarif);
        return ResponseEntity.ok(categoryTarifMapper.toDTO(updatedCategoryTarif));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a category tariff")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCategoryTarif(@PathVariable Long id) {
        categoryTarifService.deleteById(id);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a category tariff by ID")
    public ResponseEntity<CategoryTarifDTO> getCategoryTarifById(@PathVariable Long id) {
        return categoryTarifService.findById(id)
                .map(categoryTarifMapper::toDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    @Operation(summary = "Get all category tariffs")
    public ResponseEntity<List<CategoryTarifDTO>> getAllCategoryTarifs() {
        List<CategoryTarifDTO> dtos = categoryTarifService.findAll().stream()
                .map(categoryTarifMapper::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/by-description/{description}")
    @Operation(summary = "Get a category tariff by description")
    public ResponseEntity<CategoryTarifDTO> getCategoryTarifByDescription(@PathVariable String description) {
        return categoryTarifService.findAll().stream()
                .filter(ct -> ct.getDescription().equals(description))
                .findFirst()
                .map(categoryTarifMapper::toDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
} 