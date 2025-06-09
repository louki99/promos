package ma.foodplus.ordering.system.inventory.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ma.foodplus.ordering.system.inventory.dto.request.BulkProductStockRequest;
import ma.foodplus.ordering.system.inventory.dto.request.ProductStockRequest;
import ma.foodplus.ordering.system.inventory.dto.request.StockTransferRequest;
import ma.foodplus.ordering.system.inventory.dto.response.*;
import ma.foodplus.ordering.system.inventory.exception.ResourceNotFoundException;
import ma.foodplus.ordering.system.inventory.mapper.ProductStockMapper;
import ma.foodplus.ordering.system.inventory.model.ProductStock;
import ma.foodplus.ordering.system.inventory.repository.ProductStockRepository;
import ma.foodplus.ordering.system.inventory.service.ProductStockService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class ProductStockServiceImpl implements ProductStockService {

    private final ProductStockRepository productStockRepository;
    private final ProductStockMapper productStockMapper;

    @Override
    @Transactional
    public ProductStockResponse createProductStock(ProductStockRequest request) {
        log.info("Creating new product stock: {}", request);
        ProductStock productStock = productStockMapper.toEntity(request);
        productStock = productStockRepository.save(productStock);
        return productStockMapper.toResponse(productStock);
    }

    @Override
    @Transactional
    public ProductStockResponse updateProductStock(Long id, ProductStockRequest request) {
        log.info("Updating product stock with id {}: {}", id, request);
        ProductStock productStock = productStockRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product stock not found with id: " + id));
        
        productStockMapper.updateEntityFromRequest(request, productStock);
        productStock = productStockRepository.save(productStock);
        return productStockMapper.toResponse(productStock);
    }

    @Override
    @Transactional
    public void deleteProductStock(Long id) {
        log.info("Deleting product stock with id: {}", id);
        if (!productStockRepository.existsById(id)) {
            throw new ResourceNotFoundException("Product stock not found with id: " + id);
        }
        productStockRepository.deleteById(id);
    }

    @Override
    public ProductStockResponse getProductStockById(Long id) {
        log.info("Getting product stock with id: {}", id);
        ProductStock productStock = productStockRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product stock not found with id: " + id));
        return productStockMapper.toResponse(productStock);
    }

    @Override
    public List<ProductStockResponse> getAllProductStocks() {
        log.info("Getting all product stocks");
        return productStockRepository.findAll().stream()
                .map(productStockMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public BulkProductStockResponse processBulkOperation(BulkProductStockRequest request) {
        log.info("Processing bulk operation of type: {}", request.getOperationType());
        BulkProductStockResponse response = new BulkProductStockResponse();
        response.setOperationType(request.getOperationType());
        response.setTotalProcessed(request.getProductStocks().size());

        List<ProductStockResponse> successfulItems = new ArrayList<>();
        List<BulkProductStockResponse.FailedOperation> failedOperations = new ArrayList<>();

        for (ProductStockRequest stockRequest : request.getProductStocks()) {
            try {
                switch (request.getOperationType()) {
                    case CREATE:
                        successfulItems.add(createProductStock(stockRequest));
                        break;
                    case UPDATE:
                        // Assuming we have a way to identify which stock to update
                        successfulItems.add(updateProductStock(stockRequest.getProductId(), stockRequest));
                        break;
                    case DELETE:
                        deleteProductStock(stockRequest.getProductId());
                        break;
                    case QUALITY_CHECK:
                        updateQualityStatus(stockRequest.getProductId(), stockRequest.getQualityStatus());
                        break;
                    case EXPIRY_CHECK:
                        updateExpiryDate(stockRequest.getProductId(), stockRequest.getExpiryDate());
                        break;
                }
            } catch (Exception e) {
                log.error("Failed to process stock request: {}", stockRequest, e);
                BulkProductStockResponse.FailedOperation failedOp = new BulkProductStockResponse.FailedOperation();
                failedOp.setProductId(stockRequest.getProductId());
                failedOp.setDepotId(stockRequest.getDepotId());
                failedOp.setErrorMessage(e.getMessage());
                failedOperations.add(failedOp);
            }
        }

        response.setSuccessfulItems(successfulItems);
        response.setFailedOperations(failedOperations);
        response.setSuccessful(successfulItems.size());
        response.setFailed(failedOperations.size());

        return response;
    }

    @Override
    public StockTransferResponse transferStock(Long id, StockTransferRequest request) {
        log.info("Transferring stock from ID: {} to depot: {}", id, request.getDestinationDepotId());
        ProductStock sourceStock = productStockRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Source product stock not found"));

        // Validate transfer quantity
        if (sourceStock.getQuantity().compareTo(request.getQuantity()) < 0) {
            throw new RuntimeException("Insufficient stock for transfer");
        }

        // Create or update destination stock
        ProductStock destinationStock = productStockRepository
                .findByProductIdAndDepotId(sourceStock.getProductId(), request.getDestinationDepotId())
                .orElseGet(() -> {
                    ProductStock newStock = new ProductStock();
                    newStock.setProductId(sourceStock.getProductId());
                    newStock.setDepotId(request.getDestinationDepotId());
                    newStock.setQuantity(BigDecimal.ZERO);
                    newStock.setUnitCost(sourceStock.getUnitCost());
                    return newStock;
                });

        // Update quantities
        sourceStock.setQuantity(sourceStock.getQuantity().subtract(request.getQuantity()));
        destinationStock.setQuantity(destinationStock.getQuantity().add(request.getQuantity()));

        // Save changes
        productStockRepository.save(sourceStock);
        productStockRepository.save(destinationStock);

        // Create transfer response
        StockTransferResponse response = new StockTransferResponse();
        response.setSourceProductStockId(id);
        response.setDestinationProductStockId(destinationStock.getId());
        response.setSourceDepotId(sourceStock.getDepotId());
        response.setDestinationDepotId(request.getDestinationDepotId());
        response.setQuantity(request.getQuantity());
        response.setStatus(StockTransferResponse.TransferStatus.COMPLETED);
        response.setReason(request.getReason());
        response.setReferenceNumber(request.getReferenceNumber());
        response.setActualTransferDate(ZonedDateTime.now());

        return response;
    }

    @Override
    public List<StockMovementResponse> getStockMovementHistory(Long id, LocalDate startDate, LocalDate endDate) {
        // Implementation would depend on your stock movement tracking system
        // This is a placeholder implementation
        return Collections.emptyList();
    }

    @Override
    @Transactional
    public ProductStockResponse updateQualityStatus(Long id, ProductStock.QualityStatus newStatus) {
        log.info("Updating quality status for id {} to {}", id, newStatus);
        ProductStock productStock = productStockRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product stock not found with id: " + id));
        
        productStock.setQualityStatus(newStatus);
        productStock = productStockRepository.save(productStock);
        return productStockMapper.toResponse(productStock);
    }

    @Override
    @Transactional
    public ProductStockResponse quarantineProduct(Long id, String reason) {
        log.info("Quarantining product stock with id {}: {}", id, reason);
        ProductStock productStock = productStockRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product stock not found with id: " + id));
        
        productStock.setQualityStatus(ProductStock.QualityStatus.QUARANTINED);
        productStock.setQualityNotes(reason);
        productStock = productStockRepository.save(productStock);
        return productStockMapper.toResponse(productStock);
    }

    @Override
    @Transactional
    public ProductStockResponse releaseFromQuarantine(Long id) {
        log.info("Releasing product stock with id {} from quarantine", id);
        ProductStock productStock = productStockRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product stock not found with id: " + id));
        
        productStock.setQualityStatus(ProductStock.QualityStatus.INSPECTED);
        productStock.setQualityNotes("Released from quarantine");
        productStock = productStockRepository.save(productStock);
        return productStockMapper.toResponse(productStock);
    }

    @Override
    @Transactional
    public ProductStockResponse markForInspection(Long id) {
        log.info("Marking product stock with id {} for inspection", id);
        ProductStock productStock = productStockRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product stock not found with id: " + id));
        
        productStock.setQualityStatus(ProductStock.QualityStatus.INSPECTION_REQUIRED);
        productStock = productStockRepository.save(productStock);
        return productStockMapper.toResponse(productStock);
    }

    @Override
    @Transactional
    public ProductStockResponse markAsDamaged(Long id, String damageDescription) {
        log.info("Marking product stock with id {} as damaged: {}", id, damageDescription);
        ProductStock productStock = productStockRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product stock not found with id: " + id));
        
        productStock.setQualityStatus(ProductStock.QualityStatus.DAMAGED);
        productStock.setQualityNotes(damageDescription);
        productStock = productStockRepository.save(productStock);
        return productStockMapper.toResponse(productStock);
    }

    @Override
    @Transactional
    public ProductStockResponse recallProduct(Long id, String recallReason) {
        log.info("Recalling product stock with id {}: {}", id, recallReason);
        ProductStock productStock = productStockRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product stock not found with id: " + id));
        
        productStock.setQualityStatus(ProductStock.QualityStatus.RECALLED);
        productStock.setQualityNotes(recallReason);
        productStock = productStockRepository.save(productStock);
        return productStockMapper.toResponse(productStock);
    }

    @Override
    @Transactional
    public ProductStockResponse updateStockQuantity(Long id, BigDecimal newQuantity) {
        log.info("Updating stock quantity for id {} to {}", id, newQuantity);
        ProductStock productStock = productStockRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product stock not found with id: " + id));
        
        productStock.setQuantity(newQuantity);
        productStock = productStockRepository.save(productStock);
        return productStockMapper.toResponse(productStock);
    }

    @Override
    @Transactional
    public ProductStockResponse updateUnitCost(Long id, BigDecimal newUnitCost) {
        log.info("Updating unit cost for id {} to {}", id, newUnitCost);
        ProductStock productStock = productStockRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product stock not found with id: " + id));
        
        productStock.setUnitCost(newUnitCost);
        productStock = productStockRepository.save(productStock);
        return productStockMapper.toResponse(productStock);
    }

    @Override
    @Transactional
    public ProductStockResponse updateExpiryDate(Long id, LocalDate newExpiryDate) {
        log.info("Updating expiry date for id {} to {}", id, newExpiryDate);
        ProductStock productStock = productStockRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product stock not found with id: " + id));
        
        productStock.setExpiryDate(newExpiryDate);
        productStock = productStockRepository.save(productStock);
        return productStockMapper.toResponse(productStock);
    }

    @Override
    public void reserveStock(Long id, Double quantity) {
        ProductStock stock = productStockRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product stock not found"));
        BigDecimal newReservedQuantity = stock.getReservedQuantity().add(BigDecimal.valueOf(quantity));
        if (newReservedQuantity.compareTo(stock.getQuantity()) > 0) {
            throw new RuntimeException("Cannot reserve more than available quantity");
        }
        stock.setReservedQuantity(newReservedQuantity);
        productStockRepository.save(stock);
    }

    @Override
    public void releaseReservedStock(Long id, Double quantity) {
        ProductStock stock = productStockRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product stock not found"));
        BigDecimal newReservedQuantity = stock.getReservedQuantity().subtract(BigDecimal.valueOf(quantity));
        if (newReservedQuantity.compareTo(BigDecimal.ZERO) < 0) {
            throw new RuntimeException("Cannot release more than reserved quantity");
        }
        stock.setReservedQuantity(newReservedQuantity);
        productStockRepository.save(stock);
    }

    @Override
    public List<ProductStockResponse> getProductStocksByProductId(Long productId) {
        log.info("Getting product stocks for product id: {}", productId);
        return productStockRepository.findByProductId(productId).stream()
                .map(productStockMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductStockResponse> getProductStocksByDepotId(Long depotId) {
        log.info("Getting product stocks for depot id: {}", depotId);
        return productStockRepository.findByDepotId(depotId).stream()
                .map(productStockMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductStockResponse> getLowStockProducts() {
        log.info("Getting low stock products");
        return productStockRepository.findAll().stream()
                .filter(stock -> stock.getMinimumQuantity() != null && 
                        stock.getQuantity().compareTo(stock.getMinimumQuantity()) <= 0)
                .map(productStockMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductStockResponse> getExpiredProducts() {
        log.info("Getting expired products");
        LocalDate today = LocalDate.now();
        return productStockRepository.findAll().stream()
                .filter(stock -> stock.getExpiryDate() != null && 
                        stock.getExpiryDate().isBefore(today))
                .map(productStockMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductStockResponse> getProductsExpiringBefore(LocalDate date) {
        return productStockRepository.findProductsExpiringBefore(date).stream()
                .map(productStockMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductStockResponse> getProductsByQualityStatus(ProductStock.QualityStatus status) {
        log.info("Getting products with quality status: {}", status);
        return productStockRepository.findByQualityStatus(status).stream()
                .map(productStockMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductStockResponse> getProductsInQuarantine() {
        return getProductsByQualityStatus(ProductStock.QualityStatus.QUARANTINED);
    }

    @Override
    public List<ProductStockResponse> getProductsRequiringInspection() {
        return getProductsByQualityStatus(ProductStock.QualityStatus.INSPECTION_REQUIRED);
    }

    @Override
    public List<ProductStockResponse> getDamagedProducts() {
        log.info("Getting damaged products");
        return getProductsByQualityStatus(ProductStock.QualityStatus.DAMAGED);
    }

    @Override
    public List<ProductStockResponse> getRecalledProducts() {
        log.info("Getting recalled products");
        return getProductsByQualityStatus(ProductStock.QualityStatus.RECALLED);
    }

    @Override
    public List<ProductStockResponse> searchProductStocks(String searchTerm) {
        return productStockRepository.searchProductStocks(searchTerm).stream()
                .map(productStockMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public InventorySummaryResponse getInventorySummary() {
        log.info("Getting inventory summary");
        List<ProductStock> allStocks = productStockRepository.findAll();
        
        BigDecimal totalValue = allStocks.stream()
                .map(stock -> stock.getQuantity().multiply(stock.getUnitCost()))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        long totalProducts = allStocks.size();
        long lowStockCount = countLowStockProducts();
        long expiredCount = countExpiredProducts();
        long quarantinedCount = countProductsByQualityStatus(ProductStock.QualityStatus.QUARANTINED);
        long damagedCount = countProductsByQualityStatus(ProductStock.QualityStatus.DAMAGED);
        long recalledCount = countProductsByQualityStatus(ProductStock.QualityStatus.RECALLED);
        
        return InventorySummaryResponse.builder()
                .totalProducts(totalProducts)
                .totalValue(totalValue)
                .lowStockCount(lowStockCount)
                .expiredCount(expiredCount)
                .quarantinedCount(quarantinedCount)
                .damagedCount(damagedCount)
                .recalledCount(recalledCount)
                .build();
    }

    @Override
    public List<InventoryAlertResponse> getInventoryAlerts(InventoryAlertResponse.AlertType alertType) {
        log.info("Getting inventory alerts of type: {}", alertType);
        List<InventoryAlertResponse> alerts = new ArrayList<>();
        LocalDate today = LocalDate.now();
        
        switch (alertType) {
            case LOW_STOCK:
                generateLowStockAlerts(alerts);
                break;
                
            case EXPIRY_WARNING:
                generateExpiryAlerts(alerts, today);
                break;
                
            case QUALITY_ISSUE:
                generateQualityAlerts(alerts);
                break;

            case STOCK_MOVEMENT:
                generateStockMovementAlerts(alerts);
                break;

            case COST_ALERT:
                generateCostAlerts(alerts);
                break;

            case RESERVATION_ALERT:
                generateReservationAlerts(alerts);
                break;

            default:
                // Generate all alerts if no specific type is requested
                generateLowStockAlerts(alerts);
                generateExpiryAlerts(alerts, today);
                generateQualityAlerts(alerts);
                generateStockMovementAlerts(alerts);
                generateCostAlerts(alerts);
                generateReservationAlerts(alerts);
        }
        
        return alerts;
    }

    private void generateLowStockAlerts(List<InventoryAlertResponse> alerts) {
        productStockRepository.findAll().stream()
                .filter(stock -> stock.getMinimumQuantity() != null)
                .forEach(stock -> {
                    BigDecimal currentQuantity = stock.getQuantity();
                    BigDecimal minimumQuantity = stock.getMinimumQuantity();
                    BigDecimal threshold = minimumQuantity.multiply(new BigDecimal("0.2")); // 20% threshold

                    if (currentQuantity.compareTo(minimumQuantity) <= 0) {
                        InventoryAlertResponse.AlertSeverity severity = calculateLowStockSeverity(currentQuantity, minimumQuantity);
                        String message = generateLowStockMessage(stock, currentQuantity, minimumQuantity, severity);
                        
                        alerts.add(InventoryAlertResponse.builder()
                                .type(InventoryAlertResponse.AlertType.LOW_STOCK)
                                .severity(severity)
                                .productStockId(stock.getId())
                                .productName(stock.getProductName())
                                .depotName(stock.getDepotName())
                                .currentQuantity(currentQuantity)
                                .thresholdQuantity(minimumQuantity)
                                .message(message)
                                .createdAt(stock.getUpdatedAt().toLocalDateTime())
                                .build());
                    }
                });
    }

    private void generateExpiryAlerts(List<InventoryAlertResponse> alerts, LocalDate today) {
        productStockRepository.findAll().stream()
                .filter(stock -> stock.getExpiryDate() != null)
                .forEach(stock -> {
                    LocalDate expiryDate = stock.getExpiryDate();
                    long daysUntilExpiry = java.time.temporal.ChronoUnit.DAYS.between(today, expiryDate);
                    
                    if (daysUntilExpiry <= 30) { // Alert for items expiring within 30 days
                        InventoryAlertResponse.AlertSeverity severity = calculateExpirySeverity(daysUntilExpiry);
                        String message = generateExpiryMessage(stock, daysUntilExpiry, severity);
                        
                        alerts.add(InventoryAlertResponse.builder()
                                .type(InventoryAlertResponse.AlertType.EXPIRY_WARNING)
                                .severity(severity)
                                .productStockId(stock.getId())
                                .productName(stock.getProductName())
                                .depotName(stock.getDepotName())
                                .currentQuantity(stock.getQuantity())
                                .expiryDate(expiryDate)
                                .message(message)
                                .createdAt(stock.getUpdatedAt().toLocalDateTime())
                                .build());
                    }
                });
    }

    private void generateQualityAlerts(List<InventoryAlertResponse> alerts) {
        productStockRepository.findAll().stream()
                .filter(stock -> stock.getQualityStatus() != null && 
                        stock.getQualityStatus() != ProductStock.QualityStatus.INSPECTED)
                .forEach(stock -> {
                    InventoryAlertResponse.AlertSeverity severity = calculateQualitySeverity(stock.getQualityStatus());
                    String message = generateQualityMessage(stock, severity);
                    
                    alerts.add(InventoryAlertResponse.builder()
                            .type(InventoryAlertResponse.AlertType.QUALITY_ISSUE)
                            .severity(severity)
                            .productStockId(stock.getId())
                            .productName(stock.getProductName())
                            .depotName(stock.getDepotName())
                            .currentQuantity(stock.getQuantity())
                            .message(message)
                            .createdAt(stock.getUpdatedAt().toLocalDateTime())
                            .build());
                });
    }

    private void generateStockMovementAlerts(List<InventoryAlertResponse> alerts) {
        productStockRepository.findAll().stream()
                .filter(stock -> stock.getQuantity().compareTo(BigDecimal.ZERO) > 0)
                .forEach(stock -> {
                    BigDecimal averageMovement = calculateAverageStockMovement(stock.getId());
                    BigDecimal currentMovement = calculateCurrentStockMovement(stock.getId());
                    
                    if (isUnusualMovement(currentMovement, averageMovement)) {
                        InventoryAlertResponse.AlertSeverity severity = calculateStockMovementSeverity(currentMovement, averageMovement);
                        alerts.add(InventoryAlertResponse.builder()
                                .type(InventoryAlertResponse.AlertType.STOCK_MOVEMENT)
                                .severity(severity)
                                .productStockId(stock.getId())
                                .productName(stock.getProductName())
                                .depotName(stock.getDepotName())
                                .currentQuantity(stock.getQuantity())
                                .message(generateStockMovementMessage(stock, currentMovement, averageMovement))
                                .createdAt(stock.getUpdatedAt().toLocalDateTime())
                                .build());
                    }

                    // Check for sudden stock depletion
                    if (isSuddenStockDepletion(stock)) {
                        alerts.add(InventoryAlertResponse.builder()
                                .type(InventoryAlertResponse.AlertType.STOCK_MOVEMENT)
                                .severity(InventoryAlertResponse.AlertSeverity.CRITICAL)
                                .productStockId(stock.getId())
                                .productName(stock.getProductName())
                                .depotName(stock.getDepotName())
                                .currentQuantity(stock.getQuantity())
                                .message(generateSuddenDepletionMessage(stock))
                                .createdAt(stock.getUpdatedAt().toLocalDateTime())
                                .build());
                    }
                });
    }

    private void generateCostAlerts(List<InventoryAlertResponse> alerts) {
        productStockRepository.findAll().stream()
                .filter(stock -> stock.getUnitCost() != null)
                .forEach(stock -> {
                    BigDecimal averageCost = calculateAverageUnitCost(stock.getProductId());
                    BigDecimal currentCost = stock.getUnitCost();
                    
                    if (isSignificantCostChange(currentCost, averageCost)) {
                        InventoryAlertResponse.AlertSeverity severity = calculateCostAlertSeverity(currentCost, averageCost);
                        alerts.add(InventoryAlertResponse.builder()
                                .type(InventoryAlertResponse.AlertType.COST_ALERT)
                                .severity(severity)
                                .productStockId(stock.getId())
                                .productName(stock.getProductName())
                                .depotName(stock.getDepotName())
                                .message(generateCostAlertMessage(stock, currentCost, averageCost))
                                .createdAt(stock.getUpdatedAt().toLocalDateTime())
                                .build());
                    }

                    // Check for cost trends
                    if (isCostTrendSignificant(stock.getProductId())) {
                        alerts.add(InventoryAlertResponse.builder()
                                .type(InventoryAlertResponse.AlertType.COST_ALERT)
                                .severity(InventoryAlertResponse.AlertSeverity.WARNING)
                                .productStockId(stock.getId())
                                .productName(stock.getProductName())
                                .depotName(stock.getDepotName())
                                .message(generateCostTrendMessage(stock))
                                .createdAt(stock.getUpdatedAt().toLocalDateTime())
                                .build());
                    }
                });
    }

    private void generateReservationAlerts(List<InventoryAlertResponse> alerts) {
        productStockRepository.findAll().stream()
                .filter(stock -> stock.getReservedQuantity().compareTo(BigDecimal.ZERO) > 0)
                .forEach(stock -> {
                    BigDecimal reservedPercentage = stock.getReservedQuantity()
                            .multiply(new BigDecimal("100"))
                            .divide(stock.getQuantity(), 2, RoundingMode.HALF_UP);
                    
                    if (reservedPercentage.compareTo(new BigDecimal("80")) >= 0) {
                        alerts.add(InventoryAlertResponse.builder()
                                .type(InventoryAlertResponse.AlertType.RESERVATION_ALERT)
                                .severity(InventoryAlertResponse.AlertSeverity.WARNING)
                                .productStockId(stock.getId())
                                .productName(stock.getProductName())
                                .depotName(stock.getDepotName())
                                .currentQuantity(stock.getQuantity())
                                .message(generateReservationAlertMessage(stock, reservedPercentage))
                                .createdAt(stock.getUpdatedAt().toLocalDateTime())
                                .build());
                    }
                });
    }

    private InventoryAlertResponse.AlertSeverity calculateLowStockSeverity(BigDecimal currentQuantity, BigDecimal minimumQuantity) {
        BigDecimal percentage = currentQuantity.multiply(new BigDecimal("100"))
                .divide(minimumQuantity, 2, RoundingMode.HALF_UP);
        
        if (percentage.compareTo(new BigDecimal("10")) <= 0) {
            return InventoryAlertResponse.AlertSeverity.CRITICAL;
        } else if (percentage.compareTo(new BigDecimal("30")) <= 0) {
            return InventoryAlertResponse.AlertSeverity.WARNING;
        } else {
            return InventoryAlertResponse.AlertSeverity.INFO;
        }
    }

    private InventoryAlertResponse.AlertSeverity calculateExpirySeverity(long daysUntilExpiry) {
        if (daysUntilExpiry <= 7) {
            return InventoryAlertResponse.AlertSeverity.CRITICAL;
        } else if (daysUntilExpiry <= 14) {
            return InventoryAlertResponse.AlertSeverity.WARNING;
        } else {
            return InventoryAlertResponse.AlertSeverity.INFO;
        }
    }

    private InventoryAlertResponse.AlertSeverity calculateQualitySeverity(ProductStock.QualityStatus status) {
        switch (status) {
            case RECALLED:
                return InventoryAlertResponse.AlertSeverity.CRITICAL;
            case DAMAGED:
            case QUARANTINED:
                return InventoryAlertResponse.AlertSeverity.WARNING;
            default:
                return InventoryAlertResponse.AlertSeverity.INFO;
        }
    }

    private InventoryAlertResponse.AlertSeverity calculateCostAlertSeverity(BigDecimal currentCost, BigDecimal averageCost) {
        BigDecimal percentageChange = currentCost.subtract(averageCost)
                .multiply(new BigDecimal("100"))
                .divide(averageCost, 2, RoundingMode.HALF_UP)
                .abs();
        
        if (percentageChange.compareTo(new BigDecimal("50")) >= 0) {
            return InventoryAlertResponse.AlertSeverity.CRITICAL;
        } else if (percentageChange.compareTo(new BigDecimal("20")) >= 0) {
            return InventoryAlertResponse.AlertSeverity.WARNING;
        } else {
            return InventoryAlertResponse.AlertSeverity.INFO;
        }
    }

    private String generateLowStockMessage(ProductStock stock, BigDecimal currentQuantity, BigDecimal minimumQuantity, InventoryAlertResponse.AlertSeverity severity) {
        return String.format("%s: Product '%s' in depot '%s' has only %s units remaining (minimum: %s). " +
                "Please reorder soon to maintain optimal stock levels.",
                severity, stock.getProductName(), stock.getDepotName(), currentQuantity, minimumQuantity);
    }

    private String generateExpiryMessage(ProductStock stock, long daysUntilExpiry, InventoryAlertResponse.AlertSeverity severity) {
        return String.format("%s: Product '%s' in depot '%s' will expire in %d days. " +
                "Current stock: %s units. Consider discounting or removing from inventory.",
                severity, stock.getProductName(), stock.getDepotName(), daysUntilExpiry, stock.getQuantity());
    }

    private String generateQualityMessage(ProductStock stock, InventoryAlertResponse.AlertSeverity severity) {
        return String.format("%s: Product '%s' in depot '%s' has quality status '%s'. " +
                "Notes: %s. Current stock: %s units.",
                severity, stock.getProductName(), stock.getDepotName(), 
                stock.getQualityStatus(), stock.getQualityNotes(), stock.getQuantity());
    }

    private String generateStockMovementMessage(ProductStock stock, BigDecimal currentMovement, BigDecimal averageMovement) {
        return String.format("Unusual stock movement detected for product '%s' in depot '%s'. " +
                "Current movement: %s units, Average movement: %s units.",
                stock.getProductName(), stock.getDepotName(), currentMovement, averageMovement);
    }

    private String generateCostAlertMessage(ProductStock stock, BigDecimal currentCost, BigDecimal averageCost) {
        BigDecimal percentageChange = currentCost.subtract(averageCost)
                .multiply(new BigDecimal("100"))
                .divide(averageCost, 2, RoundingMode.HALF_UP);
        
        return String.format("Significant cost change detected for product '%s' in depot '%s'. " +
                "Current cost: %s, Average cost: %s (Change: %s%%)",
                stock.getProductName(), stock.getDepotName(), currentCost, averageCost, percentageChange);
    }

    private String generateReservationAlertMessage(ProductStock stock, BigDecimal reservedPercentage) {
        return String.format("High reservation rate for product '%s' in depot '%s'. " +
                "%.2f%% of stock is reserved. Current stock: %s units, Reserved: %s units.",
                stock.getProductName(), stock.getDepotName(), reservedPercentage, 
                stock.getQuantity(), stock.getReservedQuantity());
    }

    private BigDecimal calculateAverageStockMovement(Long stockId) {
        // Get stock movement history for the last 30 days
        LocalDate endDate = LocalDate.now();
        LocalDate startDate = endDate.minusDays(30);
        List<StockMovementResponse> movements = getStockMovementHistory(stockId, startDate, endDate);
        
        if (movements.isEmpty()) {
            return BigDecimal.ZERO;
        }

        // Calculate average daily movement
        BigDecimal totalMovement = movements.stream()
                .map(StockMovementResponse::getQuantity)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        return totalMovement.divide(new BigDecimal(movements.size()), 2, RoundingMode.HALF_UP);
    }

    private BigDecimal calculateCurrentStockMovement(Long stockId) {
        // Get stock movement for the last 24 hours
        LocalDate endDate = LocalDate.now();
        LocalDate startDate = endDate.minusDays(1);
        List<StockMovementResponse> movements = getStockMovementHistory(stockId, startDate, endDate);
        
        return movements.stream()
                .map(StockMovementResponse::getQuantity)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private BigDecimal calculateAverageUnitCost(Long productId) {
        // Get all stock entries for the product
        List<ProductStock> stocks = productStockRepository.findByProductId(productId);
        
        if (stocks.isEmpty()) {
            return BigDecimal.ZERO;
        }

        // Calculate weighted average cost based on quantities
        BigDecimal totalQuantity = stocks.stream()
                .map(ProductStock::getQuantity)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        if (totalQuantity.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO;
        }

        BigDecimal weightedCost = stocks.stream()
                .map(stock -> stock.getUnitCost().multiply(stock.getQuantity()))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        return weightedCost.divide(totalQuantity, 2, RoundingMode.HALF_UP);
    }

    private boolean isUnusualMovement(BigDecimal currentMovement, BigDecimal averageMovement) {
        if (averageMovement.compareTo(BigDecimal.ZERO) == 0) return false;
        
        BigDecimal percentageChange = currentMovement.subtract(averageMovement)
                .multiply(new BigDecimal("100"))
                .divide(averageMovement, 2, RoundingMode.HALF_UP)
                .abs();
        
        return percentageChange.compareTo(new BigDecimal("50")) >= 0;
    }

    private boolean isSignificantCostChange(BigDecimal currentCost, BigDecimal averageCost) {
        if (averageCost.compareTo(BigDecimal.ZERO) == 0) return false;
        
        BigDecimal percentageChange = currentCost.subtract(averageCost)
                .multiply(new BigDecimal("100"))
                .divide(averageCost, 2, RoundingMode.HALF_UP)
                .abs();
        
        return percentageChange.compareTo(new BigDecimal("20")) >= 0;
    }

    private InventoryAlertResponse.AlertSeverity calculateStockMovementSeverity(BigDecimal currentMovement, BigDecimal averageMovement) {
        if (averageMovement.compareTo(BigDecimal.ZERO) == 0) {
            return InventoryAlertResponse.AlertSeverity.INFO;
        }

        BigDecimal percentageChange = currentMovement.subtract(averageMovement)
                .multiply(new BigDecimal("100"))
                .divide(averageMovement, 2, RoundingMode.HALF_UP)
                .abs();
        
        if (percentageChange.compareTo(new BigDecimal("200")) >= 0) {
            return InventoryAlertResponse.AlertSeverity.CRITICAL;
        } else if (percentageChange.compareTo(new BigDecimal("100")) >= 0) {
            return InventoryAlertResponse.AlertSeverity.WARNING;
        } else {
            return InventoryAlertResponse.AlertSeverity.INFO;
        }
    }

    private boolean isSuddenStockDepletion(ProductStock stock) {
        // Get stock movement for the last 24 hours
        LocalDate endDate = LocalDate.now();
        LocalDate startDate = endDate.minusDays(1);
        List<StockMovementResponse> movements = getStockMovementHistory(stock.getId(), startDate, endDate);
        
        if (movements.isEmpty()) {
            return false;
        }

        // Check if more than 50% of stock was depleted in 24 hours
        BigDecimal totalDepletion = movements.stream()
                .filter(m -> m.getQuantity().compareTo(BigDecimal.ZERO) < 0)
                .map(m -> m.getQuantity().abs())
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        return totalDepletion.multiply(new BigDecimal("2")).compareTo(stock.getQuantity()) >= 0;
    }

    private boolean isCostTrendSignificant(Long productId) {
        // Get cost history for the last 90 days
        List<ProductStock> stocks = productStockRepository.findByProductId(productId);
        if (stocks.size() < 3) {
            return false;
        }

        // Calculate cost trend
        BigDecimal firstCost = stocks.get(0).getUnitCost();
        BigDecimal lastCost = stocks.get(stocks.size() - 1).getUnitCost();
        BigDecimal percentageChange = lastCost.subtract(firstCost)
                .multiply(new BigDecimal("100"))
                .divide(firstCost, 2, RoundingMode.HALF_UP);
        
        return percentageChange.abs().compareTo(new BigDecimal("10")) >= 0;
    }

    private String generateSuddenDepletionMessage(ProductStock stock) {
        return String.format("CRITICAL: Sudden stock depletion detected for product '%s' in depot '%s'. " +
                "More than 50%% of stock was depleted in the last 24 hours. " +
                "Current stock: %s units. Please investigate immediately.",
                stock.getProductName(), stock.getDepotName(), stock.getQuantity());
    }

    private String generateCostTrendMessage(ProductStock stock) {
        BigDecimal averageCost = calculateAverageUnitCost(stock.getProductId());
        BigDecimal percentageChange = stock.getUnitCost().subtract(averageCost)
                .multiply(new BigDecimal("100"))
                .divide(averageCost, 2, RoundingMode.HALF_UP);
        
        String trend = percentageChange.compareTo(BigDecimal.ZERO) > 0 ? "increased" : "decreased";
        
        return String.format("WARNING: Significant cost trend detected for product '%s' in depot '%s'. " +
                "Cost has %s by %.2f%% over the last 90 days. " +
                "Current cost: %s, Average cost: %s. " +
                "Consider reviewing pricing strategy.",
                stock.getProductName(), stock.getDepotName(), trend, 
                percentageChange.abs(), stock.getUnitCost(), averageCost);
    }

    @Override
    public long countProductsByQualityStatus(ProductStock.QualityStatus status) {
        log.info("Counting products with quality status: {}", status);
        return productStockRepository.countByQualityStatus(status);
    }

    @Override
    public long countLowStockProducts() {
        log.info("Counting low stock products");
        return productStockRepository.findAll().stream()
                .filter(stock -> stock.getMinimumQuantity() != null && 
                        stock.getQuantity().compareTo(stock.getMinimumQuantity()) <= 0)
                .count();
    }

    @Override
    public long countExpiredProducts() {
        log.info("Counting expired products");
        LocalDate today = LocalDate.now();
        return productStockRepository.findAll().stream()
                .filter(stock -> stock.getExpiryDate() != null && 
                        stock.getExpiryDate().isBefore(today))
                .count();
    }
}