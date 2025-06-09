package ma.foodplus.ordering.system.inventory.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Response DTO for inventory alerts")
public class InventoryAlertResponse {

    @Schema(description = "Type of alert")
    private AlertType type;

    @Schema(description = "Severity level of the alert")
    private AlertSeverity severity;

    @Schema(description = "ID of the product stock")
    private Long productStockId;

    @Schema(description = "Name of the product")
    private String productName;

    @Schema(description = "Name of the depot")
    private String depotName;

    @Schema(description = "Current quantity in stock")
    private BigDecimal currentQuantity;

    @Schema(description = "Threshold quantity that triggered the alert")
    private BigDecimal thresholdQuantity;

    @Schema(description = "Expiry date for expiry-related alerts")
    private LocalDate expiryDate;

    @Schema(description = "Alert message")
    private String message;

    @Schema(description = "When the alert was created")
    private LocalDateTime createdAt;

    @Schema(description = "Whether the alert has been acknowledged")
    private boolean acknowledged;

    @Schema(description = "User who acknowledged the alert")
    private String acknowledgedBy;

    @Schema(description = "When the alert was acknowledged")
    private LocalDateTime acknowledgedAt;

    public enum AlertType {
        LOW_STOCK,
        EXPIRY_WARNING,
        QUALITY_ISSUE,
        STOCK_MOVEMENT,
        COST_ALERT,
        RESERVATION_ALERT
    }

    public enum AlertSeverity {
        INFO,
        WARNING,
        CRITICAL
    }
} 