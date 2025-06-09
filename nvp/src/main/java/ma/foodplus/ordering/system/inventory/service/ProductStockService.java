package ma.foodplus.ordering.system.inventory.service;

import ma.foodplus.ordering.system.inventory.dto.request.*;
import ma.foodplus.ordering.system.inventory.dto.response.*;
import ma.foodplus.ordering.system.inventory.model.ProductStock;
import ma.foodplus.ordering.system.inventory.dto.request.StockTransferRequest;
import ma.foodplus.ordering.system.inventory.dto.response.StockTransferResponse;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface ProductStockService {
    
    // CRUD Operations
    ProductStockResponse createProductStock(ProductStockRequest request);
    ProductStockResponse updateProductStock(Long id, ProductStockRequest request);
    void deleteProductStock(Long id);
    ProductStockResponse getProductStockById(Long id);
    List<ProductStockResponse> getAllProductStocks();
    
    // Bulk Operations
    BulkProductStockResponse processBulkOperation(BulkProductStockRequest request);
    
    // Query Operations
    List<ProductStockResponse> getProductStocksByProductId(Long productId);
    List<ProductStockResponse> getProductStocksByDepotId(Long depotId);
    List<ProductStockResponse> getLowStockProducts();
    List<ProductStockResponse> getExpiredProducts();
    List<ProductStockResponse> getProductsExpiringBefore(LocalDate date);
    List<ProductStockResponse> searchProductStocks(String searchTerm);
    
    // Stock Management
    ProductStockResponse updateStockQuantity(Long id, BigDecimal newQuantity);
    ProductStockResponse updateUnitCost(Long id, BigDecimal newUnitCost);
    ProductStockResponse updateExpiryDate(Long id, LocalDate newExpiryDate);
    StockTransferResponse transferStock(Long id, StockTransferRequest request);
    List<StockMovementResponse> getStockMovementHistory(Long id, LocalDate startDate, LocalDate endDate);
    void reserveStock(Long id, Double quantity);
    void releaseReservedStock(Long id, Double quantity);
    
    // Quality Management
    ProductStockResponse updateQualityStatus(Long id, ProductStock.QualityStatus newStatus);
    ProductStockResponse quarantineProduct(Long id, String reason);
    ProductStockResponse releaseFromQuarantine(Long id);
    ProductStockResponse markForInspection(Long id);
    ProductStockResponse markAsDamaged(Long id, String damageDescription);
    ProductStockResponse recallProduct(Long id, String recallReason);
    
    // Quality Status Queries
    List<ProductStockResponse> getProductsByQualityStatus(ProductStock.QualityStatus status);
    List<ProductStockResponse> getProductsInQuarantine();
    List<ProductStockResponse> getProductsRequiringInspection();
    List<ProductStockResponse> getDamagedProducts();
    List<ProductStockResponse> getRecalledProducts();
    
    // Statistics and Reporting
    long countProductsByQualityStatus(ProductStock.QualityStatus status);
    long countLowStockProducts();
    long countExpiredProducts();
    InventorySummaryResponse getInventorySummary();
    List<InventoryAlertResponse> getInventoryAlerts(InventoryAlertResponse.AlertType alertType);
} 