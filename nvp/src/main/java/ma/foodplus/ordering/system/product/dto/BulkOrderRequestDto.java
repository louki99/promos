package ma.foodplus.ordering.system.product.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.ZonedDateTime;

@Data
public class BulkOrderRequestDto {
    @NotNull(message = "Product ID is required")
    private Long productId;

    @NotNull(message = "Quantity is required")
    @Min(value = 1, message = "Quantity must be at least 1")
    private Integer quantity;

    private String deliveryAddress;
    private ZonedDateTime preferredDeliveryDate;
    private String specialInstructions;
    private Boolean requiresColdStorage;
    private String packagingPreference;
    private String paymentTerms;
} 