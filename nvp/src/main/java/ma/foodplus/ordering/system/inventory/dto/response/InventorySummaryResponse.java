package ma.foodplus.ordering.system.inventory.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Data
@Builder
@Schema(description = "Inventory Summary Response")
public class InventorySummaryResponse {
    @Schema(description = "Total number of products in inventory")
    private long totalProducts;

    @Schema(description = "Total value of inventory")
    private BigDecimal totalValue;

    @Schema(description = "Number of products with low stock")
    private long lowStockCount;

    @Schema(description = "Number of expired products")
    private long expiredCount;

    @Schema(description = "Number of quarantined products")
    private long quarantinedCount;

    @Schema(description = "Number of damaged products")
    private long damagedCount;

    @Schema(description = "Number of recalled products")
    private long recalledCount;

    @Schema(description = "Number of items requiring quality inspection")
    private long inspectionRequiredCount;

    @Schema(description = "Number of items in quarantine")
    private long quarantineCount;

    @Schema(description = "Distribution of products by quality status")
    private Map<String, Long> qualityStatusDistribution;

    @Schema(description = "Distribution of products by depot")
    private Map<String, Long> depotDistribution;

    @Schema(description = "Top 5 most valuable products")
    private List<ProductValueSummary> topValuableProducts;

    @Schema(description = "Top 5 products with highest quantity")
    private List<ProductQuantitySummary> topQuantityProducts;

    @Data
    @Schema(description = "Product Value Summary")
    public static class ProductValueSummary {
        @Schema(description = "Product ID")
        private Long productId;

        @Schema(description = "Product name")
        private String productName;

        @Schema(description = "Total value in stock")
        private BigDecimal totalValue;
    }

    @Data
    @Schema(description = "Product Quantity Summary")
    public static class ProductQuantitySummary {
        @Schema(description = "Product ID")
        private Long productId;

        @Schema(description = "Product name")
        private String productName;

        @Schema(description = "Total quantity in stock")
        private BigDecimal totalQuantity;
    }
} 