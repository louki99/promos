package ma.foodplus.ordering.system.pos.controller;

import ma.foodplus.ordering.system.pos.domain.StockMovement;
import ma.foodplus.ordering.system.pos.enums.StockMovementType;
import ma.foodplus.ordering.system.pos.service.StockMovementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/stock-movements")
public class StockMovementController {
    @Autowired
    private StockMovementService stockMovementService;

    @GetMapping
    public List<StockMovement> getAllStockMovements() {
        return stockMovementService.getAllStockMovements();
    }

    @GetMapping("/{id}")
    public ResponseEntity<StockMovement> getStockMovementById(@PathVariable Long id) {
        return stockMovementService.getStockMovementById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/product/{productId}")
    public List<StockMovement> getStockMovementsByProduct(@PathVariable Long productId) {
        return stockMovementService.getStockMovementsByProduct(productId);
    }

    @GetMapping("/store/{storeId}")
    public List<StockMovement> getStockMovementsByStore(@PathVariable Long storeId) {
        return stockMovementService.getStockMovementsByStore(storeId);
    }

    @GetMapping("/type/{type}")
    public List<StockMovement> getStockMovementsByType(@PathVariable StockMovementType type) {
        return stockMovementService.getStockMovementsByType(type);
    }

    @PostMapping
    public StockMovement createStockMovement(@RequestBody StockMovement stockMovement) {
        return stockMovementService.createStockMovement(stockMovement);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStockMovement(@PathVariable Long id) {
        stockMovementService.deleteStockMovement(id);
        return ResponseEntity.noContent().build();
    }
} 