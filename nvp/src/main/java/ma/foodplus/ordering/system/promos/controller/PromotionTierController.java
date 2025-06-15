package ma.foodplus.ordering.system.promos.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import ma.foodplus.ordering.system.promos.dto.PromotionTierDTO;
import ma.foodplus.ordering.system.promos.service.PromotionTierService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/promotion-tiers")
@RequiredArgsConstructor
@Tag(name = "Promotion Tiers", description = "APIs for managing promotion tiers")
@Validated
public class PromotionTierController {

    private final PromotionTierService promotionTierService;

    @PostMapping
    @Operation(summary = "Create a new promotion tier")
    public ResponseEntity<PromotionTierDTO> createTier(@Valid @RequestBody PromotionTierDTO tierDTO) {
        return ResponseEntity.ok(promotionTierService.createTier(tierDTO));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an existing promotion tier")
    public ResponseEntity<PromotionTierDTO> updateTier(
            @PathVariable @NotNull Integer id,
            @Valid @RequestBody PromotionTierDTO tierDTO) {
        tierDTO.setId(id);
        return ResponseEntity.ok(promotionTierService.updateTier(tierDTO));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a promotion tier")
    public ResponseEntity<Void> deleteTier(@PathVariable @NotNull Integer id) {
        promotionTierService.deleteTier(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a promotion tier by ID")
    public ResponseEntity<PromotionTierDTO> getTierById(@PathVariable @NotNull Integer id) {
        return ResponseEntity.ok(promotionTierService.getTierById(id));
    }

    @GetMapping("/rule/{ruleId}")
    @Operation(summary = "Get all tiers for a rule")
    public ResponseEntity<List<PromotionTierDTO>> getTiersByRuleId(@PathVariable @NotNull Integer ruleId) {
        return ResponseEntity.ok(promotionTierService.getTiersByRuleId(ruleId));
    }

    @PostMapping("/rule/{ruleId}")
    @Operation(summary = "Add a tier to a rule")
    public ResponseEntity<PromotionTierDTO> addTierToRule(
            @PathVariable @NotNull Integer ruleId,
            @Valid @RequestBody PromotionTierDTO tierDTO) {
        return ResponseEntity.ok(promotionTierService.addTierToRule(ruleId, tierDTO));
    }

    @DeleteMapping("/rule/{ruleId}/tier/{tierId}")
    @Operation(summary = "Remove a tier from a rule")
    public ResponseEntity<Void> removeTierFromRule(
            @PathVariable @NotNull Integer ruleId,
            @PathVariable @NotNull Integer tierId) {
        promotionTierService.removeTierFromRule(ruleId, tierId);
        return ResponseEntity.noContent().build();
    }
} 