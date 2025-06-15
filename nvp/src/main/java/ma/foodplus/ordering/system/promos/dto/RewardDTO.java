package ma.foodplus.ordering.system.promos.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RewardDTO {
    private Integer id;

    @NotBlank(message = "Reward type is required")
    private String rewardType;

    @NotNull(message = "Value is required")
    @Positive(message = "Value must be positive")
    private BigDecimal value;

    @NotBlank(message = "Description is required")
    private String description;

    private String productId;
    private String categoryId;
    private String familyCode;
    private Integer quantity;
    private Boolean isPercentage;
    private Boolean isActive;
    private Integer promotionId;
} 