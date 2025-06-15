package ma.foodplus.ordering.system.promos.dto;

import java.math.BigDecimal;
import lombok.Data;
import lombok.AllArgsConstructor;

@Data
@AllArgsConstructor
public class PromotionCalculationResponse {
    private BigDecimal totalDiscount;
} 