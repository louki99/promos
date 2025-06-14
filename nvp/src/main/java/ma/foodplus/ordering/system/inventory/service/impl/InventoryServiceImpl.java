package ma.foodplus.ordering.system.inventory.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ma.foodplus.ordering.system.inventory.repository.ProductStockRepository;
import ma.foodplus.ordering.system.inventory.service.InventoryService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class InventoryServiceImpl implements InventoryService {

    private final ProductStockRepository productStockRepository;

    @Override
    @Transactional(readOnly = true)
    public int getProductStockLevel(Long productId) {
        log.info("Getting stock level for product: {}", productId);
        return productStockRepository.findByProductId(productId).stream()
                .map(stock -> stock.getQuantity().intValue())
                .reduce(0, Integer::sum);
    }
} 