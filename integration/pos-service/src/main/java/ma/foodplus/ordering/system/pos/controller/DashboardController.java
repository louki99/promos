package ma.foodplus.ordering.system.pos.controller;

import ma.foodplus.ordering.system.pos.service.SaleService;
import ma.foodplus.ordering.system.pos.service.InventoryService;
import ma.foodplus.ordering.system.pos.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/dashboard")
@Tag(name = "Dashboard & Reporting", description = "Endpoints for dashboard statistics and business reporting.")
public class DashboardController {
    @Autowired
    private SaleService saleService;
    
    @Autowired
    private InventoryService inventoryService;
    
    @Autowired
    private ProductService productService;

    @GetMapping("/sales-summary")
    @Operation(summary = "Get sales summary", description = "Get sales summary statistics for a date range.")
    public ResponseEntity<Map<String, Object>> getSalesSummary(
            @RequestParam Long storeId,
            @RequestParam LocalDateTime startDate,
            @RequestParam LocalDateTime endDate) {
        
        BigDecimal totalSales = saleService.getTotalSales(storeId, startDate, endDate);
        Long transactionCount = saleService.getTransactionCount(storeId, startDate, endDate);
        
        Map<String, Object> summary = new HashMap<>();
        summary.put("totalSales", totalSales);
        summary.put("transactionCount", transactionCount);
        summary.put("averageTransactionValue", transactionCount > 0 ? 
            totalSales.divide(BigDecimal.valueOf(transactionCount), 2, BigDecimal.ROUND_HALF_UP) : BigDecimal.ZERO);
        summary.put("startDate", startDate);
        summary.put("endDate", endDate);
        summary.put("storeId", storeId);
        
        return ResponseEntity.ok(summary);
    }

    @GetMapping("/top-selling-products")
    @Operation(summary = "Get top selling products", description = "Get top selling products for a date range.")
    public ResponseEntity<List<Object[]>> getTopSellingProducts(
            @RequestParam LocalDateTime startDate,
            @RequestParam LocalDateTime endDate) {
        
        List<Object[]> topProducts = saleService.getTopSellingProducts(startDate, endDate);
        return ResponseEntity.ok(topProducts);
    }

    @GetMapping("/inventory-alerts")
    @Operation(summary = "Get inventory alerts", description = "Get products with low stock levels.")
    public ResponseEntity<Map<String, Object>> getInventoryAlerts(@RequestParam Long storeId) {
        
        List<Object[]> lowStockProducts = inventoryService.getLowStockProducts(storeId);
        List<Object[]> outOfStockProducts = inventoryService.getOutOfStockProducts(storeId);
        
        Map<String, Object> alerts = new HashMap<>();
        alerts.put("lowStockProducts", lowStockProducts);
        alerts.put("outOfStockProducts", outOfStockProducts);
        alerts.put("totalLowStock", lowStockProducts.size());
        alerts.put("totalOutOfStock", outOfStockProducts.size());
        alerts.put("storeId", storeId);
        
        return ResponseEntity.ok(alerts);
    }

    @GetMapping("/revenue-trends")
    @Operation(summary = "Get revenue trends", description = "Get daily revenue trends for a date range.")
    public ResponseEntity<Map<String, Object>> getRevenueTrends(
            @RequestParam Long storeId,
            @RequestParam LocalDateTime startDate,
            @RequestParam LocalDateTime endDate) {
        
        List<Object[]> dailyRevenue = saleService.getDailyRevenue(storeId, startDate, endDate);
        
        Map<String, Object> trends = new HashMap<>();
        trends.put("dailyRevenue", dailyRevenue);
        trends.put("startDate", startDate);
        trends.put("endDate", endDate);
        trends.put("storeId", storeId);
        
        return ResponseEntity.ok(trends);
    }

    @GetMapping("/product-performance")
    @Operation(summary = "Get product performance", description = "Get product performance metrics.")
    public ResponseEntity<Map<String, Object>> getProductPerformance(@RequestParam Long storeId) {
        
        List<Object[]> bestSellers = saleService.getTopSellingProducts(
            LocalDateTime.now().minusDays(30), LocalDateTime.now());
        List<Object[]> slowMoving = inventoryService.getSlowMovingProducts(storeId);
        
        Map<String, Object> performance = new HashMap<>();
        performance.put("bestSellers", bestSellers);
        performance.put("slowMovingProducts", slowMoving);
        performance.put("storeId", storeId);
        
        return ResponseEntity.ok(performance);
    }
} 