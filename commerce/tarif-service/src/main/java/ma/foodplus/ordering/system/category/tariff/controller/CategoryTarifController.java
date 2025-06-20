package ma.foodplus.ordering.system.category.tariff.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import ma.foodplus.ordering.system.category.tariff.domain.CategoryTarif;
import ma.foodplus.ordering.system.category.tariff.dto.CategoryTarifDTO;
import ma.foodplus.ordering.system.category.tariff.mapper.CategoryTarifMapper;
import ma.foodplus.ordering.system.category.tariff.service.CategoryTarifService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/category-tarifs")
@RequiredArgsConstructor
@Tag(name = "Category Tariff Management", description = "CRUD and activation APIs for managing category tariffs (hotels, restaurants, cafes). Includes endpoints for create, update, delete, fetch by ID/code, list all, get active, and toggle activation.")
public class CategoryTarifController {

    private final CategoryTarifService categoryTarifService;
    private final CategoryTarifMapper categoryTarifMapper;

    @PostMapping
    @Operation(summary = "Create a new category tariff", description = "Creates a new category tariff entity for a business type (hotel, restaurant, cafe, etc.)")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Category tariff created successfully", content = @Content(schema = @Schema(implementation = CategoryTarifDTO.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input", content = @Content)
    })
    public ResponseEntity<CategoryTarifDTO> createCategoryTarif(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "CategoryTarifDTO to create", required = true, content = @Content(schema = @Schema(implementation = CategoryTarifDTO.class)))
            @Valid @RequestBody CategoryTarifDTO categoryTarifDTO) {
        CategoryTarif categoryTarif = categoryTarifMapper.toEntity(categoryTarifDTO);
        CategoryTarif savedCategoryTarif = categoryTarifService.save(categoryTarif);
        return new ResponseEntity<>(categoryTarifMapper.toDTO(savedCategoryTarif), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an existing category tariff", description = "Updates the details of an existing category tariff by its ID.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Category tariff updated successfully", content = @Content(schema = @Schema(implementation = CategoryTarifDTO.class))),
        @ApiResponse(responseCode = "404", description = "Category tariff not found", content = @Content),
        @ApiResponse(responseCode = "400", description = "Invalid input", content = @Content)
    })
    public ResponseEntity<CategoryTarifDTO> updateCategoryTarif(
            @Parameter(description = "ID of the category tariff to update", required = true) @PathVariable Long id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Updated CategoryTarifDTO", required = true, content = @Content(schema = @Schema(implementation = CategoryTarifDTO.class)))
            @Valid @RequestBody CategoryTarifDTO categoryTarifDTO) {
        CategoryTarif categoryTarif = categoryTarifMapper.toEntity(categoryTarifDTO);
        CategoryTarif updatedCategoryTarif = categoryTarifService.update(id, categoryTarif);
        return ResponseEntity.ok(categoryTarifMapper.toDTO(updatedCategoryTarif));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a category tariff", description = "Deletes a category tariff by its ID.")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Category tariff deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Category tariff not found", content = @Content)
    })
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCategoryTarif(
            @Parameter(description = "ID of the category tariff to delete", required = true) @PathVariable Long id) {
        categoryTarifService.deleteById(id);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a category tariff by ID", description = "Fetches a category tariff by its unique ID.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Category tariff found", content = @Content(schema = @Schema(implementation = CategoryTarifDTO.class))),
        @ApiResponse(responseCode = "404", description = "Category tariff not found", content = @Content)
    })
    public ResponseEntity<CategoryTarifDTO> getCategoryTarifById(
            @Parameter(description = "ID of the category tariff", required = true) @PathVariable Long id) {
        return categoryTarifService.findById(id)
                .map(categoryTarifMapper::toDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    @Operation(summary = "Get all category tariffs", description = "Returns a list of all category tariffs.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "List of category tariffs", content = @Content(schema = @Schema(implementation = CategoryTarifDTO.class)))
    })
    public ResponseEntity<List<CategoryTarifDTO>> getAllCategoryTarifs() {
        List<CategoryTarifDTO> dtos = categoryTarifService.findAll().stream()
                .map(categoryTarifMapper::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/code/{code}")
    @Operation(summary = "Get a category tariff by code", description = "Fetches a category tariff by its unique code.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Category tariff found", content = @Content(schema = @Schema(implementation = CategoryTarifDTO.class))),
        @ApiResponse(responseCode = "404", description = "Category tariff not found", content = @Content)
    })
    public ResponseEntity<CategoryTarifDTO> getCategoryTarifByCode(
            @Parameter(description = "Code of the category tariff", required = true) @PathVariable String code) {
        return categoryTarifService.findByCode(code)
                .map(categoryTarifMapper::toDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/active")
    @Operation(summary = "Get the active category tariff", description = "Fetches the currently active category tariff.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Active category tariff found", content = @Content(schema = @Schema(implementation = CategoryTarifDTO.class))),
        @ApiResponse(responseCode = "404", description = "No active category tariff found", content = @Content)
    })
    public ResponseEntity<CategoryTarifDTO> getActiveCategoryTarif() {
        return categoryTarifService.findActiveCategoryTarif()
                .map(categoryTarifMapper::toDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/toggle/{id}")
    @Operation(summary = "Toggle a category tariff", description = "Toggles the activation status of a category tariff by its ID.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Category tariff toggled successfully", content = @Content(schema = @Schema(implementation = CategoryTarifDTO.class))),
        @ApiResponse(responseCode = "404", description = "Category tariff not found", content = @Content)
    })
    public ResponseEntity<CategoryTarifDTO> activateCategoryTarif(
            @Parameter(description = "ID of the category tariff to toggle", required = true) @PathVariable Long id) {
        CategoryTarif response = categoryTarifService.toggle(id);
        return ResponseEntity.ok(categoryTarifMapper.toDTO(response));
    }
} 