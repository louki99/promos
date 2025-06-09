package ma.foodplus.ordering.system.product.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CacheWarmingService {

    private final ProductService productService;
    private final ProductManagementUseCase productManagementUseCase;

    @Async
    @EventListener(ApplicationReadyEvent.class)
    public void warmUpCache() {
        log.info("Starting cache warm-up...");
        try {
            // Warm up all products cache
            productService.getAllProducts();
            log.info("Warmed up all products cache");

            // Warm up deliverable products cache
            productManagementUseCase.getDeliverableProducts();
            log.info("Warmed up deliverable products cache");

            // Warm up active products cache
            productManagementUseCase.getActiveProducts();
            log.info("Warmed up active products cache");

        } catch (Exception e) {
            log.error("Error during cache warm-up", e);
        }
        log.info("Cache warm-up completed");
    }
} 