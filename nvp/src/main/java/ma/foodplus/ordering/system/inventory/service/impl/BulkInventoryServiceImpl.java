package ma.foodplus.ordering.system.inventory.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ma.foodplus.ordering.system.inventory.model.ProductStock;
import ma.foodplus.ordering.system.inventory.repository.ProductStockRepository;
import ma.foodplus.ordering.system.inventory.service.BulkInventoryService;
import ma.foodplus.ordering.system.product.model.Product;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class BulkInventoryServiceImpl implements BulkInventoryService {

    private final ProductStockRepository productStockRepository;

    @Override
    @Transactional(readOnly = true)
    public boolean checkAvailability(Product product, Integer quantity) {
        if (product == null || quantity == null) {
            return false;
        }

        // Get total available quantity across all depots
        List<ProductStock> stocks = productStockRepository.findByProductIdAndQuantityGreaterThan(
            product.getId(), 
            BigDecimal.ZERO
        );
        int totalAvailable = stocks.stream()
                .mapToInt(stock -> stock.getQuantity().intValue())
                .sum();

        return totalAvailable >= quantity;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Integer> getRecommendedQuantities(Product product) {
        if (product == null) {
            return new ArrayList<>();
        }

        List<Integer> recommendations = new ArrayList<>();

        // Add minimum wholesale quantity
        if (product.getWholesaleMinimumQuantity() != null) {
            recommendations.add(product.getWholesaleMinimumQuantity());
        }

        // Add tier quantities
        if (product.getWholesaleTier1Quantity() != null) {
            recommendations.add(product.getWholesaleTier1Quantity());
        }
        if (product.getWholesaleTier2Quantity() != null) {
            recommendations.add(product.getWholesaleTier2Quantity());
        }
        if (product.getWholesaleTier3Quantity() != null) {
            recommendations.add(product.getWholesaleTier3Quantity());
        }

        // Add bulk package size multiples
        if (product.getIsBulkItem() && product.getBulkPackageSize() != null) {
            int bulkSize = product.getBulkPackageSize();
            int maxMultiplier = 5; // Recommend up to 5 bulk packages
            for (int i = 2; i <= maxMultiplier; i++) {
                recommendations.add(bulkSize * i);
            }
        }

        return recommendations;
    }

    @Override
    @Transactional
    public void reserveStock(Product product, Integer quantity) {
        if (product == null || quantity == null || quantity <= 0) {
            throw new IllegalArgumentException("Invalid product or quantity");
        }

        if (!checkAvailability(product, quantity)) {
            throw new IllegalStateException("Insufficient stock available");
        }

        // Get available stock entries ordered by quantity
        List<ProductStock> stocks = productStockRepository.findByProductIdAndQuantityGreaterThan(
            product.getId(), 
            BigDecimal.ZERO
        );
        int remainingToReserve = quantity;

        for (ProductStock stock : stocks) {
            if (remainingToReserve <= 0) {
                break;
            }

            int availableInStock = stock.getQuantity().intValue();
            int toReserve = Math.min(remainingToReserve, availableInStock);

            stock.setReservedQuantity(stock.getReservedQuantity().add(BigDecimal.valueOf(toReserve)));
            productStockRepository.save(stock);

            remainingToReserve -= toReserve;
        }
    }

    @Override
    @Transactional
    public void releaseStock(Product product, Integer quantity) {
        if (product == null || quantity == null || quantity <= 0) {
            throw new IllegalArgumentException("Invalid product or quantity");
        }

        // Get stock entries with reserved quantity
        List<ProductStock> stocks = productStockRepository.findByProductIdAndQuantityGreaterThan(
            product.getId(), 
            BigDecimal.ZERO
        );
        int remainingToRelease = quantity;

        for (ProductStock stock : stocks) {
            if (remainingToRelease <= 0) {
                break;
            }

            int reservedInStock = stock.getReservedQuantity().intValue();
            int toRelease = Math.min(remainingToRelease, reservedInStock);

            stock.setReservedQuantity(stock.getReservedQuantity().subtract(BigDecimal.valueOf(toRelease)));
            productStockRepository.save(stock);

            remainingToRelease -= toRelease;
        }
    }
} 