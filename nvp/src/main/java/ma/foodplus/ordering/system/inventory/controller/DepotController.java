package ma.foodplus.ordering.system.inventory.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import ma.foodplus.ordering.system.inventory.dto.DepotDTO;
import ma.foodplus.ordering.system.inventory.model.Depot.DepotType;
import ma.foodplus.ordering.system.inventory.service.DepotService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/inventory/depots")
@RequiredArgsConstructor
@Tag(name = "Depot Management", description = "APIs for managing inventory depots")
public class DepotController extends InventoryBaseController {

    private final DepotService depotService;

    @PostMapping
    @Operation(summary = "Create a new depot")
    public ResponseEntity<DepotDTO> createDepot(@RequestBody DepotDTO depotDTO) {
        return new ResponseEntity<>(depotService.createDepot(depotDTO), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an existing depot")
    public ResponseEntity<DepotDTO> updateDepot(
            @PathVariable Long id,
            @RequestBody DepotDTO depotDTO) {
        return ResponseEntity.ok(depotService.updateDepot(id, depotDTO));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a depot")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteDepot(@PathVariable Long id) {
        depotService.deleteDepot(id);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a depot by ID")
    public ResponseEntity<DepotDTO> getDepotById(@PathVariable Long id) {
        return ResponseEntity.ok(depotService.getDepotById(id));
    }

    @GetMapping("/code/{depotCode}")
    @Operation(summary = "Get a depot by code")
    public ResponseEntity<DepotDTO> getDepotByCode(@PathVariable String depotCode) {
        return ResponseEntity.ok(depotService.getDepotByCode(depotCode));
    }

    @GetMapping
    @Operation(summary = "Get all depots")
    public ResponseEntity<List<DepotDTO>> getAllDepots() {
        return ResponseEntity.ok(depotService.getAllDepots());
    }

    @GetMapping("/active")
    @Operation(summary = "Get all active depots")
    public ResponseEntity<List<DepotDTO>> getActiveDepots() {
        return ResponseEntity.ok(depotService.getActiveDepots());
    }

    @GetMapping("/type/{type}")
    @Operation(summary = "Get depots by type")
    public ResponseEntity<List<DepotDTO>> getDepotsByType(@PathVariable DepotType type) {
        return ResponseEntity.ok(depotService.getDepotsByType(type));
    }

    @GetMapping("/site/{siteId}")
    @Operation(summary = "Get depots by site ID")
    public ResponseEntity<List<DepotDTO>> getDepotsBySiteId(@PathVariable Long siteId) {
        return ResponseEntity.ok(depotService.getDepotsBySiteId(siteId));
    }

    @GetMapping("/refrigerated")
    @Operation(summary = "Get all refrigerated depots")
    public ResponseEntity<List<DepotDTO>> getRefrigeratedDepots() {
        return ResponseEntity.ok(depotService.getRefrigeratedDepots());
    }

    @GetMapping("/capacity")
    @Operation(summary = "Get depots by minimum capacity")
    public ResponseEntity<List<DepotDTO>> getDepotsByMinCapacity(@RequestParam Double minCapacity) {
        return ResponseEntity.ok(depotService.getDepotsByMinCapacity(minCapacity));
    }

    @GetMapping("/security-level")
    @Operation(summary = "Get depots by minimum security level")
    public ResponseEntity<List<DepotDTO>> getDepotsByMinSecurityLevel(@RequestParam Integer minSecurityLevel) {
        return ResponseEntity.ok(depotService.getDepotsByMinSecurityLevel(minSecurityLevel));
    }

    @GetMapping("/search")
    @Operation(summary = "Search depots")
    public ResponseEntity<List<DepotDTO>> searchDepots(@RequestParam String searchTerm) {
        return ResponseEntity.ok(depotService.searchDepots(searchTerm));
    }

    @GetMapping("/stats/active/count")
    @Operation(summary = "Count active depots")
    public ResponseEntity<Long> countActiveDepots() {
        return ResponseEntity.ok(depotService.countActiveDepots());
    }

    @GetMapping("/stats/type/{type}/count")
    @Operation(summary = "Count depots by type")
    public ResponseEntity<Long> countDepotsByType(@PathVariable DepotType type) {
        return ResponseEntity.ok(depotService.countDepotsByType(type));
    }

    @PutMapping("/{id}/activate")
    @Operation(summary = "Activate a depot")
    public ResponseEntity<Void> activateDepot(@PathVariable Long id) {
        depotService.activateDepot(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}/deactivate")
    @Operation(summary = "Deactivate a depot")
    public ResponseEntity<Void> deactivateDepot(@PathVariable Long id) {
        depotService.deactivateDepot(id);
        return ResponseEntity.ok().build();
    }
} 