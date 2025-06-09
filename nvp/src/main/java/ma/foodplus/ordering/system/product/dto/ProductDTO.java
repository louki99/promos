package ma.foodplus.ordering.system.product.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Product Data Transfer Object")
public class ProductDTO {
    
    @Schema(description = "Unique identifier of the product")
    private Long id;

    @Schema(description = "Name of the product")
    private String name;

    @Schema(description = "Description of the product")
    private String description;

    @Schema(description = "Category of the product")
    private String category;

    @Schema(description = "Demand level of the product")
    private DemandLevel demandLevel;

    @Schema(description = "Base price of the product")
    private BigDecimal basePrice;

    @Schema(description = "Current stock level")
    private BigDecimal currentStock;

    @Schema(description = "Minimum stock level")
    private BigDecimal minimumStock;

    @Schema(description = "Maximum stock level")
    private BigDecimal maximumStock;

    @Schema(description = "Whether the product is active")
    private boolean active;

    @Schema(description = "Creation timestamp")
    private LocalDateTime createdAt;

    @Schema(description = "Last update timestamp")
    private LocalDateTime updatedAt;

    public enum DemandLevel {
        HIGH,
        MEDIUM,
        LOW
    }
} 