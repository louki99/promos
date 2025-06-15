package ma.foodplus.ordering.system.promos.dto;

import java.util.Map;
import lombok.Data;

@Data
public class PromotionCalculationRequest {
    private Integer promotionId;
    private Map<Long, Integer> basketItems;
} 