package ma.foodplus.ordering.system.promos.dto;

import java.util.Map;
import lombok.Data;

@Data
public class PromotionValidationRequest {
    private String promoCode;
    private Long customerId;
    private Map<Long, Integer> basketItems;
} 