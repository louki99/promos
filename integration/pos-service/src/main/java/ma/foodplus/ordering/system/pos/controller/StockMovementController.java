package ma.foodplus.ordering.system.pos.controller;

import ma.foodplus.ordering.system.pos.domain.StockMovement;
import ma.foodplus.ordering.system.pos.enums.StockMovementType;
import ma.foodplus.ordering.system.pos.service.StockMovementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

@RestController
@RequestMapping("/api/stock-movements")
@Tag(name = "Stock Movement Management", description = "Endpoints for managing inventory stock movements.")
public class StockMovementController {
    @Autowired
    private StockMovementService stockMovementService;

    @GetMapping
    @Operation(summary = "Get all stock movements", description = "Retrieve a list of all stock movements.")
    public List<StockMovement> getAllStockMovements() {
        return stockMovementService.getAllStockMovements();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get stock movement by ID", description = "Retrieve a stock movement by its unique ID.")
    public ResponseEntity<StockMovement> getStockMovementById(@PathVariable Long id) {
        return stockMovementService.getStockMovementById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/product/{productId}")
    @Operation(summary = "Get stock movements by product", description = "Retrieve all stock movements for a specific product.")
    public List<StockMovement> getStockMovementsByProduct(@PathVariable Long productId) {
        return stockMovementService.getStockMovementsByProduct(productId);
    }

    @GetMapping("/store/{storeId}")
    @Operation(summary = "Get stock movements by store", description = "Retrieve all stock movements for a specific store.")
    public List<StockMovement> getStockMovementsByStore(@PathVariable Long storeId) {
        return stockMovementService.getStockMovementsByStore(storeId);
    }

    @GetMapping("/type/{type}")
    @Operation(summary = "Get stock movements by type", description = "Retrieve all stock movements of a specific type.")
    public List<StockMovement> getStockMovementsByType(@PathVariable StockMovementType type) {
        return stockMovementService.getStockMovementsByType(type);
    }

    @PostMapping
    @Operation(summary = "Create stock movement", description = "Create a new stock movement.")
    public StockMovement createStockMovement(@RequestBody StockMovement stockMovement) {
        return stockMovementService.createStockMovement(stockMovement);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update stock movement", description = "Update an existing stock movement by ID.")
    public StockMovement updateStockMovement(@PathVariable Long id, @RequestBody StockMovement stockMovement) {
        return stockMovementService.updateStockMovement(id, stockMovement);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete stock movement", description = "Delete a stock movement by ID.")
    public ResponseEntity<Void> deleteStockMovement(@PathVariable Long id) {
        stockMovementService.deleteStockMovement(id);
        return ResponseEntity.noContent().build();
    }
} 