package ma.foodplus.ordering.system.inventory.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import ma.foodplus.ordering.system.inventory.model.ProductStock.QualityStatus;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

@Data
public class ProductStockDTO {
    private Long id;

    @NotNull(message = "Product ID is required")
    private Long productId;

    @NotNull(message = "Depot is required")
    private DepotDTO depot;

    private String deNo;
    private Integer qteMini;
    private Integer qteMax;
    private Integer qteSto;
    private Integer qteReserved;
    private Integer qteAvailable;
    private BigDecimal unitCost;
    private BigDecimal totalValue;
    private ZonedDateTime lastPurchaseDate;
    private ZonedDateTime lastSaleDate;
    private Integer averageDailyUsage;
    private Integer daysOfStock;
    private Integer reorderPoint;
    private Integer reorderQuantity;
    private Integer shelfLifeDays;
    private ZonedDateTime expiryDate;
    private String batchNumber;
    private String serialNumber;
    private String locationCode;
    private String storageCondition;
    private QualityStatus qualityStatus;
    private boolean principal;
    private String notes;
    private ZonedDateTime createdAt;
    private ZonedDateTime updatedAt;
} 