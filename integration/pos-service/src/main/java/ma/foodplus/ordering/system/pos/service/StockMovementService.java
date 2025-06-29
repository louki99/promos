package ma.foodplus.ordering.system.pos.service;

import ma.foodplus.ordering.system.pos.domain.StockMovement;
import ma.foodplus.ordering.system.pos.enums.StockMovementType;
import ma.foodplus.ordering.system.pos.repository.StockMovementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class StockMovementService {
    @Autowired
    private StockMovementRepository stockMovementRepository;

    public List<StockMovement> getAllStockMovements() {
        return stockMovementRepository.findAll();
    }

    public Optional<StockMovement> getStockMovementById(Long id) {
        return stockMovementRepository.findById(id);
    }

    public List<StockMovement> getStockMovementsByProduct(Long productId) {
        return stockMovementRepository.findByProductId(productId);
    }

    public List<StockMovement> getStockMovementsByStore(Long storeId) {
        return stockMovementRepository.findByStoreId(storeId);
    }

    public List<StockMovement> getStockMovementsByType(StockMovementType type) {
        return stockMovementRepository.findByMovementType(type);
    }

    public StockMovement createStockMovement(StockMovement stockMovement) {
        stockMovement.setCreatedAt(java.time.LocalDateTime.now());
        return stockMovementRepository.save(stockMovement);
    }

    public StockMovement updateStockMovement(Long id, StockMovement stockMovementDetails) {
        StockMovement stockMovement = stockMovementRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Stock movement not found"));
        
        stockMovement.setProduct(stockMovementDetails.getProduct());
        stockMovement.setStore(stockMovementDetails.getStore());
        stockMovement.setQuantity(stockMovementDetails.getQuantity());
        stockMovement.setMovementType(stockMovementDetails.getMovementType());
        stockMovement.setReference(stockMovementDetails.getReference());
        stockMovement.setPerformedBy(stockMovementDetails.getPerformedBy());
        
        return stockMovementRepository.save(stockMovement);
    }

    public void deleteStockMovement(Long id) {
        stockMovementRepository.deleteById(id);
    }
} 