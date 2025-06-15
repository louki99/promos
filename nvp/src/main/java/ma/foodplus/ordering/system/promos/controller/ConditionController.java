package ma.foodplus.ordering.system.promos.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import ma.foodplus.ordering.system.promos.dto.ConditionDTO;
import ma.foodplus.ordering.system.promos.service.ConditionService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/conditions")
@RequiredArgsConstructor
@Tag(name = "Promotion Conditions", description = "APIs for managing promotion conditions")
@Validated
public class ConditionController {

    private final ConditionService conditionService;


    @PostMapping
    @Operation(summary = "Create a new condition")
    public ResponseEntity<ConditionDTO> createCondition(@Valid @RequestBody ConditionDTO conditionDTO) {
        return ResponseEntity.ok(conditionService.createCondition(conditionDTO));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an existing condition")
    public ResponseEntity<ConditionDTO> updateCondition(
            @PathVariable @NotNull Integer id,
            @Valid @RequestBody ConditionDTO conditionDTO) {
        conditionDTO.setId(id);
        return ResponseEntity.ok(conditionService.updateCondition(conditionDTO));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a condition")
    public ResponseEntity<Void> deleteCondition(@PathVariable @NotNull Integer id) {
        conditionService.deleteCondition(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a condition by ID")
    public ResponseEntity<ConditionDTO> getConditionById(@PathVariable @NotNull Integer id) {
        return ResponseEntity.ok(conditionService.getConditionById(id));
    }

    @GetMapping("/rule/{ruleId}")
    @Operation(summary = "Get all conditions for a rule")
    public ResponseEntity<List<ConditionDTO>> getConditionsByRuleId(@PathVariable @NotNull Integer ruleId) {
        return ResponseEntity.ok(conditionService.getConditionsByRuleId(ruleId));
    }

    @PostMapping("/rule/{ruleId}")
    @Operation(summary = "Add a condition to a rule")
    public ResponseEntity<ConditionDTO> addConditionToRule(
            @PathVariable @NotNull Integer ruleId,
            @Valid @RequestBody ConditionDTO conditionDTO) {
        return ResponseEntity.ok(conditionService.addConditionToRule(ruleId, conditionDTO));
    }

    @DeleteMapping("/rule/{ruleId}/condition/{conditionId}")
    @Operation(summary = "Remove a condition from a rule")
    public ResponseEntity<Void> removeConditionFromRule(
            @PathVariable @NotNull Integer ruleId,
            @PathVariable @NotNull Integer conditionId) {
        conditionService.removeConditionFromRule(ruleId, conditionId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/types")
    @Operation(summary = "Get all available condition types")
    public ResponseEntity<List<String>> getConditionTypes() {
        return ResponseEntity.ok(conditionService.getConditionTypes());
    }

    @GetMapping("/operators")
    @Operation(summary = "Get all available operators")
    public ResponseEntity<List<String>> getOperators() {
        return ResponseEntity.ok(conditionService.getOperators());
    }
} 